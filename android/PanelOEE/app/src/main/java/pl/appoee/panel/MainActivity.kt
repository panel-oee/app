package pl.appoee.panel

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.provider.Settings
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import java.nio.charset.StandardCharsets
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private var fileChooserCallback: ValueCallback<Array<Uri>>? = null
    private var pendingExportContent: String? = null
    private var pendingDownloadId = -1L
    private var pendingInstallUri: Uri? = null
    private var downloadReceiverRegistered = false
    private val fileChooserRequest = 1001
    private val exportFileRequest = 1002

    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (downloadId != pendingDownloadId) return
            val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query().setFilterById(downloadId)
            manager.query(query).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        manager.getUriForDownloadedFile(downloadId)?.let(::offerInstall)
                            ?: Toast.makeText(this@MainActivity, "Nie udało się odczytać pobranego APK", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Pobieranie aktualizacji nie powiodło się", Toast.LENGTH_LONG).show()
                    }
                }
            }
            pendingDownloadId = -1L
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            loadWithOverviewMode = true
            useWideViewPort = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(false)
        }
        webView.addJavascriptInterface(AndroidBridge(), "PanelOeeNative")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                return if (url.startsWith("file:///android_asset/")) false else {
                    if (url.startsWith("https://") || url.startsWith("http://")) {
                        try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) } catch (_: Exception) { }
                        true
                    } else false
                }
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                view: WebView,
                callback: ValueCallback<Array<Uri>>,
                params: FileChooserParams
            ): Boolean {
                fileChooserCallback?.onReceiveValue(null)
                fileChooserCallback = callback
                return try {
                    startActivityForResult(params.createIntent(), fileChooserRequest)
                    true
                } catch (_: Exception) {
                    fileChooserCallback = null
                    Toast.makeText(this@MainActivity, "Nie można otworzyć wyboru pliku", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
        val downloadFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= 33) registerReceiver(downloadCompleteReceiver, downloadFilter, Context.RECEIVER_NOT_EXPORTED)
        else registerReceiver(downloadCompleteReceiver, downloadFilter)
        downloadReceiverRegistered = true
        if (savedInstanceState == null) webView.loadUrl("file:///android_asset/panel.html")
        else webView.restoreState(savedInstanceState)
        checkForUpdate()
    }

    override fun onResume() {
        super.onResume()
        val uri = pendingInstallUri
        if (uri != null && (Build.VERSION.SDK_INT < 26 || packageManager.canRequestPackageInstalls())) {
            pendingInstallUri = null
            launchInstaller(uri)
        }
    }

    override fun onDestroy() {
        if (downloadReceiverRegistered) unregisterReceiver(downloadCompleteReceiver)
        super.onDestroy()
    }

    private fun checkForUpdate() {
        Thread {
            var connection: HttpURLConnection? = null
            try {
                connection = (URL("https://api.github.com/repos/panel-oee/app/releases/latest").openConnection() as HttpURLConnection).apply {
                    connectTimeout = 8000
                    readTimeout = 8000
                    requestMethod = "GET"
                    setRequestProperty("Accept", "application/vnd.github+json")
                    setRequestProperty("User-Agent", "Panel-OEE-Android")
                }
                if (connection.responseCode != HttpURLConnection.HTTP_OK) return@Thread
                val release = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
                val tag = release.optString("tag_name").removePrefix("v")
                val installed = packageManager.getPackageInfo(packageName, 0).versionName ?: "0.0.0"
                if (compareVersions(tag, installed) <= 0) return@Thread
                val assets = release.optJSONArray("assets") ?: return@Thread
                var apkUrl: String? = null
                for (index in 0 until assets.length()) {
                    val asset = assets.optJSONObject(index) ?: continue
                    val name = asset.optString("name")
                    val candidate = asset.optString("browser_download_url")
                    if (name.startsWith("Panel-OEE-") && name.endsWith(".apk") && candidate.startsWith("https://github.com/panel-oee/app/releases/download/")) {
                        apkUrl = candidate
                        break
                    }
                }
                val resolvedApkUrl = apkUrl ?: return@Thread
                runOnUiThread { showUpdateDialog(tag, installed, resolvedApkUrl) }
            } catch (_: Exception) {
                // Brak sieci lub brak opublikowanego Release nie blokuje pracy aplikacji.
            } finally {
                connection?.disconnect()
            }
        }.start()
    }

    private fun compareVersions(remote: String, installed: String): Int {
        val left = remote.substringBefore('-').split('.').map { it.toIntOrNull() ?: 0 }
        val right = installed.substringBefore('-').split('.').map { it.toIntOrNull() ?: 0 }
        for (index in 0 until maxOf(left.size, right.size)) {
            val a = left.getOrElse(index) { 0 }
            val b = right.getOrElse(index) { 0 }
            if (a != b) return a.compareTo(b)
        }
        return 0
    }

    private fun showUpdateDialog(version: String, installed: String, apkUrl: String) {
        AlertDialog.Builder(this)
            .setTitle("Dostępna aktualizacja Panelu OEE")
            .setMessage("Dostępna wersja: $version\nZainstalowana wersja: $installed\n\nPobrać aktualizację?")
            .setNegativeButton("Później", null)
            .setPositiveButton("Pobierz") { _, _ -> downloadUpdate(version, apkUrl) }
            .show()
    }

    private fun downloadUpdate(version: String, apkUrl: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(apkUrl))
                .setTitle("Panel OEE $version")
                .setDescription("Pobieranie aktualizacji aplikacji")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "Panel-OEE-$version.apk")
            val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            pendingDownloadId = manager.enqueue(request)
            Toast.makeText(this, "Pobieranie aktualizacji rozpoczęte", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(this, "Nie można rozpocząć pobierania aktualizacji", Toast.LENGTH_LONG).show()
        }
    }

    private fun offerInstall(uri: Uri) {
        if (Build.VERSION.SDK_INT >= 26 && !packageManager.canRequestPackageInstalls()) {
            pendingInstallUri = uri
            AlertDialog.Builder(this)
                .setTitle("Zezwól na aktualizację")
                .setMessage("Android wymaga zezwolenia na instalowanie aktualizacji pobranych poza Sklepem Play.")
                .setNegativeButton("Anuluj") { _, _ -> pendingInstallUri = null }
                .setPositiveButton("Ustawienia") { _, _ ->
                    try { startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName"))) }
                    catch (_: Exception) { pendingInstallUri = null }
                }
                .show()
            return
        }
        launchInstaller(uri)
    }

    private fun launchInstaller(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(this, "Android nie może otworzyć instalatora APK", Toast.LENGTH_LONG).show()
        }
    }

    private inner class AndroidBridge {
        @JavascriptInterface
        fun saveTextFile(fileName: String, mimeType: String, content: String) {
            runOnUiThread {
                pendingExportContent = content
                val safeName = fileName.substringAfterLast('/').substringAfterLast('\\').replace(Regex("[^A-Za-z0-9._-]"), "_")
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = mimeType.ifBlank { "text/plain" }
                    putExtra(Intent.EXTRA_TITLE, safeName.ifBlank { "Panel-OEE.csv" })
                }
                try {
                    startActivityForResult(intent, exportFileRequest)
                } catch (_: Exception) {
                    pendingExportContent = null
                    Toast.makeText(this@MainActivity, "Nie można otworzyć zapisu pliku", Toast.LENGTH_SHORT).show()
                }
            }
        }

        @JavascriptInterface
        fun printReport() {
            runOnUiThread {
                try {
                    val printManager = getSystemService(PRINT_SERVICE) as PrintManager
                    val jobName = "Panel OEE — raport"
                    val adapter = webView.createPrintDocumentAdapter(jobName)
                    val attributes = PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                        .build()
                    printManager.print(jobName, adapter, attributes)
                } catch (_: Exception) {
                    Toast.makeText(this@MainActivity, "Nie można przygotować wydruku PDF", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Android, retained for WebView file chooser compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileChooserRequest) {
            fileChooserCallback?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
            fileChooserCallback = null
        } else if (requestCode == exportFileRequest) {
            val content = pendingExportContent
            pendingExportContent = null
            val uri = data?.data
            if (resultCode == RESULT_OK && content != null && uri != null) {
                try {
                    val stream = contentResolver.openOutputStream(uri) ?: error("Nie można otworzyć pliku")
                    stream.use { it.write(content.toByteArray(StandardCharsets.UTF_8)) }
                    Toast.makeText(this, "Zapisano plik", Toast.LENGTH_SHORT).show()
                } catch (_: Exception) {
                    Toast.makeText(this, "Nie udało się zapisać pliku", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
