# Changelog

## [3.6.0] - 2026-08-23

### Dodano
- Centrum przypomnień obsługujące wiele niezależnych harmonogramów.
- Typy: brak raportu OEE za poprzedni dzień, brak raportu bieżącego, niewykonane 6S, przegląd maszyny i własne przypomnienie.
- Osobną godzinę, dni tygodnia, treść i status dla każdego przypomnienia.
- Opcjonalne ponowienie po wybranej liczbie minut.
- Edycję, włączanie, wyłączanie i usuwanie przypomnień.
- Historię wysłanych i pominiętych komunikatów.
- Synchronizację ustawień z tabelą Supabase `oee_reminders` z automatycznym trybem lokalnym.
- Powiadomienie testowe.

### Zmieniono
- Zastąpiono pojedyncze przypomnienie z 3.5.0 rozbudowanym Centrum przypomnień.
- Podniesiono wersję panelu, manifestu, version.json i Service Workera do 3.6.0.
