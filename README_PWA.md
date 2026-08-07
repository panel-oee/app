# Panel OEE Lakiernia PWA

## Pliki produkcyjne
- `panel.html` - główny panel OEE
- `config_panel.html` - konfiguracja przerw i 6S
- `manifest.webmanifest` - konfiguracja instalacji PWA
- `sw.js` - cache i aktualizacje aplikacji
- `offline.html` - ekran awaryjny bez internetu
- `icons/` - ikony aplikacji

## Publikacja na GitHub Pages
1. Wgraj pliki do tego samego katalogu repozytorium.
2. Nie zmieniaj nazw `panel.html`, `config_panel.html`, `manifest.webmanifest` ani `sw.js`.
3. Otwórz stronę przez HTTPS z GitHub Pages. Instalacja PWA nie działa poprawnie po otwarciu pliku bezpośrednio z pamięci telefonu.
4. Android/Chrome: otwórz `panel.html` i wybierz **Zainstaluj aplikację**.
5. iPhone/Safari: **Udostępnij** > **Do ekranu początkowego**.

## Ważne
- Dane Supabase wymagają internetu.
- Interfejs i ostatnio otwarte strony mogą działać offline.
- Pliki `*_legacy_backup.html` są kopią bezpieczeństwa i nie muszą być publikowane.
