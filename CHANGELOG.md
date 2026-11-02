# Changelog 3.8.2

## [3.8.2] — 2026-11-02

### Naprawiono
- Przywrócono przycisk włączania powiadomień i powiadomienie testowe.
- Dodano przypomnienia 5 minut przed przerwą i w chwili rozpoczęcia przerwy lub 6S.
- Dodano kontrolę brakującego raportu o 13:50 dla zmiany 1 i o 21:50 dla zmiany 2.
- Wzmocniono sprawdzanie `version.json` bez cache i dodano ręczny przycisk „Sprawdź aktualizację”.
- Service Worker używa `updateViaCache: none`, network-first dla HTML i natychmiast aktywuje nowe wydanie.

### Ważne
- Powiadomienia trzeba jednorazowo zaakceptować przyciskiem „Włącz powiadomienia”.
- Harmonogram lokalny działa, gdy PWA jest uruchomiona albo pozostaje aktywna w tle.
