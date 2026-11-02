# Changelog 3.8.3

- Usunięto odwołanie do nieistniejącego `assets/quantum-forge.css`, które powodowało błędy 404.
- Style niezbędne do wykresu pozostają osadzone bezpośrednio w `panel.html`.
- Przywrócono bezpieczne `select('*')` dla zgodności ze schematem Supabase, pozostawiając filtrowanie daty i zmiany.
- Dodano widoczny komunikat diagnostyczny, jeśli pobieranie danych wykresu zakończy się błędem.
- Zachowano aktualizacje PWA i powiadomienia lokalne.
