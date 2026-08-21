# Changelog

W tym pliku dokumentowane są wszystkie istotne zmiany w aplikacji **Panel OEE Lakiernia**.

Format jest oparty na konwencji [Keep a Changelog](https://keepachangelog.com/pl/1.1.0/), a numeracja wersji jest zgodna z zasadami wersjonowania semantycznego:

- **MAJOR**: duża zmiana sposobu działania aplikacji,
- **MINOR**: nowa funkcja zgodna z dotychczasową aplikacją,
- **PATCH**: poprawka błędu, wyglądu lub działania istniejącej funkcji.

> **Uwaga historyczna:** wpisy dotyczące wersji 1.x i 2.x zostały odtworzone na podstawie stanu aplikacji oraz kolejnych paczek PWA. Od wersji 3.0 historia zmian odpowiada kolejnym aktualizacjom wykonanym podczas rozwoju aplikacji.

---

## [Unreleased]

### Planowane

- Dalsze rozwijanie mechanizmu powiadomień Push działających również po całkowitym zamknięciu aplikacji, z wykorzystaniem serwera lub funkcji działającej w tle.
- Rozważenie centralnego zapisywania ustawień przypomnień w Supabase, aby harmonogram był wspólny dla wielu urządzeń.
- Możliwość dodania kilku niezależnych godzin przypomnień o brakujących danych.
- Możliwość wyboru odbiorców i rodzaju przypomnienia, na przykład raport OEE, zadania 6S albo przegląd maszyny.
- Plan przeglądów maszyn z terminami, statusem wykonania i automatycznym wyliczaniem kolejnego przeglądu.
- Asystent analityczny generujący miesięczne i tygodniowe podsumowania OEE oraz rekomendacje działań.
- Prognoza końcowego OEE miesiąca i prawdopodobieństwo osiągnięcia celu.
- Lista obecności operatorów i analiza wpływu obsady na wynik OEE.
- Integracja danych produkcyjnych na żywo, jeżeli sterownik Gema udostępni dane przez sieć.

### Odłożone na później

- Logowanie kodem PIN.
- Role i uprawnienia użytkowników: Operator, Brygadzista i Kierownik.
- Zdjęcia potwierdzające wykonanie zadań 6S.
- Ranking operatorów 6S.

---

## [3.5.0] - 2026-08-21

### Dodano

- Konfigurowalne przypomnienie o uzupełnieniu brakujących danych OEE.
- Przycisk **„Przypomnienie danych”** w Dashboardzie „Dzisiaj”.
- Możliwość włączenia lub wyłączenia przypomnienia.
- Możliwość wyboru dokładnej godziny przypomnienia.
- Możliwość wyboru raportu podlegającego kontroli:
  - raport za poprzedni dzień roboczy,
  - raport za bieżący dzień.
- Możliwość wyboru dni tygodnia, w których przypomnienie ma działać.
- Możliwość wpisania własnej treści powiadomienia.
- Przycisk wysyłający testowe powiadomienie.
- Ochronę przed wielokrotnym wysłaniem tego samego przypomnienia dla danego raportu, dnia i godziny.
- Otwieranie lub aktywowanie aplikacji po kliknięciu powiadomienia.
- Zapisywanie ustawień przypomnienia lokalnie na urządzeniu.

### Zmieniono

- Numer aplikacji podniesiono do **3.5.0** we wszystkich elementach wydania:
  - `panel.html`,
  - `version.json`,
  - `manifest.webmanifest`,
  - `sw.js`,
  - stopka aplikacji.
- Przycisk przypomnienia pokazuje wybraną godzinę, gdy funkcja jest aktywna.
- Zaktualizowano Service Workera i nazwę pamięci podręcznej.

### Ograniczenia

- Przypomnienie jest lokalne i działa najpewniej, gdy aplikacja jest otwarta, działa w tle albo zostanie uruchomiona w pobliżu wybranej godziny.
- Gwarantowane powiadomienia przy całkowicie zamkniętej aplikacji wymagają zewnętrznego mechanizmu Push działającego po stronie serwera.

---

## [3.4.0] - 2026-08-21

### Dodano

- Automatyczny mechanizm wykrywania nowej wersji aplikacji.
- Plik `version.json` jako źródło informacji o opublikowanej wersji.
- Plik `CHANGELOG.md` w paczce aplikacji.
- Okno **„Dostępna aktualizacja aplikacji”** pokazujące:
  - wersję obecną,
  - wersję dostępną,
  - opis wydania.
- Przyciski **„Później”** i **„Aktualizuj teraz”**.
- Automatyczne sprawdzanie aktualizacji:
  - po uruchomieniu aplikacji,
  - po powrocie aplikacji z tła,
  - po ponownym przejściu do aplikacji,
  - cyklicznie co 15 minut.
- Obsługę komunikatu `SKIP_WAITING` w Service Workerze.
- Czyszczenie starej pamięci podręcznej podczas instalowania aktualizacji.
- Ponowne uruchomienie `panel.html` po zakończeniu aktualizacji.

### Zmieniono

- Numer wersji w stopce jest ustawiany automatycznie na podstawie wersji bieżącego wydania.
- Ujednolicono numer wersji w `panel.html`, `version.json`, `manifest.webmanifest` i `sw.js`.
- Rozbudowano strategię pamięci podręcznej PWA:
  - `version.json` jest pobierany bez cache,
  - nawigacja korzysta z podejścia network-first,
  - zasoby statyczne korzystają z cache aplikacji,
  - stara pamięć podręczna jest usuwana po aktywacji nowej wersji.

### Naprawiono

- Problem pozostawania starego numeru wersji w stopce po aktualizacji aplikacji.
- Problem braku wyraźnego komunikatu informującego użytkownika o dostępnej aktualizacji.

---

## [3.3.0] - 2026-08-19

### Dodano

- Graficzny wykres Pareto strat w sekcji analizy strat.
- Słupki przedstawiające liczbę minut strat według przyczyny.
- Niebieską linię przedstawiającą skumulowany udział procentowy strat.
- Linię referencyjną **80%**, ułatwiającą identyfikację głównych przyczyn strat.
- Etykiety wartości w minutach nad słupkami.
- Obsługę maksymalnie ośmiu głównych kategorii strat na wykresie.
- Responsywne przewijanie wykresu na ekranach mobilnych.

### Zmieniono

- Zachowano ranking Pareto pod wykresem jako szczegółową listę przyczyn.
- Zaktualizowano manifest i cache aplikacji do wersji 3.3.0.

---

## [3.2.2] - 2026-08-19

### Naprawiono

- Poprawiono położenie informacji o wersji aplikacji.
- Na górze pozostawiono wyłącznie przyciski nawigacyjne:
  - **Panel OEE**,
  - **Przerwy i 6S**,
  - **Konfiguracja progów**.
- Na samym dole panelu pozostawiono wyłącznie belkę z numerem wersji.
- Usunięto numer wersji z górnej belki nawigacyjnej.

---

## [3.2.1] - 2026-08-19

### Zmieniono

- Usunięto przycisk **„Statystyki 6S”** z belki aplikacji.
- Usunięto skrót **„Statystyki 6S”** z `manifest.webmanifest`.
- Podbito cache Service Workera, aby urządzenia pobrały poprawiony układ.

### Naprawiono

- Cofnięto błędne przeniesienie całej nawigacji na dół aplikacji.
- Rozdzielono nawigację aplikacji od informacji o numerze wersji.

---

## [3.2.0] - 2026-08-19

### Zmieniono

- Aplikację przebudowano na podstawie kolejnej aktualizacji głównego pliku `panel.html`.
- Zachowano zmiany wprowadzone ręcznie w pliku bazowym.
- Utrzymano:
  - Dashboard „Dzisiaj”,
  - powiadomienia,
  - analizę Pareto,
  - nawigację PWA,
  - mechanizm instalacji aplikacji.
- Numer wersji podniesiono do **3.2.0**.
- Zaktualizowano `manifest.webmanifest`.
- Zmieniono nazwę cache w `sw.js`, aby wymusić pobranie aktualizacji.

---

## [3.1.0] - 2026-08-19

### Zmieniono

- Aplikację przebudowano na podstawie zaktualizowanego głównego pliku `panel.html`.
- Zachowano nowe elementy dodane w pliku bazowym, w tym ówczesny skrót **„Statystyki 6S”**.
- Rozszerzono górną nawigację o dostęp do funkcji panelu i konfiguracji.
- Dodano widoczny numer **Wersja 3.1**.
- Zaktualizowano manifest do wersji **3.1.0**.
- Podbito pamięć podręczną Service Workera.

### Techniczne

- Paczka aktualizacyjna nie nadpisywała osobnych plików `config_panel.html` i `statystyki_6S.html`, aby zachować ręcznie rozwijane moduły.

---

## [3.0.1] - 2026-08-18

### Dodano

- Metadane wersji aplikacji w `manifest.webmanifest`.
- Pola `version` i `app_version`.
- Widoczny znacznik **„Wersja 3.0”** w aplikacji.
- Meta-tag `app-version` w plikach panelu.

### Zmieniono

- Nazwę aplikacji ustawiono na **Panel OEE Lakiernia 3.0**.
- Skróconą nazwę ustawiono na **OEE 3.0**.
- Podbito nazwę cache Service Workera.

### Uwagi

- Systemowy numer WebAPK pokazywany przez Android może być nadawany przez Chrome i nie zawsze jest zgodny z numerem funkcjonalnym PWA.

---

## [3.0.0] - 2026-08-18

### Najważniejsze wydanie

Wersja 3.0 przekształciła aplikację z formularza raportowego w bardziej rozbudowany panel do codziennego zarządzania lakiernią.

### Dodano: Dashboard „Dzisiaj”

- OEE bieżącego miesiąca.
- Liczbę zapisanych dni raportowych.
- Aktualną zmianę.
- Odliczanie do końca zmiany.
- Najbliższą przerwę.
- Odliczanie do najbliższej przerwy.
- Informację o dzisiejszych zadaniach 6S.
- Liczbę dni bez awarii lub usterki.
- Kontrolę obecności raportu za poprzedni dzień roboczy.
- Prognozę OEE miesiąca na podstawie bieżącej średniej.
- Najlepszy i najgorszy dzień miesiąca.
- Automatyczne odświeżanie danych Dashboardu.

### Dodano: Powiadomienia

- Przycisk włączający powiadomienia w aplikacji.
- Powiadomienie 5 minut przed przerwą.
- Powiadomienie w momencie rozpoczęcia przerwy.
- Osobne komunikaty dla czasu 6S.
- Powiadomienie o brakującym raporcie OEE.
- Powiadomienie o niewykonanych zadaniach 6S.
- Ochronę przed wielokrotnym wyświetlaniem tego samego komunikatu.
- Lokalne komunikaty typu toast w interfejsie.

### Dodano: Pareto strat

- Osobną sekcję **„Pareto strat miesiąca”**.
- Ranking głównych przyczyn strat.
- Zestawienie minut strat według kategorii.
- Procent skumulowany dla kolejnych kategorii.
- Wskazanie liczby przyczyn odpowiadających za około 80% strat.
- Sumę strat miesiąca.
- Największą pojedynczą stratę.
- Analizę awarii i usterek.
- Analizę czasu zmiany koloru i czyszczenia.
- Analizę czasu serwisu.
- Średni czas przezbrojenia, jeżeli dostępna jest liczba przezbrojeń.

### Dodano: PWA

- Instalację aplikacji na telefonie i komputerze.
- `manifest.webmanifest`.
- Service Workera `sw.js`.
- Stronę `offline.html`.
- Ikony 192 × 192 i 512 × 512.
- Przycisk instalacji aplikacji.
- Obsługę trybu standalone.
- Podstawową obsługę pracy offline dla zasobów aplikacji.

### Zmieniono

- Rozbudowano nawigację między panelem a konfiguracją przerw i 6S.
- Dopasowano Dashboard i Pareto do ekranów komputerowych, telefonów i ekranów TV.
- Zachowano integrację z Supabase oraz istniejącą historię raportów.

### Świadomie pominięto

- Zdjęcia z wykonania zadań 6S.
- Logowanie PIN i role użytkowników.

---

## [2.1.0] - 2026-08-18

### Dodano

- Poprawioną konstrukcję PWA dla istniejącego panelu OEE.
- Nawigację pomiędzy `panel.html` i `config_panel.html`.
- Obsługę instalacji PWA.
- Poprawki wyglądu na urządzeniach mobilnych.
- Ukrywanie przycisku instalacji w trybie standalone.

### Zmieniono

- Uporządkowano manifest, Service Workera i pliki instalacyjne.
- Przygotowano finalną paczkę wdrożeniową wersji 2.1.

---

## [2.0.0] - 2026-08-18

### Dodano

- Pierwszą wersję aplikacji PWA działającą na bazie panelu OEE.
- Manifest aplikacji.
- Service Workera.
- Stronę informującą o braku połączenia.
- Ikony aplikacji.
- Możliwość instalacji panelu jako aplikacji z poziomu przeglądarki.

### Zmieniono

- Panel internetowy zaczął działać jako aplikacja uruchamiana z ikony telefonu lub komputera.

---

## [1.0.0] - wersja bazowa

### Dodano

- Miesięczny panel OEE dla lakierni.
- Kalendarz miesięczny z kolorowaniem dni według wyniku OEE.
- Formularz raportu dobowego.
- Obsługę liczników sterownika Gema:
  - czas automatyki,
  - licznik metrów,
  - licznik sztuk.
- Obliczanie dostępności, wydajności, jakości i OEE.
- Obsługę awarii i usterek.
- Obsługę czasu czyszczenia kabiny.
- Obsługę liczby przezbrojeń.
- Obsługę czasu serwisu.
- Obsługę liczby poprawek.
- Kategorie głównych przyczyn przestoju.
- Notatki do raportów.
- Walidację ciągłości liczników.
- Walidację logiczną raportu przed zapisem.
- Zapis raportów w Supabase.
- Lokalną kopię danych w `localStorage`.
- Edycję i usuwanie zapisanych raportów.
- Eksport kopii danych do pliku.
- Import kopii danych z pliku.
- Kopiowanie danych do Excela.
- Tygodniowe i miesięczne podsumowania OEE.
- Rekord OEE miesiąca.
- Wykres miesięcznych wyników OEE.
- Szczegółową tabelę raportów.
- Konfigurację progów OEE i kolorów wykresu.
- Tryb jasny i ciemny.
- Tryb kierownika i uproszczony tryb operatora.
- Konfigurację przerw oraz zadań 6S.
- Obsługę własnych dźwięków przerw.
- Historię zadań 6S.
- Zarządzanie operatorami i zadaniami 6S.
- Podstawowe karty analityczne dla kierownika:
  - alerty,
  - trend miesiąc do miesiąca,
  - dni bez awarii,
  - największa strata.

---

## Zasady przygotowania kolejnych wydań

Przy każdym nowym wydaniu należy:

1. Zwiększyć numer wersji zgodnie z zakresem zmiany.
2. Ustawić ten sam numer w:
   - `version.json`,
   - `manifest.webmanifest`,
   - stałej `CURRENT_APP_VERSION` w `panel.html`,
   - stałej `APP_VERSION` w `sw.js`,
   - stopce aplikacji generowanej przez skrypt.
3. Zaktualizować datę kompilacji i datę wydania w `version.json`.
4. Dodać wpis na początku tego pliku.
5. Zmienić nazwę cache Service Workera przez zmianę `APP_VERSION`.
6. Sprawdzić składnię JavaScript.
7. Sprawdzić poprawność paczki ZIP.
8. Wgrać komplet plików do jednego katalogu GitHub Pages.
9. Pozostawić `version.json` bez długotrwałego cache, aby urządzenia mogły wykryć aktualizację.

### Przykład następnego wydania

Dla nowej funkcji po wersji `3.5.0` zalecany numer to `3.6.0`. Dla samej poprawki błędu w wersji `3.5.0` zalecany numer to `3.5.1`.
