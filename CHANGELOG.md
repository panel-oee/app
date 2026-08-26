# Changelog

## [3.6.5] - 2026-08-25

### Zmieniono
- Ograniczono zakres pobierania `oee_reports` do początku poprzedniego miesiąca i końca bieżącego miesiąca.
- W głównych zapytaniach Supabase zastąpiono `select('*')` listami wymaganych kolumn.
- Odświeżanie historii 6S na Dashboardzie zmieniono z 2 do 5 minut.
- Wstrzymano odświeżanie historii 6S, gdy karta przeglądarki jest ukryta.
- Własne dźwięki przerw są przesyłane do Supabase Storage `break-sounds`, a harmonogram przechowuje URL zamiast danych Base64.
- Service Worker nie przechwytuje ani nie zapisuje w cache zewnętrznych odpowiedzi API Supabase.
- Zachowano nawigację mobilną w jednym rzędzie oraz Centrum przypomnień.

### Techniczne
- Ujednolicono wersję `3.6.5` w `panel.html`, `config_panel.html`, `version.json`, `manifest.webmanifest` i `sw.js`.
- Nazwa cache PWA: `oee-lakiernia-3.6.5`.
