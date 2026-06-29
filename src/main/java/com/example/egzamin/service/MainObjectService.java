package com.example.egzamin.service;

import com.example.egzamin.dto.MainObjectDto;
import com.example.egzamin.entity.AdditionalEntity;
import com.example.egzamin.entity.MainObject;
import com.example.egzamin.exception.NotFoundException;
import com.example.egzamin.repository.AdditionalEntityRepository;
import com.example.egzamin.repository.MainObjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
    ============================================================
    PLIK ZMIENNY NR 6: MainObjectService.java
    ============================================================

    To jest serwis dla głównego obiektu aplikacji.

    MainObject może oznaczać różne rzeczy zależnie od tematu egzaminu:

    - salę,
    - samochód,
    - pokój hotelowy,
    - sprzęt,
    - książkę,
    - gabinet,
    - usługę,
    - boisko,
    - stolik,
    - inny obiekt, który można wyświetlać albo rezerwować.

    WAŻNE:
    Nazwy klasy MainObjectService NIE zmieniamy.

    Ten serwis jest warstwą pośrednią między:

    - kontrolerem,
    - repozytorium,
    - bazą danych.

    Czyli kontroler NIE powinien bezpośrednio grzebać w repozytorium.
    Kontroler woła serwis, a serwis dopiero używa repozytorium.

    Przykład przepływu:

    Użytkownik/admin klika "Dodaj salę"
        ↓
    Controller odbiera formularz
        ↓
    MainObjectService sprawdza i zapisuje dane
        ↓
    MainObjectRepository zapisuje do bazy
*/

@Service
@Transactional
public class MainObjectService {

    /*
        ============================================================
        REPOZYTORIUM GŁÓWNEGO OBIEKTU
        ============================================================

        MainObjectRepository służy do komunikacji z tabelą main_objects.

        Dzięki niemu możemy robić rzeczy typu:

        - findAll()      -> pobierz wszystkie obiekty,
        - findById(id)   -> znajdź obiekt po ID,
        - save(object)   -> zapisz obiekt,
        - delete(object) -> usuń obiekt.

        Nie piszemy tutaj SQL ręcznie,
        bo Spring Data JPA robi to za nas.
    */
    private final MainObjectRepository mainObjectRepository;

    /*
        ============================================================
        REPOZYTORIUM ENCJI DODATKOWEJ
        ============================================================

        AdditionalEntityRepository jest potrzebne,
        bo MainObject ma przypisaną kategorię / typ / grupę.

        Przykład:

        Sala 101 ma kategorię "Sala komputerowa".

        W formularzu dostajemy tylko additionalEntityId,
        czyli np. 1.

        Serwis musi po tym ID znaleźć prawdziwy obiekt AdditionalEntity
        i dopiero przypisać go do MainObject.
    */
    private final AdditionalEntityRepository additionalEntityRepository;

    /*
        ============================================================
        KONSTRUKTOR
        ============================================================

        Spring automatycznie wstrzykuje tutaj repozytoria.

        Nie musimy pisać @Autowired nad konstruktorem,
        bo jeżeli klasa ma jeden konstruktor, Spring sam wie,
        że ma go użyć.

        To jest obecnie zalecany sposób.
    */
    public MainObjectService(MainObjectRepository mainObjectRepository,
                             AdditionalEntityRepository additionalEntityRepository) {
        this.mainObjectRepository = mainObjectRepository;
        this.additionalEntityRepository = additionalEntityRepository;
    }

    /*
        ============================================================
        POBIERANIE WSZYSTKICH OBIEKTÓW
        ============================================================

        Ta metoda pobiera wszystkie obiekty z bazy.

        Czyli np.:

        - wszystkie sale,
        - wszystkie samochody,
        - wszystkie pokoje,
        - cały sprzęt.

        Używa się jej najczęściej w panelu administratora,
        bo admin powinien widzieć też obiekty nieaktywne.
    */
    @Transactional(readOnly = true)
    public List<MainObject> findAll() {
        return mainObjectRepository.findAll();
    }

