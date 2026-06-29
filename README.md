# Uniwersalny szablon egzaminacyjny Spring Boot

Ten projekt jest bazą do szybkiego robienia aplikacji zaliczeniowych typu:

- wypożyczalnia samochodów,
- system paczkomatów,
- rezerwacja biletów w kinie,
- umawianie wizyt do weterynarza,
- hotel,
- rezerwacja sal,
- wypożyczalnia sprzętu.

Projekt jest celowo zrobiony na ogólnych nazwach klas. Dzięki temu przy nowym temacie nie trzeba zmieniać całej struktury aplikacji.

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

Zmieniasz znaczenie klas przez dane, napisy i algorytm.

Przykład dla wypożyczalni samochodów:

| Klasa w kodzie | Znaczenie w temacie |
|---|---|
| `MainObject` | samochód |
| `AdditionalEntity` | typ auta |
| `Reservation` | rezerwacja auta |

Przykład dla weterynarza:

| Klasa w kodzie | Znaczenie w temacie |
|---|---|
| `MainObject` | lekarz weterynarii |
| `AdditionalEntity` | specjalizacja |
| `Reservation` | wizyta |

## Co zwykle zmieniasz przy nowym temacie

1. `config/DataInitializer.java`  
   Tu wpisujesz dane startowe: auta, lekarzy, skrytki, seanse, sale.

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
  "note": "Odbiór rano",
  "extraData": "Dane zależne od tematu",
  "discount": true
}
```

Ten endpoint wymaga zalogowania. Najprościej testować go przez Swagger po zalogowaniu w aplikacji albo przez narzędzie typu Postman.

## Co pokazać prowadzącemu

Najbezpieczniejsza kolejność:

1. Start aplikacji.
2. Logowanie jako `user`.
3. Lista dostępnych obiektów.
4. Formularz rezerwacji / operacji.
5. Zapis operacji.
6. Widok „moje rezerwacje”, gdzie user widzi tylko swoje dane.
7. Logowanie jako `admin`.
8. Admin widzi wszystkie rezerwacje i obiekty.
9. Swagger i endpoint REST.
10. Test jednostkowy algorytmu.

## Gdzie są najważniejsze komentarze

| Plik | Po co go czytać |
|---|---|
| `MainObject.java` | wyjaśnia główny obiekt aplikacji |
| `AdditionalEntity.java` | wyjaśnia typ/kategorię |
| `Reservation.java` | wyjaśnia operację użytkownika |
| `ReservationForm.java` | wyjaśnia dane z formularza |
| `ReservationService.java` | wyjaśnia logikę zapisu i bezpieczeństwo danych |
| `AlgorithmService.java` | wyjaśnia algorytmy pod różne tematy |
| `SecurityConfig.java` | wyjaśnia role i dostęp |
