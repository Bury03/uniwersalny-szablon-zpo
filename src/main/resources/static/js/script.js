/*
    ============================================================
    GŁÓWNY PLIK JS: script.js
    ============================================================

    Ten plik zawiera proste skrypty pomocnicze.

    Nie jest wymagany do działania backendu,
    ale może poprawić wygodę korzystania ze strony.

    Działa uniwersalnie dla każdego tematu projektu.
*/

document.addEventListener("DOMContentLoaded", function () {
    /*
        ============================================================
        AUTOMATYCZNE UKRYWANIE KOMUNIKATÓW
        ============================================================

        Jeśli na stronie są komunikaty sukcesu lub błędu,
        można je ukryć po kilku sekundach.

        Nie jest to konieczne, ale wygląda lepiej.
    */

    const messages = document.querySelectorAll(".success, .error");

    messages.forEach(function (message) {
        setTimeout(function () {
            message.style.opacity = "0";
            message.style.transition = "opacity 0.5s ease";
        }, 4000);
    });

    /*
        ============================================================
        PROSTA WALIDACJA DAT REZERWACJI
        ============================================================

        Jeśli na stronie jest formularz rezerwacji,
        sprawdzamy po stronie przeglądarki,
        czy koniec jest po początku.

        UWAGA:
        To jest tylko pomoc dla użytkownika.

        Prawdziwa walidacja i tak jest w ReservationService,
        bo walidacji backendowej nie wolno pomijać.
    */

    const startInput = document.getElementById("startDateTime");
    const endInput = document.getElementById("endDateTime");

    if (startInput && endInput) {
        endInput.addEventListener("change", function () {
            const startValue = startInput.value;
            const endValue = endInput.value;

            if (startValue && endValue && endValue <= startValue) {
                alert("Koniec rezerwacji musi być później niż początek.");
                endInput.value = "";
            }
        });
    }
});