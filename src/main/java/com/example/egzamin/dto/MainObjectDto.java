package com.example.egzamin.dto;

import com.example.egzamin.entity.AdditionalEntity;
import com.example.egzamin.entity.MainObject;

/*
    ============================================================
    PLIK ZMIENNY NR 4: MainObjectDto.java
    ============================================================

    DTO oznacza Data Transfer Object.

    Po ludzku:
    Jest to klasa pomocnicza do przenoszenia danych.

    MainObjectDto NIE jest tabelą w bazie danych.
    Dlatego NIE ma tutaj adnotacji:

    @Entity
    @Table

    Encją bazodanową jest MainObject.java.

    Ten plik przydaje się, gdy:

    - odbieramy dane z formularza HTML,
    - wysyłamy dane do widoku Thymeleaf,
    - obsługujemy REST API,
    - nie chcemy bezpośrednio pracować na encji MainObject.

    Przykład:

    Użytkownik/admin wypełnia formularz:
    - nazwa,
    - opis,
    - lokalizacja,
    - pojemność,
    - cena,
    - aktywność,
    - kategoria.

    Te dane mogą najpierw trafić do MainObjectDto,
    a dopiero potem serwis zrobi z nich MainObject.
*/
public class MainObjectDto {

    /*
        ============================================================
        ID
        ============================================================

        ID jest potrzebne np. przy edycji.

        Gdy dodajemy nowy obiekt:
        id najczęściej jest null.

        Gdy edytujemy istniejący obiekt:
        id mówi, który rekord w bazie trzeba zaktualizować.
    */
    private Long id;

    /*
        ============================================================
        NAME
        ============================================================

        Nazwa głównego obiektu.

        Zależnie od tematu:

        - sala: nazwa sali,
        - auto: model auta,
        - hotel: nazwa pokoju,
        - biblioteka: tytuł książki,
        - sprzęt: nazwa sprzętu.

        Nazwy pola w Javie NIE zmieniamy.
        Zmieniamy co najwyżej napisy w HTML-u.
    */
    private String name;

    /*
        ============================================================
        DESCRIPTION
        ============================================================

        Opis obiektu.

        Przykład:
        "Sala komputerowa z projektorem"
        "Samochód osobowy z klimatyzacją"
        "Pokój z balkonem"
    */
    private String description;

    /*
        ============================================================
        LOCATION
        ============================================================

        Lokalizacja / miejsce / adres / półka / parking.

        Zależnie od tematu:

        - sala: budynek i piętro,
        - auto: miejsce odbioru,
        - pokój: piętro,
        - książka: regał,
        - sprzęt: magazyn.
    */
    private String location;

    /*
        ============================================================
        CAPACITY
        ============================================================

        Pojemność / liczba miejsc / liczba osób / liczba sztuk.

        Przykład:
        - sala ma 30 miejsc,
        - auto ma 5 miejsc,
        - pokój jest dla 2 osób,
        - sprzętu są 3 sztuki.
    */
    private Integer capacity;

    /*
        ============================================================
        PRICE
        ============================================================

        Cena / koszt.

        Zależnie od tematu:

        - cena za godzinę,
        - cena za dobę,
        - cena wypożyczenia,
        - kaucja,
        - koszt usługi.
    */
    private Double price;

    /*
        ============================================================
        ACTIVE
        ============================================================

        Czy obiekt jest aktywny.

        true  -> obiekt dostępny,
        false -> obiekt wyłączony.

        Przykład:
        Sala może być w remoncie,
        auto może być uszkodzone,
        pokój może być niedostępny.
    */
    private boolean active = true;

    /*
        ============================================================
        ADDITIONAL ENTITY ID
        ============================================================

        To jest ID kategorii / typu / grupy.

        Nie trzymamy tutaj całego AdditionalEntity,
        tylko jego ID.

        Dlaczego?

        Bo w formularzu HTML najczęściej wysyłamy tylko wartość typu:

        <option value="1">Sala komputerowa</option>

        Czyli formularz wysyła np. additionalEntityId = 1.

        Potem serwis po tym ID znajduje AdditionalEntity w bazie.
    */
    private Long additionalEntityId;

    /*
        ============================================================
        ADDITIONAL ENTITY NAME
        ============================================================

        To pole jest pomocnicze do wyświetlania.

        Przykład:
        W tabeli admina chcemy pokazać:

        Sala 101 | Sala komputerowa

        Wtedy additionalEntityName pozwala łatwo pokazać nazwę kategorii.

        To pole nie musi być wysyłane z formularza.
        Ono jest głównie do widoku / REST-a.
    */
    private String additionalEntityName;

    /*
        ============================================================
        KONSTRUKTOR PUSTY
        ============================================================

        Spring i Thymeleaf potrzebują pustego konstruktora.

        Dzięki temu mogą utworzyć pusty obiekt DTO
        i wypełnić go danymi z formularza.
    */
    public MainObjectDto() {
    }

    /*
        ============================================================
        KONSTRUKTOR POMOCNICZY
        ============================================================

        Przydatny np. w testach albo kiedy chcemy szybko ręcznie
        stworzyć DTO w kodzie.

        Nie zawsze będzie używany, ale nie przeszkadza.
    */
    public MainObjectDto(Long id,
                         String name,
                         String description,
                         String location,
                         Integer capacity,
                         Double price,
                         boolean active,
                         Long additionalEntityId,
                         String additionalEntityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.price = price;
        this.active = active;
        this.additionalEntityId = additionalEntityId;
        this.additionalEntityName = additionalEntityName;
    }

