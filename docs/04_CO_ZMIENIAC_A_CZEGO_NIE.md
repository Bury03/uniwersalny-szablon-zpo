# Co zmieniać, a czego nie ruszać

Ten plik jest po to, żeby nie rozwalić działającego szablonu przy szybkim przerabianiu tematu.

## Zwykle nie ruszaj

```text
SecurityConfig.java
CustomUserDetailsService.java
UserService.java
UserRepository.java
HomeController.java
AuthController.java
login.html
register.html
style.css
script.js
```

Te pliki tworzą podstawę aplikacji.

Wyjątek: jeżeli temat wymaga dodatkowej roli, np. `COURIER`, `CASHIER`, `EMPLOYEE`, wtedy zmieniasz `Role.java` i `SecurityConfig.java`.

## Zmieniaj prawie zawsze

```text
DataInitializer.java
AlgorithmService.java
AlgorithmServiceTest.java
index.html
reservation-form.html
my-reservations.html
admin/main-objects.html
admin/all-reservations.html
messages.properties
```

To są pliki do dopasowania pod temat.

## Zmieniaj tylko gdy trzeba

```text
MainObject.java
Reservation.java
ReservationForm.java
OperationRequest.java
OperationResponse.java
OperationRestController.java
```

Te pliki zmieniasz wtedy, gdy temat wymaga dodatkowego pola albo innego wejścia w REST API.

Najpierw spróbuj użyć istniejących pól:

| Pole | Do czego można je wykorzystać |
|---|---|
| `name` | nazwa zasobu |
| `description` | opis |
| `location` | miejsce, adres, sala, sektor |
| `capacity` | liczba miejsc, rozmiar, limit, pojemność |
| `price` | cena bazowa |
| `note` | notatka użytkownika |
| `extraData` | dane specyficzne dla tematu |
| `totalPrice` | wynik liczenia ceny |

## Najbezpieczniejsza przeróbka

Zamiast zmieniać nazwy klas:

```text
MainObject -> Car
Reservation -> CarReservation
AdditionalEntity -> CarType
```

lepiej zostawić:

```text
MainObject
Reservation
AdditionalEntity
```

i zmienić tylko napisy, dane i algorytm.

To jest mniej eleganckie, ale dużo bezpieczniejsze przy szybkim robieniu projektu.

## Kiedy warto dodać nową klasę

Dodawaj nową encję dopiero wtedy, gdy bez niej temat naprawdę nie ma sensu.

Przykłady:

| Temat | Czy dodawać nową encję? |
|---|---|
| prosta rezerwacja | raczej nie |
| proste wypożyczenie | raczej nie |
| kino z dokładnymi miejscami | można dodać `Seat` |
| paczkomat z wieloma paczkomatami | można dodać `LockerStation` |
| system z pracownikami | można dodać rolę albo encję pracownika |

Na egzaminie lepiej dodać mniej klas, ale mieć działający przepływ.
