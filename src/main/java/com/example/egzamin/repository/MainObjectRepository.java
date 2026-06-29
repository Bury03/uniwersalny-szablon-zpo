package com.example.egzamin.repository;

import com.example.egzamin.entity.MainObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
    ============================================================
    REPOZYTORIUM: MainObjectRepository
    ============================================================

    To repozytorium odpowiada za komunikację z tabelą main_objects.

    MainObject to główny obiekt aplikacji.

    W zależności od tematu egzaminu może oznaczać:

    - salę,
    - samochód,
    - pokój,
    - sprzęt,
    - książkę,
    - gabinet,
    - usługę.

    Dzięki JpaRepository automatycznie dostajemy metody:

    findAll()      -> pobiera wszystkie obiekty
    findById(id)   -> szuka obiektu po ID
    save(object)   -> zapisuje lub aktualizuje obiekt
    delete(object) -> usuwa obiekt
    count()        -> liczy obiekty
    existsById(id) -> sprawdza, czy obiekt istnieje

    Dlatego MainObjectService może używać tych metod bez pisania SQL.
*/
@Repository
public interface MainObjectRepository extends JpaRepository<MainObject, Long> {
}