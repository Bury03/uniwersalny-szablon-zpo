package com.example.egzamin.config;

import com.example.egzamin.entity.AdditionalEntity;
import com.example.egzamin.entity.MainObject;
import com.example.egzamin.entity.Role;
import com.example.egzamin.entity.User;
import com.example.egzamin.repository.AdditionalEntityRepository;
import com.example.egzamin.repository.MainObjectRepository;
import com.example.egzamin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
/*
    ============================================================
    PLIK ZMIENNY
    ============================================================

    Ten plik uruchamia się automatycznie przy starcie aplikacji.
    Jego zadanie:

    - utworzyć konto administratora,
    - utworzyć konto zwykłego użytkownika,
    - utworzyć przykładowe kategorie / typy,
    - utworzyć przykładowe obiekty główne.

    NAJWAŻNIEJSZE PRZY NOWYM TEMACIE:

    To jest pierwszy plik, który zwykle zmieniasz.

    Przykłady mapowania:

    1. Wypożyczalnia samochodów:
       AdditionalEntity = typ auta, np. SUV, osobowy, dostawczy.
       MainObject = samochód, np. Toyota Corolla.

    2. Weterynarz:
       AdditionalEntity = specjalizacja, np. chirurgia, dermatologia.
       MainObject = lekarz, np. dr Kowalski.

    3. Paczkomat:
       AdditionalEntity = paczkomat albo gabaryt.
       MainObject = skrytka.

    4. Kino:
       AdditionalEntity = film.
       MainObject = seans.

    Nie musisz zmieniać nazwy klasy MainObject. Wystarczy zmienić dane,
    a w HTML-ach zmienić napisy widoczne dla użytkownika.

*/
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AdditionalEntityRepository additionalEntityRepository;
    private final MainObjectRepository mainObjectRepository;
    private final PasswordEncoder passwordEncoder;
    public DataInitializer(UserRepository userRepository,
                           AdditionalEntityRepository additionalEntityRepository,
                           MainObjectRepository mainObjectRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.additionalEntityRepository = additionalEntityRepository;
        this.mainObjectRepository = mainObjectRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args) {
        createUsers();
        createAdditionalEntitiesAndMainObjects();
    }
    private void createUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("user")) {
            User user = new User();

            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);

            userRepository.save(user);
        }
    }
    private void createAdditionalEntitiesAndMainObjects() {
        if (additionalEntityRepository.count() > 0 || mainObjectRepository.count() > 0) {
            return;
        }

        /*
            ========================================================
            AdditionalEntity oznacza kategorię / typ / grupę.
            Przykład: Dla sal będą to typy sal.
        */
        AdditionalEntity computerRoom = new AdditionalEntity(
                "Sala komputerowa",
                "Sala wyposażona w stanowiska komputerowe dla studentów."
        );

        AdditionalEntity lectureRoom = new AdditionalEntity(
                "Sala wykładowa",
                "Duża sala przeznaczona do wykładów i prezentacji."
        );

        AdditionalEntity laboratory = new AdditionalEntity(
                "Laboratorium",
                "Specjalistyczna sala do zajęć praktycznych."
        );

        /*
            Zapisujemy kategorie do bazy. Po zapisaniu dostaną swoje ID.
        */
        computerRoom = additionalEntityRepository.save(computerRoom);
        lectureRoom = additionalEntityRepository.save(lectureRoom);
        laboratory = additionalEntityRepository.save(laboratory);

        /*
            ========================================================
            GŁÓWNE OBIEKTY
            W tej wersji przykładowej traktujemy MainObject jako salę.
        */

        MainObject room101 = new MainObject(
                "Sala 101",
                "Sala komputerowa z 20 stanowiskami i projektorem.",
                "Budynek A, piętro 1",
                20,
                50.0,
                true,
                computerRoom
        );

        MainObject room202 = new MainObject(
                "Sala 202",
                "Duża sala wykładowa z rzutnikiem i nagłośnieniem.",
                "Budynek B, piętro 2",
                80,
                120.0,
                true,
                lectureRoom
        );

        MainObject room303 = new MainObject(
                "Laboratorium 303",
                "Laboratorium do zajęć praktycznych i projektowych.",
                "Budynek C, piętro 3",
                15,
                90.0,
                true,
                laboratory
        );

        MainObject room404 = new MainObject(
                "Sala 404",
                "Mniejsza sala ćwiczeniowa dla grup projektowych.",
                "Budynek A, piętro 4",
                12,
                40.0,
                true,
                lectureRoom
        );

        MainObject inactiveRoom = new MainObject(
                "Sala 505",
                "Sala chwilowo niedostępna z powodu remontu.",
                "Budynek D, piętro 5",
                30,
                70.0,
                false,
                lectureRoom
        );

        /*
            Zapisujemy obiekty do bazy.
        */
        mainObjectRepository.save(room101);
        mainObjectRepository.save(room202);
        mainObjectRepository.save(room303);
        mainObjectRepository.save(room404);
        mainObjectRepository.save(inactiveRoom);
    }
}