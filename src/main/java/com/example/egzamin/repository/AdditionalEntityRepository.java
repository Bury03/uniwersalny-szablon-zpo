package com.example.egzamin.repository;

import com.example.egzamin.entity.AdditionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
    ============================================================
    REPOZYTORIUM: AdditionalEntityRepository
    ============================================================

    To repozytorium odpowiada za komunikację z tabelą
    additional_entities, czyli z encją AdditionalEntity.

    AdditionalEntity oznacza u nas kategorię / typ / grupę.

    Przykłady:
    - typ sali,
    - typ samochodu,
    - standard pokoju,
    - kategoria sprzętu.

    JpaRepository daje nam gotowe metody:

    - findAll()
    - findById()
    - save()
    - delete()
    - count()
    - existsById()

    Dzięki temu nie musimy pisać SQL ręcznie.
*/
@Repository
public interface AdditionalEntityRepository extends JpaRepository<AdditionalEntity, Long> {
}