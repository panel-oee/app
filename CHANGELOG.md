# Changelog 3.8.1

## [3.8.1] — 2026-11-02

### Zmieniono
- W systemie dwuzmianowym raport jest przypisywany do dnia kalendarzowego, w którym trwa zmiana.
- Panel po uruchomieniu zawsze otwiera bieżącą datę kalendarzową, również po weekendzie.
- Zmiana 1 jest wybierana w godzinach 06:00–13:59 oraz przed 06:00.
- Zmiana 2 jest wybierana od 14:00, również po godzinie 22:00 jako zakończona zmiana 2.
- Zapytanie raportów filtruje jednocześnie wybraną zmianę i zakres dat.

### Zachowano
- Pareto przezbrojeń niezależne od sztuk wadliwych.
- Obsługę `downtime_minutes`.
- Aktualizacje PWA bez cache `version.json`.
- Nawigację w jednym rzędzie na telefonie.