    /*
        ============================================================
        ALIAS: getAll()
        ============================================================

        To jest metoda zapasowa o innej nazwie.

        Dlaczego ją zostawiam?

        Bo różne kontrolery mogą używać różnych nazw:

        - findAll()
        - getAll()
        - getAllMainObjects()

        Żeby nie rozwalać projektu, dajemy kilka prostych metod,
        które robią to samo.

        Dzięki temu jak gdzieś w kontrolerze jest getAll(),
        to projekt nadal się kompiluje.
    */
    @Transactional(readOnly = true)
    public List<MainObject> getAll() {
        return findAll();
    }

    /*
        ============================================================
        ALIAS: getAllMainObjects()
        ============================================================

        Kolejna pomocnicza nazwa dla tej samej operacji.

        Zwraca wszystkie MainObject.
    */
    @Transactional(readOnly = true)
    public List<MainObject> getAllMainObjects() {
        return findAll();
    }

    /*
        ============================================================
        POBIERANIE AKTYWNYCH OBIEKTÓW
        ============================================================

        Ta metoda zwraca tylko obiekty aktywne.

        Czyli takie, które mają:

        active = true

        Przykład:

        - sala jest dostępna,
        - samochód jest dostępny,
        - pokój można rezerwować,
        - sprzęt nie jest wyłączony.

        Celowo nie używam tutaj metody typu findByActiveTrue()
        z repozytorium, bo wtedy musielibyśmy mieć ją dopisaną
        w MainObjectRepository.

        A tak metoda działa nawet z prostym JpaRepository.
    */
    @Transactional(readOnly = true)
    public List<MainObject> findAllActive() {
        return mainObjectRepository.findAll()
                .stream()
                .filter(MainObject::isActive)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        ALIAS: getActiveMainObjects()
        ============================================================

        Metoda pomocnicza dla kontrolerów.

        Robi dokładnie to samo co findAllActive().
    */
    @Transactional(readOnly = true)
    public List<MainObject> getActiveMainObjects() {
        return findAllActive();
    }

    /*
        ============================================================
        ALIAS: getAllActive()
        ============================================================

        Kolejna nazwa pomocnicza.

        Dzięki temu jeżeli któryś kontroler/test używa getAllActive(),
        to nadal będzie działało.
    */
    @Transactional(readOnly = true)
    public List<MainObject> getAllActive() {
        return findAllActive();
    }

    /*
        ============================================================
        POBIERANIE WSZYSTKICH OBIEKTÓW JAKO DTO
        ============================================================

        Ta metoda pobiera wszystkie MainObject,
        a potem zamienia je na MainObjectDto.

        Encja MainObject jest dobra do pracy z bazą danych.
        DTO jest wygodne do widoku / formularza / REST API.

        Czyli:

        MainObject -> MainObjectDto
    */
    @Transactional(readOnly = true)
    public List<MainObjectDto> findAllDto() {
        return mainObjectRepository.findAll()
                .stream()
                .map(MainObjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        POBIERANIE AKTYWNYCH OBIEKTÓW JAKO DTO
        ============================================================

        To samo co wyżej, ale tylko dla aktywnych obiektów.
    */
    @Transactional(readOnly = true)
    public List<MainObjectDto> findAllActiveDto() {
        return findAllActive()
                .stream()
                .map(MainObjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        SZUKANIE OBIEKTU PO ID
        ============================================================

        Ta metoda szuka MainObject po ID.

        Przykład:

        id = 1

        Serwis szuka w bazie obiektu o ID = 1.

        Jeżeli znajdzie:
        - zwraca obiekt.

        Jeżeli nie znajdzie:
        - rzuca NotFoundException.

        Dzięki temu nie dostajemy potem dziwnych błędów null,
        tylko od razu wiadomo, że obiektu nie znaleziono.
    */
    @Transactional(readOnly = true)
    public MainObject findById(Long id) {
        return mainObjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono obiektu o ID: " + id));
    }

    /*
        ============================================================
        ALIAS: getById()
        ============================================================

        Ta metoda robi to samo co findById().

        Jest po to, żeby pasowała do różnych nazw używanych
        w kontrolerach albo testach.
    */
    @Transactional(readOnly = true)
    public MainObject getById(Long id) {
        return findById(id);
    }

    /*
        ============================================================
        ALIAS: getMainObjectById()
        ============================================================

        Kolejna pomocnicza nazwa dla pobierania po ID.
    */
    @Transactional(readOnly = true)
    public MainObject getMainObjectById(Long id) {
        return findById(id);
    }

    /*
        ============================================================
        POBIERANIE OBIEKTU PO ID JAKO DTO
        ============================================================

        Ta metoda:
        1. znajduje MainObject po ID,
        2. zamienia go na MainObjectDto.

        Przydaje się np. przy formularzu edycji.
    */
    @Transactional(readOnly = true)
    public MainObjectDto getDtoById(Long id) {
        MainObject mainObject = findById(id);
        return MainObjectDto.fromEntity(mainObject);
    }

    /*
        ============================================================
        SPRAWDZENIE, CZY OBIEKT ISTNIEJE
        ============================================================

        Prosta metoda pomocnicza.

        Zwraca:

        true  -> obiekt istnieje,
        false -> obiekt nie istnieje.
    */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }

        return mainObjectRepository.existsById(id);
    }

    /*
        ============================================================
        LICZBA WSZYSTKICH OBIEKTÓW
        ============================================================

        Przydatne np. do panelu admina,
        gdzie możesz chcieć pokazać statystyki.

        Przykład:
        "Liczba sal: 5"
    */
    @Transactional(readOnly = true)
    public long count() {
        return mainObjectRepository.count();
    }

    /*
        ============================================================
        ZAPIS ENCJI
        ============================================================

        Ta metoda zapisuje gotowy obiekt MainObject.

        Może działać zarówno jako:

        - dodawanie nowego obiektu,
        - aktualizacja istniejącego obiektu.

        Spring Data JPA działa tak:

        Jeśli id == null:
            tworzy nowy rekord.

        Jeśli id != null:
            aktualizuje istniejący rekord.
    */
    public MainObject save(MainObject mainObject) {
        return mainObjectRepository.save(mainObject);
    }

    /*
        ============================================================
        TWORZENIE OBIEKTU Z DTO
        ============================================================

        Ta metoda jest używana, gdy dane przychodzą z formularza
        albo z REST API jako MainObjectDto.

        Krok po kroku:

        1. Sprawdzamy kategorię / typ po additionalEntityId.
        2. Zamieniamy DTO na encję MainObject.
        3. Zapisujemy MainObject do bazy.
    */
    public MainObject create(MainObjectDto dto) {
        AdditionalEntity additionalEntity = findAdditionalEntityOrNull(dto.getAdditionalEntityId());

        MainObject mainObject = dto.toEntity(additionalEntity);

        /*
            Przy tworzeniu nowego obiektu ustawiamy ID na null.

            Dlaczego?

            Bo baza danych sama ma nadać nowe ID.
            Gdyby przypadkiem w DTO było jakieś stare ID,
            moglibyśmy nadpisać istniejący rekord.
        */
        mainObject.setId(null);

        return mainObjectRepository.save(mainObject);
    }

    /*
        ============================================================
        ALIAS: saveDto()
        ============================================================

        Metoda pomocnicza.

        Jeżeli DTO ma puste ID, tworzymy nowy obiekt.
        Jeżeli DTO ma ID, aktualizujemy istniejący obiekt.

        Dzięki temu ta metoda działa uniwersalnie.
    */
    public MainObject saveDto(MainObjectDto dto) {
        if (dto.getId() == null) {
            return create(dto);
        }

        return update(dto.getId(), dto);
    }

    /*
        ============================================================
        AKTUALIZACJA OBIEKTU
        ============================================================

        Ta metoda aktualizuje istniejący MainObject.

        Krok po kroku:

        1. Szukamy istniejącego obiektu w bazie.
        2. Szukamy kategorii / typu po additionalEntityId.
        3. Przepisujemy dane z DTO do znalezionego obiektu.
        4. Zapisujemy obiekt.

        Nie tworzymy całkiem nowego obiektu,
        bo przy edycji lepiej aktualizować istniejący rekord.
    */
    public MainObject update(Long id, MainObjectDto dto) {
        MainObject existingMainObject = findById(id);

        AdditionalEntity additionalEntity = findAdditionalEntityOrNull(dto.getAdditionalEntityId());

        dto.updateEntity(existingMainObject, additionalEntity);

        return mainObjectRepository.save(existingMainObject);
    }

    /*
        ============================================================
        ALIAS: updateMainObject()
        ============================================================

        Ta metoda robi to samo co update().

        Jest zostawiona, żeby pasowała do różnych kontrolerów.
    */
    public MainObject updateMainObject(Long id, MainObjectDto dto) {
        return update(id, dto);
    }

    /*
        ============================================================
        USUWANIE OBIEKTU
        ============================================================

        Ta metoda usuwa MainObject z bazy danych.

        Najpierw sprawdzamy, czy obiekt istnieje.

        Jeżeli nie istnieje:
        - rzucamy NotFoundException.

        Jeżeli istnieje:
        - usuwamy go.

        UWAGA:
        W prawdziwych systemach często lepiej obiekt dezaktywować,
        a nie usuwać fizycznie z bazy.

        Dlatego niżej jest też metoda deactivate().
    */
    public void delete(Long id) {
        MainObject mainObject = findById(id);
        mainObjectRepository.delete(mainObject);
    }

    /*
        ============================================================
        ALIAS: deleteById()
        ============================================================

        Pomocnicza nazwa dla usuwania po ID.
    */
    public void deleteById(Long id) {
        delete(id);
    }

    /*
        ============================================================
        ALIAS: deleteMainObject()
        ============================================================

        Kolejna pomocnicza nazwa dla usuwania.
    */
    public void deleteMainObject(Long id) {
        delete(id);
    }

    /*
        ============================================================
        DEZAKTYWACJA OBIEKTU
        ============================================================

        To jest bezpieczniejsza wersja niż usuwanie.

        Zamiast usuwać rekord z bazy, ustawiamy:

        active = false

        Przykłady:

        - sala jest w remoncie,
        - auto jest uszkodzone,
        - pokój jest niedostępny,
        - sprzęt został wycofany.

        Obiekt nadal istnieje w bazie,
        ale można go ukryć przed użytkownikiem.
    */
    public MainObject deactivate(Long id) {
        MainObject mainObject = findById(id);
        mainObject.setActive(false);
        return mainObjectRepository.save(mainObject);
    }

    /*
        ============================================================
        ALIAS: deactivateMainObject()
        ============================================================

        Pomocnicza nazwa dla dezaktywacji.
    */
    public MainObject deactivateMainObject(Long id) {
        return deactivate(id);
    }

    /*
        ============================================================
        AKTYWACJA OBIEKTU
        ============================================================

        Odwrotność dezaktywacji.

        Ustawiamy:

        active = true

        Przykład:
        Sala była w remoncie, ale wróciła do użytku.
    */
    public MainObject activate(Long id) {
        MainObject mainObject = findById(id);
        mainObject.setActive(true);
        return mainObjectRepository.save(mainObject);
    }

    /*
        ============================================================
        ALIAS: activateMainObject()
        ============================================================

        Pomocnicza nazwa dla aktywacji.
    */
    public MainObject activateMainObject(Long id) {
        return activate(id);
    }

    /*
        ============================================================
        WYSZUKIWANIE KATEGORII / TYPU PO ID
        ============================================================

        To jest prywatna metoda pomocnicza.

        private oznacza:
        - używamy jej tylko wewnątrz tego serwisu,
        - kontroler jej nie widzi,
        - inne klasy jej nie wołają.

        Zadanie:

        - jeśli additionalEntityId jest null, zwracamy null,
        - jeśli ID istnieje, zwracamy znalezioną AdditionalEntity,
        - jeśli ID nie istnieje, rzucamy NotFoundException.

        Dlaczego zwracamy null, gdy ID jest null?

        Bo czasem obiekt może nie mieć kategorii.
        Np. na egzaminie nie zawsze trzeba wymuszać kategorię.
    */
    private AdditionalEntity findAdditionalEntityOrNull(Long additionalEntityId) {
        if (additionalEntityId == null) {
            return null;
        }

        return additionalEntityRepository.findById(additionalEntityId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono kategorii o ID: " + additionalEntityId));
    }
}