    /*
        ============================================================
        METODA fromEntity()
        ============================================================

        Ta metoda zamienia encję MainObject na DTO.

        Czyli:

        MainObject -> MainObjectDto

        Przydaje się, gdy pobieramy dane z bazy,
        ale do widoku albo API chcemy oddać DTO.

        Przykład:
        Z bazy pobieramy salę jako MainObject,
        a do formularza edycji przekazujemy MainObjectDto.
    */
    public static MainObjectDto fromEntity(MainObject mainObject) {
        /*
            Jeżeli z jakiegoś powodu przekazano null,
            to zwracamy null.

            Dzięki temu unikamy błędu NullPointerException.
        */
        if (mainObject == null) {
            return null;
        }

        /*
            Tworzymy pusty obiekt DTO,
            a potem przepisujemy do niego dane z encji.
        */
        MainObjectDto dto = new MainObjectDto();

        dto.setId(mainObject.getId());
        dto.setName(mainObject.getName());
        dto.setDescription(mainObject.getDescription());
        dto.setLocation(mainObject.getLocation());
        dto.setCapacity(mainObject.getCapacity());
        dto.setPrice(mainObject.getPrice());
        dto.setActive(mainObject.isActive());

        /*
            AdditionalEntity może być puste.

            Przykład:
            Możemy mieć obiekt bez kategorii.

            Dlatego najpierw sprawdzamy:
            czy mainObject.getAdditionalEntity() != null
        */
        if (mainObject.getAdditionalEntity() != null) {
            dto.setAdditionalEntityId(mainObject.getAdditionalEntity().getId());
            dto.setAdditionalEntityName(mainObject.getAdditionalEntity().getName());
        }

        return dto;
    }

    /*
        ============================================================
        METODA toEntity()
        ============================================================

        Ta metoda zamienia DTO na encję MainObject.

        Czyli:

        MainObjectDto -> MainObject

        Używamy jej np. wtedy, gdy admin wysłał formularz dodawania
        albo edycji obiektu.

        Do tej metody przekazujemy AdditionalEntity,
        bo samo DTO ma tylko additionalEntityId.

        Serwis musi najpierw znaleźć kategorię w bazie,
        a potem tutaj ją przekazać.
    */
    public MainObject toEntity(AdditionalEntity additionalEntity) {
        MainObject mainObject = new MainObject();

        mainObject.setId(this.id);
        mainObject.setName(this.name);
        mainObject.setDescription(this.description);
        mainObject.setLocation(this.location);
        mainObject.setCapacity(this.capacity);
        mainObject.setPrice(this.price);
        mainObject.setActive(this.active);
        mainObject.setAdditionalEntity(additionalEntity);

        return mainObject;
    }

    /*
        ============================================================
        METODA updateEntity()
        ============================================================

        Ta metoda aktualizuje istniejącą encję danymi z DTO.

        Przydaje się przy edycji.

        Dlaczego nie zawsze tworzymy nowy MainObject?

        Bo przy edycji bezpieczniej jest:
        - pobrać istniejący obiekt z bazy,
        - zmienić jego pola,
        - zapisać go ponownie.

        Dzięki temu nie gubimy relacji i innych danych.
    */
    public void updateEntity(MainObject mainObject, AdditionalEntity additionalEntity) {
        if (mainObject == null) {
            return;
        }

        mainObject.setName(this.name);
        mainObject.setDescription(this.description);
        mainObject.setLocation(this.location);
        mainObject.setCapacity(this.capacity);
        mainObject.setPrice(this.price);
        mainObject.setActive(this.active);
        mainObject.setAdditionalEntity(additionalEntity);
    }

    /*
        ============================================================
        GETTERY I SETTERY
        ============================================================

        Są potrzebne, żeby:

        - formularz HTML mógł wpisywać dane do DTO,
        - Thymeleaf mógł odczytać dane,
        - kontroler mógł przekazać dane do serwisu,
        - REST API mogło zwrócić dane jako JSON.
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        /*
            ID ustawiamy np. przy edycji istniejącego obiektu.
        */
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        /*
            Ustawia nazwę obiektu.

            Przykład:
            dto.setName("Sala 101");
        */
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        /*
            Ustawia opis obiektu.
        */
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        /*
            Ustawia lokalizację obiektu.
        */
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        /*
            Ustawia pojemność / liczbę miejsc / liczbę sztuk.
        */
        this.capacity = capacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        /*
            Ustawia cenę / koszt.
        */
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    /*
        Ten getter jest dodatkowo dodany dla bezpieczeństwa.

        Czasami różne mechanizmy JavaBean / Thymeleaf / JSON
        lubią boolean jako isActive(),
        a czasami jako getActive().

        Dzięki temu mamy oba warianty.
    */
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        /*
            Ustawia, czy obiekt jest aktywny.
        */
        this.active = active;
    }

    public Long getAdditionalEntityId() {
        return additionalEntityId;
    }

    public void setAdditionalEntityId(Long additionalEntityId) {
        /*
            Ustawia ID kategorii / typu / grupy.

            Przykład:
            additionalEntityId = 1
        */
        this.additionalEntityId = additionalEntityId;
    }

    public String getAdditionalEntityName() {
        return additionalEntityName;
    }

    public void setAdditionalEntityName(String additionalEntityName) {
        /*
            Ustawia nazwę kategorii do wyświetlania.
        */
        this.additionalEntityName = additionalEntityName;
    }
}