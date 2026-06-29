package com.example.egzamin.repository;

import com.example.egzamin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
    ============================================================
    PLIK TECHNICZNY STAŁY: UserRepository.java
    ============================================================

    Repozytorium użytkowników.

    W tej wersji NIE używamy emaila,
    bo encja User nie ma pola email.

    Dlatego nie może tutaj być metod:

    findByEmail(...)
    existsByEmail(...)

    Gdyby one zostały, Spring przy starcie aplikacji szukałby pola email
    w klasie User i aplikacja by się wywaliła.
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
        Szuka użytkownika po loginie.

        Używane między innymi przez:
        - logowanie,
        - Spring Security,
        - ReservationService,
        - CustomUserDetailsService.
    */
    Optional<User> findByUsername(String username);

    /*
        Sprawdza, czy login już istnieje.

        Używane przy:
        - rejestracji,
        - DataInitializer,
        - tworzeniu kont admin/user.
    */
    boolean existsByUsername(String username);
}