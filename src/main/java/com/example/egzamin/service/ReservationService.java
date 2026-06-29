package com.example.egzamin.service;

import com.example.egzamin.dto.ReservationForm;
import com.example.egzamin.entity.MainObject;
import com.example.egzamin.entity.Reservation;
import com.example.egzamin.entity.User;
import com.example.egzamin.exception.AppAccessDeniedException;
import com.example.egzamin.exception.NotFoundException;
import com.example.egzamin.exception.ReservationConflictException;
import com.example.egzamin.repository.MainObjectRepository;
import com.example.egzamin.repository.ReservationRepository;
import com.example.egzamin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
    ============================================================
    PLIK ZMIENNY NR 7: ReservationService.java
    ============================================================

    To jest serwis odpowiedzialny za rezerwacje.

    ReservationService jest jednym z najważniejszych plików w projekcie,
    bo tutaj znajduje się logika, która pilnuje, czy rezerwacja jest poprawna.

    Ten serwis odpowiada za:

    - tworzenie rezerwacji,
    - sprawdzanie, czy obiekt istnieje,
    - sprawdzanie, czy użytkownik istnieje,
    - sprawdzanie, czy termin jest poprawny,
    - sprawdzanie, czy rezerwacje się nie nakładają,
    - anulowanie rezerwacji,
    - pobieranie rezerwacji użytkownika,
    - pobieranie wszystkich rezerwacji dla administratora.

    Najważniejsza zasada:

    Controller ma tylko odebrać żądanie z przeglądarki.
    ReservationService ma wykonać logikę.

    Czyli nie robimy skomplikowanych ifów w kontrolerze.
    Robimy je tutaj.
*/

@Service
@Transactional
public class ReservationService {

    /*
        ============================================================
        REPOZYTORIUM REZERWACJI
        ============================================================

        ReservationRepository służy do komunikacji z tabelą reservations.

        Dzięki niemu możemy:

        - pobrać wszystkie rezerwacje,
        - znaleźć rezerwację po ID,
        - zapisać rezerwację,
        - usunąć rezerwację,
        - policzyć rezerwacje.

        Jeżeli ReservationRepository rozszerza JpaRepository,
        to te metody dostajemy automatycznie.
    */
    private final ReservationRepository reservationRepository;

    /*
        ============================================================
        REPOZYTORIUM GŁÓWNEGO OBIEKTU
        ============================================================

        MainObjectRepository jest potrzebne, bo rezerwacja dotyczy
        jakiegoś obiektu głównego.

        Przykład:

        - sala,
        - samochód,
        - pokój,
        - sprzęt,
        - książka,
        - gabinet.

        Formularz rezerwacji daje nam tylko mainObjectId,
        więc serwis musi znaleźć prawdziwy MainObject w bazie.
    */
    private final MainObjectRepository mainObjectRepository;

    /*
        ============================================================
        REPOZYTORIUM UŻYTKOWNIKA
        ============================================================

        UserRepository jest potrzebne, żeby znaleźć użytkownika,
        który tworzy rezerwację.

        W praktyce często bierzemy użytkownika po username/emailu
        z aktualnego logowania Spring Security.
    */
    private final UserRepository userRepository;

    /*
        ============================================================
        SERWIS ALGORYTMÓW
        ============================================================

        AlgorithmService trzyma logikę, za którą najczęściej są punkty:
        - liczenie ceny,
        - wykrywanie kolizji,
        - wybór skrytki,
        - proponowanie miejsc.

        ReservationService korzysta z niego przy tworzeniu rezerwacji,
        żeby wynik algorytmu zapisać w bazie.
    */
    private final AlgorithmService algorithmService;

    /*
        ============================================================
        KONSTRUKTOR
        ============================================================

        Spring automatycznie wstrzykuje tutaj repozytoria.

        Nie trzeba pisać @Autowired,
        bo jeżeli klasa ma jeden konstruktor,
        Spring sam go użyje.
    */
    public ReservationService(ReservationRepository reservationRepository,
                              MainObjectRepository mainObjectRepository,
                              UserRepository userRepository,
                              AlgorithmService algorithmService) {
        this.reservationRepository = reservationRepository;
        this.mainObjectRepository = mainObjectRepository;
        this.userRepository = userRepository;
        this.algorithmService = algorithmService;
    }

