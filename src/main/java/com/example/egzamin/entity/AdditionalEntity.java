package com.example.egzamin.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/*
    ============================================================
    PLIK ZMIENNY NR 2: AdditionalEntity.java
    ============================================================

    Ta klasa reprezentuje DODATKOWĄ ENCJĘ w projekcie.

    Najprościej:
    AdditionalEntity = kategoria / typ / rodzaj / grupa.

    Przykłady zależnie od tematu:

    1. Rezerwacja sal:
       - AdditionalEntity = typ sali
       - np. "Sala komputerowa", "Sala wykładowa", "Laboratorium"

    2. Hotel:
       - AdditionalEntity = standard pokoju
       - np. "Standard", "Premium", "Apartament"

    3. Wypożyczalnia samochodów:
       - AdditionalEntity = typ auta
       - np. "Osobowy", "SUV", "Dostawczy"

    4. Biblioteka:
       - AdditionalEntity = kategoria książki
       - np. "Informatyka", "Matematyka", "Literatura"

    5. Sprzęt:
       - AdditionalEntity = rodzaj sprzętu
       - np. "Projektor", "Laptop", "Mikrofon"

    WAŻNE:
    Nazwy klasy "AdditionalEntity" NIE zmieniamy.
    Dzięki temu inne pliki dalej działają bez przerabiania projektu.
*/

@Entity
@Table(name = "additional_entities")
public class AdditionalEntity {

    /*
        ============================================================
        ID
        ============================================================

        Każda kategoria / typ / grupa ma swoje unikalne ID.

        Przykład:
        id = 1 -> "Sala komputerowa"
        id = 2 -> "Sala wykładowa"
        id = 3 -> "Laboratorium"

        Tego pola normalnie nie ruszamy.
        Baza danych sama nadaje ID.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        ============================================================
        NAME
        ============================================================

        Nazwa dodatkowej encji.

        Dla sal:
        - "Sala komputerowa"
        - "Sala wykładowa"

        Dla samochodów:
        - "Osobowy"
        - "SUV"
        - "Dostawczy"

        Dla hotelu:
        - "Standard"
        - "Premium"
        - "Apartament"

        To pole jest uniwersalne, dlatego nazywa się po prostu "name".
        W HTML-u można potem wyświetlić to jako "Typ", "Kategoria",
        "Rodzaj", "Standard" itd.
    */
    @Column(length = 100)
    private String name;

    /*
        ============================================================
        DESCRIPTION
        ============================================================

        Opis kategorii / typu / grupy.

        Przykłady:

        - "Sale przeznaczone do zajęć komputerowych"
        - "Samochody osobowe dla maksymalnie 5 osób"
        - "Pokoje o podwyższonym standardzie"
        - "Książki związane z informatyką"

        To pole nie jest obowiązkowe, ale jest przydatne,
        bo można je pokazać w panelu administratora.
    */
    @Column(length = 1000)
    private String description;

    /*
        ============================================================
        ACTIVE
        ============================================================

        Informacja, czy kategoria jest aktywna.

        true  -> kategoria aktywna,
        false -> kategoria wyłączona.

        Przykład:
        Możesz mieć kategorię "Sala laboratoryjna",
        ale jeśli już jej nie używasz, możesz ustawić active = false.

        Dzięki temu nie trzeba usuwać kategorii z bazy.
    */
    private boolean active = true;

    /*
        ============================================================
        RELACJA DO MainObject
        ============================================================

        Jedna AdditionalEntity może mieć wiele MainObject.

        Czyli:

        Jedna kategoria "Sala komputerowa" może mieć wiele sal:
        - Sala 101
        - Sala 102
        - Sala 203

        Jeden typ "SUV" może mieć wiele aut:
        - Toyota RAV4
        - BMW X5
        - Kia Sportage

        mappedBy = "additionalEntity" oznacza, że w klasie MainObject
        jest pole:

        private AdditionalEntity additionalEntity;

        Czyli MainObject jest właścicielem relacji,
        a tutaj mamy tylko listę wszystkich obiektów należących do tej kategorii.
    */
    @OneToMany(
            mappedBy = "additionalEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<MainObject> mainObjects = new ArrayList<>();

    /*
        ============================================================
        KONSTRUKTOR PUSTY
        ============================================================

        Hibernate / JPA wymaga pustego konstruktora.

        Bez niego Spring może mieć problem z tworzeniem obiektów
        pobieranych z bazy danych.
    */
    public AdditionalEntity() {
    }

    /*
        ============================================================
        KONSTRUKTOR POMOCNICZY NR 1
        ============================================================

        Ten konstruktor jest wygodny np. w DataInitializer.

        Pozwala szybko stworzyć kategorię z nazwą i opisem.

        active ustawiamy domyślnie na true,
        bo nowo dodana kategoria najczęściej ma być aktywna.
    */
    public AdditionalEntity(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    /*
        ============================================================
        KONSTRUKTOR POMOCNICZY NR 2
        ============================================================

        Ten konstruktor pozwala od razu ustawić,
        czy kategoria ma być aktywna.

        Przykład:
        new AdditionalEntity("Sala komputerowa", "Sale z komputerami", true);
    */
    public AdditionalEntity(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

    /*
        ============================================================
        GETTERY I SETTERY
        ============================================================

        Są potrzebne, bo:

        - Thymeleaf odczytuje dane przez gettery,
        - formularze zapisują dane przez settery,
        - Spring używa ich przy mapowaniu danych,
        - testy mogą łatwo ustawiać dane.
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        /*
            ID normalnie nadaje baza danych.
            Setter zostaje, bo może być potrzebny np. w testach.
        */
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        /*
            Ustawia nazwę kategorii / typu / grupy.

            Przykład:
            additionalEntity.setName("Sala komputerowa");
        */
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        /*
            Ustawia opis kategorii.

            Przykład:
            additionalEntity.setDescription("Sale z komputerami dla studentów");
        */
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        /*
            Ustawia, czy kategoria jest aktywna.

            true  -> aktywna,
            false -> wyłączona.
        */
        this.active = active;
    }

    public List<MainObject> getMainObjects() {
        return mainObjects;
    }

    public void setMainObjects(List<MainObject> mainObjects) {
        /*
            Ustawia listę obiektów głównych przypisanych do tej kategorii.

            Zazwyczaj nie musisz tego robić ręcznie,
            ale Spring/Hibernate i testy mogą tego potrzebować.
        */
        this.mainObjects = mainObjects;
    }
}