# Uniwersalny szablon egzaminacyjny Spring Boot

Ten projekt jest bazą do szybkiego robienia prostych aplikacji zaliczeniowych w Spring Boot.

Szablon nie jest przygotowany pod jeden konkretny temat. Przykłady typu wypożyczalnia samochodów, paczkomaty, kino albo weterynarz służą tylko do pokazania, jak można przerobić ten sam szkielet na różne zadania. Na egzaminie temat może być inny, ale zwykle da się go sprowadzić do podobnego schematu.

## Ogólny schemat aplikacji

Najczęściej temat da się opisać tak:

1. Użytkownik wybiera jakiś zasób.
2. Użytkownik wykonuje operację na tym zasobie.
3. System zapisuje operację w bazie danych.
4. Backend sprawdza warunek albo wykonuje algorytm.
5. Użytkownik widzi tylko swoje dane.
6. Administrator widzi wszystko.
7. Jest przynajmniej jeden endpoint REST.
8. Jest test jednostkowy najważniejszego algorytmu.

## Loginy startowe

| Rola | Login | Hasło |
|---|---|---|
| Administrator | `admin` | `admin123` |
| Użytkownik | `user` | `user123` |

## Co jest w środku

| Obszar punktacji | Gdzie jest w projekcie |
|---|---|
| Baza danych | `entity`, `repository`, H2, `DataInitializer` |
| UI | `templates`, `static/css/style.css` |
| Algorytm | `service/AlgorithmService.java` |
| Security | `config/SecurityConfig.java`, `User`, `Role` |
| REST API | `rest/OperationRestController.java`, `rest/MainRestController.java` |
| Swagger | `/swagger-ui/index.html` po uruchomieniu aplikacji |
| Testy | `src/test/java/.../AlgorithmServiceTest.java` |

## Najważniejsza zasada

Nie zmieniaj od razu nazw klas `MainObject`, `AdditionalEntity`, `Reservation`. One są celowo ogólne.

| Klasa w kodzie | Znaczenie ogólne |
|---|---|
| `MainObject` | główny zasób systemu, np. auto, pokój, lekarz, miejsce, sprzęt |
| `AdditionalEntity` | typ, kategoria, grupa, lokalizacja albo właściciel zasobu |
| `Reservation` | operacja użytkownika, np. rezerwacja, zgłoszenie, wypożyczenie, zamówienie |
| `AlgorithmService` | miejsce na logikę punktowaną w zadaniu |
| `OperationRestController` | uniwersalny endpoint REST do pokazania w Swaggerze |

Dzięki temu przy nowym temacie nie trzeba przebudowywać całego projektu. Najpierw zmienia się dane, napisy i algorytm.

## Co zwykle zmieniasz przy nowym temacie

1. `config/DataInitializer.java`  
   Tu wpisujesz dane startowe pasujące do tematu.

2. `service/AlgorithmService.java`  
   Tu zostawiasz albo przerabiasz algorytm pod temat.

3. `templates/...`  
   Tu zmieniasz napisy widoczne dla użytkownika.

4. `src/main/resources/messages.properties`  
   Tu zapisujesz mapowanie nazw, żeby nie pomylić znaczeń.

5. `src/test/java/com/example/egzamin/service/AlgorithmServiceTest.java`  
   Tu zostawiasz test pasujący do aktualnego tematu.

## Jak uruchomić

W katalogu projektu:

```bash
./mvnw spring-boot:run
```

Na Windowsie:

```bash
mvnw.cmd spring-boot:run
```

Adresy po uruchomieniu:

| Funkcja | Adres |
|---|---|
| Strona główna | `http://localhost:8080/` |
| Logowanie | `http://localhost:8080/login` |
| Panel usera | `http://localhost:8080/user/panel` |
| Panel admina | `http://localhost:8080/admin/panel` |
| H2 Console | `http://localhost:8080/h2-console` |
| Swagger | `http://localhost:8080/swagger-ui/index.html` |

## Jak sprawdzić REST API

Endpoint uniwersalny:

```http
POST /api/operations
```

Przykładowy JSON:

```json
{
  "mainObjectId": 1,
  "startDateTime": "2026-07-01T10:00",
  "endDateTime": "2026-07-03T10:00",
  "note": "Przykładowa notatka",
  "extraData": "Dane zależne od tematu",
  "discount": true
}
```

Ten endpoint wymaga zalogowania. Najprościej testować go przez Swagger po zalogowaniu w aplikacji albo przez narzędzie typu Postman.

## Co pokazać prowadzącemu

Najbezpieczniejsza kolejność:

1. Start aplikacji.
2. Logowanie jako `user`.
3. Lista dostępnych zasobów.
4. Formularz operacji.
5. Zapis operacji do bazy.
6. Widok danych użytkownika, gdzie user widzi tylko swoje rekordy.
7. Logowanie jako `admin`.
8. Admin widzi wszystkie rekordy i zarządza zasobami.
9. Swagger i endpoint REST.
10. Test jednostkowy algorytmu.

## Dokumentacja

Najpierw przeczytaj:

```text
docs/00_START_TUTAJ.md
```

Potem przy przerabianiu tematu używaj:

```text
docs/01_INSTRUKCJA_PRZERABIANIA.md
docs/02_SCIAGA_MAPOWANIA_I_ALGORYTMOW.md
docs/03_CHECKLISTA_PUNKTOW.md
docs/04_CO_ZMIENIAC_A_CZEGO_NIE.md
docs/05_PROBA_NA_LOSOWYM_TEMACIE.md
```
