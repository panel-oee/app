# Panel OEE Android — GitHub package

This is an Android Studio project for the native companion app. The existing repository root remains the PWA; keep this project under `android/PanelOEE`.

The app checks `https://api.github.com/repos/panel-oee/app/releases/latest` when it opens. A newer `vX.Y.Z` release with a `Panel-OEE-X.Y.Z.apk` asset is offered to the user. Android still asks the user to allow installation and confirm the update.

Build the initial debug APK with `gradlew assembleDebug`. For published releases, use the root `.github/workflows/android-release.yml` workflow and a stable signing key stored only as GitHub Actions secrets. See `../../INSTRUKCJA-GITHUB.md` for the complete setup and release procedure.