    /*
        ============================================================
        POBIERANIE WSZYSTKICH REZERWACJI
        ============================================================

        Ta metoda jest głównie dla administratora.

        Admin powinien móc zobaczyć wszystkie rezerwacje w systemie,
        niezależnie od tego, kto je zrobił.

        Przykład:
        Panel admina -> Wszystkie rezerwacje.
    */
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    /*
        ============================================================
        ALIAS: getAll()
        ============================================================

        To jest metoda pomocnicza.

        Robi dokładnie to samo co findAll(),
        ale ma inną nazwę.

        Dzięki temu jeśli jakiś kontroler/test używa getAll(),
        projekt dalej się kompiluje.
    */
    @Transactional(readOnly = true)
    public List<Reservation> getAll() {
        return findAll();
    }

    /*
        ============================================================
        ALIAS: getAllReservations()
        ============================================================

        Kolejna pomocnicza nazwa dla pobierania wszystkich rezerwacji.
    */
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return findAll();
    }

    /*
        ============================================================
        POBIERANIE REZERWACJI PO ID
        ============================================================

        Szukamy konkretnej rezerwacji po ID.

        Jeżeli istnieje:
        - zwracamy ją.

        Jeżeli nie istnieje:
        - rzucamy NotFoundException.

        Dzięki temu później nie operujemy na nullu.
    */
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono rezerwacji o ID: " + id));
    }

    /*
        ============================================================
        ALIAS: getById()
        ============================================================

        Ta metoda robi to samo co findById().
    */
    @Transactional(readOnly = true)
    public Reservation getById(Long id) {
        return findById(id);
    }

    /*
        ============================================================
        ALIAS: getReservationById()
        ============================================================

        Kolejna nazwa pomocnicza dla kontrolerów/testów.
    */
    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return findById(id);
    }

    /*
        ============================================================
        LICZBA REZERWACJI
        ============================================================

        Przydatne np. w panelu administratora.

        Można wyświetlić:
        "Liczba rezerwacji: 12"
    */
    @Transactional(readOnly = true)
    public long count() {
        return reservationRepository.count();
    }

    /*
        ============================================================
        POBIERANIE REZERWACJI UŻYTKOWNIKA
        ============================================================

        Ta metoda zwraca rezerwacje konkretnego użytkownika.

        Nie zakładamy tutaj, że ReservationRepository ma metodę
        findByUser(...), bo wtedy musielibyśmy ją dopisywać.

        Zamiast tego bierzemy wszystkie rezerwacje i filtrujemy w Javie.

        To jest prostsze i bezpieczniejsze na egzamin,
        bo działa z podstawowym JpaRepository.
    */
    @Transactional(readOnly = true)
    public List<Reservation> findByUser(User user) {
        return reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.getUser() != null)
                .filter(reservation -> reservation.getUser().getId() != null)
                .filter(reservation -> user != null && user.getId() != null)
                .filter(reservation -> reservation.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        ALIAS: getReservationsByUser()
        ============================================================

        Robi to samo co findByUser().
    */
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByUser(User user) {
        return findByUser(user);
    }

    /*
        ============================================================
        POBIERANIE REZERWACJI UŻYTKOWNIKA PO USERNAME
        ============================================================

        Ta metoda przydaje się w UserPanelController.

        Zalogowany użytkownik ma username.
        Po username szukamy encji User,
        a potem pobieramy jego rezerwacje.

        Przykład:
        username = "jan"
        -> szukamy User jan
        -> pobieramy jego rezerwacje
    */
    @Transactional(readOnly = true)
    public List<Reservation> findByUsername(String username) {
        User user = findUserByUsername(username);
        return findByUser(user);
    }

    /*
        ============================================================
        ALIAS: getReservationsForUser()
        ============================================================

        Pomocnicza nazwa dla pobierania rezerwacji zalogowanego użytkownika.
    */
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsForUser(String username) {
        return findByUsername(username);
    }

    /*
        ============================================================
        ALIAS: getUserReservations()
        ============================================================

        Kolejna pomocnicza nazwa.
    */
    @Transactional(readOnly = true)
    public List<Reservation> getUserReservations(String username) {
        return findByUsername(username);
    }

    /*
        ============================================================
        POBIERANIE AKTYWNYCH REZERWACJI
        ============================================================

        Aktywna rezerwacja to taka, która ma status ACTIVE.

        Używamy metody isActive() z encji Reservation.
    */
    @Transactional(readOnly = true)
    public List<Reservation> findAllActive() {
        return reservationRepository.findAll()
                .stream()
                .filter(Reservation::isActive)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        POBIERANIE AKTYWNYCH REZERWACJI UŻYTKOWNIKA
        ============================================================

        To samo co findByUser(),
        ale odfiltrowujemy anulowane/zakończone.
    */
    @Transactional(readOnly = true)
    public List<Reservation> findActiveByUser(User user) {
        return findByUser(user)
                .stream()
                .filter(Reservation::isActive)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        TWORZENIE REZERWACJI Z FORMULARZA I USERNAME
        ============================================================

        To jest najważniejsza metoda dla zwykłego użytkownika.

        Dostajemy:

        - ReservationForm, czyli dane z formularza,
        - username, czyli aktualnie zalogowanego użytkownika.

        Krok po kroku:

        1. Sprawdzamy, czy formularz ma podstawowe dane.
        2. Szukamy użytkownika po username.
        3. Szukamy MainObject po mainObjectId.
        4. Sprawdzamy, czy obiekt jest aktywny.
        5. Sprawdzamy, czy daty są poprawne.
        6. Sprawdzamy, czy nie ma konfliktu terminów.
        7. Tworzymy Reservation.
        8. Zapisujemy Reservation w bazie.
    */
    public Reservation createReservation(ReservationForm form, String username) {
        validateFormBasicData(form);

        User user = findUserByUsername(username);

        MainObject mainObject = findMainObjectById(form.getMainObjectId());

        validateMainObjectIsActive(mainObject);

        validateDateRange(form.getStartDateTime(), form.getEndDateTime());

        validateNoConflict(
                mainObject,
                form.getStartDateTime(),
                form.getEndDateTime(),
                null
        );

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setMainObject(mainObject);
        reservation.setStartDateTime(form.getStartDateTime());
        reservation.setEndDateTime(form.getEndDateTime());
        reservation.setNote(form.getNote());
        reservation.setExtraData(form.getExtraData());
        reservation.setStatus("ACTIVE");
        reservation.setTotalPrice(algorithmService.calculatePrice(
                mainObject.getPrice(),
                form.getStartDateTime(),
                form.getEndDateTime(),
                form.isDiscount()
        ));

        return reservationRepository.save(reservation);
    }

    /*
        ============================================================
        ALIAS: create()
        ============================================================

        Krótsza nazwa dla createReservation().
    */
    public Reservation create(ReservationForm form, String username) {
        return createReservation(form, username);
    }

    /*
        ============================================================
        ALIAS: reserve()
        ============================================================

        Jeszcze jedna nazwa pomocnicza.

        Przydaje się, jeśli kontroler był napisany bardziej "po ludzku",
        np. reservationService.reserve(...).
    */
    public Reservation reserve(ReservationForm form, String username) {
        return createReservation(form, username);
    }

    /*
        ============================================================
        TWORZENIE REZERWACJI Z GOTOWYCH OBIEKTÓW
        ============================================================

        Ta metoda przydaje się w testach albo wewnętrznie.

        Dostajemy już znalezionego użytkownika i obiekt.
    */
    public Reservation createReservation(User user,
                                         MainObject mainObject,
                                         LocalDateTime startDateTime,
                                         LocalDateTime endDateTime,
                                         String note) {
        if (user == null) {
            throw new NotFoundException("Nie znaleziono użytkownika");
        }

        if (mainObject == null) {
            throw new NotFoundException("Nie znaleziono obiektu do rezerwacji");
        }

        validateMainObjectIsActive(mainObject);
        validateDateRange(startDateTime, endDateTime);
        validateNoConflict(mainObject, startDateTime, endDateTime, null);

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setMainObject(mainObject);
        reservation.setStartDateTime(startDateTime);
        reservation.setEndDateTime(endDateTime);
        reservation.setNote(note);
        reservation.setStatus("ACTIVE");
        reservation.setTotalPrice(algorithmService.calculatePrice(
                mainObject.getPrice(),
                startDateTime,
                endDateTime,
                false
        ));

        return reservationRepository.save(reservation);
    }

    /*
        ============================================================
        AKTUALIZACJA REZERWACJI
        ============================================================

        Ta metoda pozwala zaktualizować istniejącą rezerwację.

        Najczęściej na egzaminie wystarczy dodawanie i anulowanie,
        ale edycję zostawiamy, bo nie przeszkadza.

        excludeReservationId oznacza:
        przy sprawdzaniu konfliktów ignorujemy aktualnie edytowaną rezerwację.

        Dlaczego?

        Bo gdy edytujesz rezerwację o ID = 5,
        to ona sama nie może być traktowana jako konflikt ze sobą.
    */
    public Reservation updateReservation(Long reservationId, ReservationForm form, String username) {
        validateFormBasicData(form);

        Reservation existingReservation = findById(reservationId);

        /*
            Zabezpieczenie:
            zwykły użytkownik nie powinien edytować cudzej rezerwacji.

            Jeżeli username nie pasuje do właściciela rezerwacji,
            rzucamy AppAccessDeniedException.
        */
        validateReservationOwner(existingReservation, username);

        MainObject mainObject = findMainObjectById(form.getMainObjectId());

        validateMainObjectIsActive(mainObject);
        validateDateRange(form.getStartDateTime(), form.getEndDateTime());

        validateNoConflict(
                mainObject,
                form.getStartDateTime(),
                form.getEndDateTime(),
                reservationId
        );

        existingReservation.setMainObject(mainObject);
        existingReservation.setStartDateTime(form.getStartDateTime());
        existingReservation.setEndDateTime(form.getEndDateTime());
        existingReservation.setNote(form.getNote());
        existingReservation.setExtraData(form.getExtraData());
        existingReservation.setTotalPrice(algorithmService.calculatePrice(
                mainObject.getPrice(),
                form.getStartDateTime(),
                form.getEndDateTime(),
                form.isDiscount()
        ));

        return reservationRepository.save(existingReservation);
    }

    /*
        ============================================================
        ANULOWANIE REZERWACJI PRZEZ UŻYTKOWNIKA
        ============================================================

        Użytkownik może anulować swoją rezerwację.

        Nie usuwamy rekordu z bazy.
        Ustawiamy status:

        CANCELLED

        Dzięki temu admin nadal może zobaczyć historię.
    */
    public Reservation cancelReservation(Long reservationId, String username) {
        Reservation reservation = findById(reservationId);

        validateReservationOwner(reservation, username);

        reservation.setStatus("CANCELLED");

        return reservationRepository.save(reservation);
    }

    /*
        ============================================================
        ALIAS: cancel()
        ============================================================

        Krótka nazwa dla anulowania rezerwacji.
    */
    public Reservation cancel(Long reservationId, String username) {
        return cancelReservation(reservationId, username);
    }

    /*
        ============================================================
        ANULOWANIE REZERWACJI PRZEZ ADMINA
        ============================================================

        Admin może anulować dowolną rezerwację.

        Dlatego tutaj NIE sprawdzamy właściciela.
    */
    public Reservation cancelReservationByAdmin(Long reservationId) {
        Reservation reservation = findById(reservationId);

        reservation.setStatus("CANCELLED");

        return reservationRepository.save(reservation);
    }

    /*
        ============================================================
        ZAKOŃCZENIE REZERWACJI
        ============================================================

        Ustawia status FINISHED.

        Przydatne, jeśli chcesz oznaczyć, że rezerwacja się zakończyła.
    */
    public Reservation finishReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);

        reservation.setStatus("FINISHED");

        return reservationRepository.save(reservation);
    }

    /*
        ============================================================
        USUWANIE REZERWACJI
        ============================================================

        Fizycznie usuwa rezerwację z bazy.

        Na egzaminie bezpieczniej używać anulowania,
        ale zostawiamy delete, bo może być potrzebne w adminie albo testach.
    */
    public void delete(Long reservationId) {
        Reservation reservation = findById(reservationId);
        reservationRepository.delete(reservation);
    }

    /*
        ============================================================
        ALIAS: deleteById()
        ============================================================

        Pomocnicza nazwa dla usuwania.
    */
    public void deleteById(Long reservationId) {
        delete(reservationId);
    }

    /*
        ============================================================
        SPRAWDZENIE KONFLIKTU TERMINÓW
        ============================================================

        Ta metoda mówi, czy dla danego obiektu istnieje już aktywna
        rezerwacja nachodząca na podany czas.

        Przykład konfliktu:

        Istniejąca rezerwacja:
        10:00 - 12:00

        Nowa rezerwacja:
        11:00 - 13:00

        To jest konflikt, bo godziny się nakładają.

        Przykład bez konfliktu:

        Istniejąca:
        10:00 - 12:00

        Nowa:
        12:00 - 14:00

        To NIE jest konflikt,
        bo jedna kończy się dokładnie wtedy, gdy druga się zaczyna.

        Warunek nakładania przedziałów:

        nowyStart < istniejącyEnd
        oraz
        nowyEnd > istniejącyStart
    */
    @Transactional(readOnly = true)
    public boolean hasConflict(MainObject mainObject,
                               LocalDateTime startDateTime,
                               LocalDateTime endDateTime) {
        return hasConflict(mainObject, startDateTime, endDateTime, null);
    }

    /*
        ============================================================
        SPRAWDZENIE KONFLIKTU Z MOŻLIWOŚCIĄ IGNOROWANIA ID
        ============================================================

        ignoreReservationId jest potrzebne przy edycji.

        Jeżeli edytujemy rezerwację ID = 5,
        to ignorujemy rezerwację ID = 5 podczas szukania konfliktów.
    */
    @Transactional(readOnly = true)
    public boolean hasConflict(MainObject mainObject,
                               LocalDateTime startDateTime,
                               LocalDateTime endDateTime,
                               Long ignoreReservationId) {
        if (mainObject == null || mainObject.getId() == null) {
            return false;
        }

        if (startDateTime == null || endDateTime == null) {
            return false;
        }

        return reservationRepository.findAll()
                .stream()

                /*
                    Interesują nas tylko aktywne rezerwacje.
                    Anulowane i zakończone nie blokują terminu.
                */
                .filter(Reservation::isActive)

                /*
                    Sprawdzamy tylko rezerwacje tego samego obiektu.
                    Rezerwacja sali 101 nie koliduje z salą 102.
                */
                .filter(reservation -> reservation.getMainObject() != null)
                .filter(reservation -> reservation.getMainObject().getId() != null)
                .filter(reservation -> reservation.getMainObject().getId().equals(mainObject.getId()))

                /*
                    Przy edycji ignorujemy aktualną rezerwację.
                */
                .filter(reservation -> ignoreReservationId == null
                        || reservation.getId() == null
                        || !reservation.getId().equals(ignoreReservationId))

                /*
                    Sprawdzamy, czy przedziały czasu się nakładają.
                */
                .anyMatch(reservation ->
                        startDateTime.isBefore(reservation.getEndDateTime())
                                && endDateTime.isAfter(reservation.getStartDateTime())
                );
    }

    /*
        ============================================================
        SPRAWDZENIE DOSTĘPNOŚCI OBIEKTU
        ============================================================

        Zwraca true, jeśli nie ma konfliktu.

        Przydatne np. dla REST API albo testów.
    */
    @Transactional(readOnly = true)
    public boolean isAvailable(MainObject mainObject,
                               LocalDateTime startDateTime,
                               LocalDateTime endDateTime) {
        validateDateRange(startDateTime, endDateTime);
        return !hasConflict(mainObject, startDateTime, endDateTime);
    }

    /*
        ============================================================
        SPRAWDZENIE DOSTĘPNOŚCI PO ID OBIEKTU
        ============================================================

        Wersja wygodna, gdy mamy tylko mainObjectId.
    */
    @Transactional(readOnly = true)
    public boolean isAvailable(Long mainObjectId,
                               LocalDateTime startDateTime,
                               LocalDateTime endDateTime) {
        MainObject mainObject = findMainObjectById(mainObjectId);
        return isAvailable(mainObject, startDateTime, endDateTime);
    }

    /*
        ============================================================
        WALIDACJA PODSTAWOWYCH DANYCH FORMULARZA
        ============================================================

        Sprawdzamy, czy formularz nie jest pusty
        i czy wybrano obiekt do rezerwacji.
    */
    private void validateFormBasicData(ReservationForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Formularz rezerwacji nie może być pusty");
        }

        if (!form.hasMainObjectSelected()) {
            throw new IllegalArgumentException("Musisz wybrać obiekt do rezerwacji");
        }

        if (!form.hasStartAndEndDateTime()) {
            throw new IllegalArgumentException("Musisz podać początek i koniec rezerwacji");
        }
    }

    /*
        ============================================================
        WALIDACJA ZAKRESU DAT
        ============================================================

        Sprawdzamy:

        1. Czy daty nie są puste.
        2. Czy koniec jest po początku.

        Przykład poprawny:
        start = 10:00
        end   = 12:00

        Przykład błędny:
        start = 12:00
        end   = 10:00
    */
    private void validateDateRange(LocalDateTime startDateTime,
                                   LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            throw new IllegalArgumentException("Początek i koniec rezerwacji są wymagane");
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new IllegalArgumentException("Koniec rezerwacji musi być później niż początek");
        }
    }

    /*
        ============================================================
        WALIDACJA AKTYWNOŚCI OBIEKTU
        ============================================================

        Nie pozwalamy rezerwować obiektów nieaktywnych.

        Przykłady:

        - sala w remoncie,
        - auto uszkodzone,
        - pokój niedostępny,
        - sprzęt wyłączony.
    */
    private void validateMainObjectIsActive(MainObject mainObject) {
        if (mainObject == null) {
            throw new NotFoundException("Nie znaleziono obiektu do rezerwacji");
        }

        if (!mainObject.isActive()) {
            throw new IllegalArgumentException("Ten obiekt nie jest aktywny i nie może być rezerwowany");
        }
    }

    /*
        ============================================================
        WALIDACJA BRAKU KONFLIKTU
        ============================================================

        Jeśli istnieje już aktywna rezerwacja w tym terminie,
        rzucamy ReservationConflictException.

        Czyli blokujemy podwójną rezerwację tego samego obiektu.
    */
    private void validateNoConflict(MainObject mainObject,
                                    LocalDateTime startDateTime,
                                    LocalDateTime endDateTime,
                                    Long ignoreReservationId) {
        if (hasConflict(mainObject, startDateTime, endDateTime, ignoreReservationId)) {
            throw new ReservationConflictException("Wybrany termin jest już zajęty");
        }
    }

    /*
        ============================================================
        WALIDACJA WŁAŚCICIELA REZERWACJI
        ============================================================

        Zwykły użytkownik może anulować albo edytować tylko swoje rezerwacje.

        Przykład:

        Jan nie może anulować rezerwacji Anny.

        Admin ma osobne metody, które nie sprawdzają właściciela.
    */
    private void validateReservationOwner(Reservation reservation, String username) {
        if (reservation == null) {
            throw new NotFoundException("Nie znaleziono rezerwacji");
        }

        if (reservation.getUser() == null) {
            throw new AppAccessDeniedException("Rezerwacja nie ma przypisanego użytkownika");
        }

        if (username == null || username.isBlank()) {
            throw new AppAccessDeniedException("Brak zalogowanego użytkownika");
        }

        String reservationUsername = reservation.getUser().getUsername();

        if (reservationUsername == null || !reservationUsername.equals(username)) {
            throw new AppAccessDeniedException("Nie masz dostępu do tej rezerwacji");
        }
    }

    /*
        ============================================================
        SZUKANIE MAIN OBJECT PO ID
        ============================================================

        Formularz daje nam tylko ID obiektu.

        Tutaj znajdujemy prawdziwy MainObject w bazie.
    */
    private MainObject findMainObjectById(Long mainObjectId) {
        if (mainObjectId == null) {
            throw new NotFoundException("Nie podano ID obiektu");
        }

        return mainObjectRepository.findById(mainObjectId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono obiektu o ID: " + mainObjectId));
    }

    /*
        ============================================================
        SZUKANIE USER PO USERNAME
        ============================================================

        Tu może być jedna ważna rzecz zależna od Twojego UserRepository.

        Zakładam, że UserRepository ma metodę:

        findByUsername(String username)

        Jeżeli IntelliJ pokaże błąd właśnie tutaj,
        to trzeba będzie dopasować tę jedną linijkę do Twojego UserRepository.
    */
    private User findUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotFoundException("Nie znaleziono zalogowanego użytkownika");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono użytkownika: " + username));
    }
}