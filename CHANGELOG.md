# Changelog 3.8.6

## [3.8.6] — 2026-11-02

### Naprawiono
- Kalendarz i wykres korzystają z danych zbiorczych obu zmian już podczas pierwszego uruchomienia strony.
- Usunięto konieczność przechodzenia do poprzedniego miesiąca w celu wymuszenia renderowania danych.
- Formularz nadal korzysta z danych aktualnie wybranej zmiany.
- Po ustawieniu formularza wykonywane jest końcowe renderowanie kalendarza, wykresu i kart okresowych na danych zbiorczych.

### Przyczyna
- W 3.8.5 dane zbiorcze były poprawnie pobierane, ale pierwsze wywołanie `buildCalendar()` i `renderTableAndChart()` korzystało z `gemaBazaDanych`, czyli danych wybranej zmiany. Dopiero `changeMonth()` używał `withSummaryData()`.
