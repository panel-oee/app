# Panel OEE — pakiet GitHub

Ta paczka dodaje aplikację Android do istniejącego repozytorium `panel-oee/app`, w którym jest już strona PWA. Nie zastępuj plików PWA z katalogu głównego.

## Co jest w paczce

- `.github/workflows/android-release.yml` — buduje i publikuje podpisany APK po utworzeniu taga `vX.Y.Z`.
- `android/PanelOEE/` — projekt Android Studio z kontrolą najnowszego GitHub Release.
- APK startowy zostanie zbudowany obok tego pliku instrukcji jako `Panel-OEE-2.2.0.apk`.

## 1. Skopiuj paczkę do repozytorium GitHub

Najbezpieczniej użyj GitHub Desktop:

1. Zainstaluj GitHub Desktop i zaloguj się do konta mającego zapis do `https://github.com/panel-oee/app`.
2. Wybierz **File → Clone repository**, wskaż `panel-oee/app` i sklonuj repozytorium na komputer.
3. Skopiuj z tej paczki katalog `android` do głównego katalogu sklonowanego repozytorium.
4. Skopiuj `.github/workflows/android-release.yml` do `.github/workflows/` w sklonowanym repozytorium. Jeśli katalog `.github/workflows` już istnieje, dodaj tylko ten plik i pozostaw pozostałe workflow.
5. Nie kopiuj `README.md`, `panel.html`, `config_panel.html` ani pozostałych plików PWA z katalogu głównego projektu Androida do katalogu głównego repozytorium. Pliki PWA mają tam pozostać.
6. W GitHub Desktop sprawdź listę zmian, wpisz opis `Add Android app and release workflow`, wybierz **Commit to main**, a potem **Push origin**.

## 2. Dodaj podpis aplikacji do GitHub Secrets

Aktualizator Androida może instalować APK jako aktualizację tylko wtedy, gdy nowy APK jest podpisany tym samym kluczem co zainstalowana aplikacja. Dotychczasowe APK 1.0–2.1 budowaliśmy jako debug na tym komputerze, więc do zachowania możliwości aktualizacji użyj jego lokalnego debug keystore jako sekretu Actions. Klucza nie wolno commitować do repozytorium.

Na komputerze, na którym budowano poprzednie APK, otwórz PowerShell i skopiuj zawartość debug keystore do schowka:

```powershell
$keyPath = Join-Path $env:USERPROFILE '.android\debug.keystore'
[Convert]::ToBase64String([IO.File]::ReadAllBytes($keyPath)) | Set-Clipboard
```

W GitHub otwórz repozytorium → **Settings → Secrets and variables → Actions → New repository secret**. Dodaj sekrety:

| Nazwa | Wartość |
|---|---|
| `ANDROID_KEYSTORE_BASE64` | zawartość schowka z poprzedniego polecenia |
| `ANDROID_KEYSTORE_PASSWORD` | `android` |
| `ANDROID_KEY_ALIAS` | `androiddebugkey` |
| `ANDROID_KEY_PASSWORD` | `android` |

Te wartości debug keystore są domyślne. Jeśli komputer używa niestandardowego debug keystore, wprowadź jego właściwe hasło i alias. Nie wpisuj sekretów do plików projektu.

## 3. Utwórz pierwszy Release

Po wypchnięciu plików do `main`:

1. Otwórz w GitHub **Releases → Draft a new release**.
2. Utwórz nowy tag `v2.2.0` na gałęzi `main`.
3. Opublikuj Release. Workflow **Android Release** zbuduje podpisany APK i dołączy go do tego Release.
4. Po zakończeniu sprawdź, czy Release zawiera `Panel-OEE-2.2.0.apk`.
5. Zainstaluj ten APK na telefonie. Jeśli stara aplikacja 2.1 jest zainstalowana, powinien zaktualizować ją i zachować jej dane.

Możesz też najpierw zainstalować dostarczony `Panel-OEE-2.2.0.apk` z tej paczki. Jest podpisany tym samym lokalnym kluczem debug co APK 2.1. Podpis sprawdziłem — Android przyjmie go jako aktualizację i zachowa dane. Release v2.2.0 ma tę samą nazwę wersji, więc komunikat o aktualizacji pojawi się dopiero przy kolejnej wersji, np. v2.3.0.

Aktualizator sprawdza GitHub przy każdym uruchomieniu aplikacji. Gdy znajdzie Release o wyższej wersji, pokaże komunikat. Po wybraniu **Pobierz** Android pobierze APK i poprosi o zezwolenie na instalację z tego źródła oraz potwierdzenie aktualizacji.

## 4. Publikuj kolejne wersje

Dla nowej wersji zaktualizuj pliki aplikacji w `android/PanelOEE`, zacommituj je do `main`, a potem utwórz nowy Release z kolejnym tagiem, np. `v2.3.0`. Nie używaj ponownie tagu. Workflow zbuduje APK z wyższym `versionCode`, podpisze tym samym kluczem i dołączy plik `Panel-OEE-2.3.0.apk`. Starsza aplikacja wyświetli aktualizację po ponownym uruchomieniu.

## Ważne

- Repozytorium jest publiczne, więc opublikowany APK i kod aplikacji będą dostępne publicznie.
- Sekrety Actions pozostają poza repozytorium; nie dodawaj keystore ani haseł do commitów.
- Aktualizacja wymaga internetu i potwierdzenia instalacji przez Androida. Aplikacja nie instaluje APK po cichu.
- Komunikat sprawdzany jest przy uruchomieniu aplikacji, a nie w tle, gdy pozostaje otwarta.
- Pozostaw stary debug keystore w bezpiecznym miejscu. Bez tego samego klucza Android nie przyjmie kolejnych APK jako aktualizacji.
