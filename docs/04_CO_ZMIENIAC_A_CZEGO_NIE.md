# Co zmieniać, a czego nie ruszać

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

## Najczęstsza bezpieczna przeróbka

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

i zmienić tylko napisy oraz dane.

To jest mniej eleganckie, ale dużo bezpieczniejsze przy szybkim robieniu projektu.
