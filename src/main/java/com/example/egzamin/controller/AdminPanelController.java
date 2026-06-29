package com.example.egzamin.controller;

import com.example.egzamin.entity.Reservation;
import com.example.egzamin.entity.User;
import com.example.egzamin.repository.UserRepository;
import com.example.egzamin.service.MainObjectService;
import com.example.egzamin.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
/*
    Ten kontroler obsługuje panel administratora.
    Administrator ma większe uprawnienia niż zwykły użytkownik.
    Admin może np.:
    - wejść do panelu admina,
    - zobaczyć statystyki,
    - zobaczyć wszystkich użytkowników,
    - zobaczyć wszystkie rezerwacje,
    - anulować dowolną rezerwację,
    - przejść do zarządzania głównymi obiektami.
*/
@Controller
@RequestMapping("/admin")
public class AdminPanelController {
    private final MainObjectService mainObjectService;
    private final ReservationService reservationService;
    private final UserRepository userRepository;
    public AdminPanelController(MainObjectService mainObjectService,
                                ReservationService reservationService,
                                UserRepository userRepository) {
        this.mainObjectService = mainObjectService;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }
    /*
        PANEL ADMINA
    */
    @GetMapping("/panel")
    public String adminPanel(Model model) {
        /*
            Liczba użytkowników w systemie.
        */
        long usersCount = userRepository.count();
        /*
            Liczba głównych obiektów.
        */
        long mainObjectsCount = mainObjectService.count();
        /*
            Liczba wszystkich rezerwacji.
        */
        long reservationsCount = reservationService.count();
        /*
            Dodajemy dane do modelu,
            żeby Thymeleaf mógł je wyświetlić w HTML-u.
        */
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("mainObjectsCount", mainObjectsCount);
        model.addAttribute("reservationsCount", reservationsCount);
        /*
            Dodatkowo dodajemy kilka list,
            gdyby HTML chciał pokazać podgląd danych.
        */
        model.addAttribute("mainObjects", mainObjectService.findAll());
        model.addAttribute("reservations", reservationService.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "admin/panel";
    }

    @GetMapping
    public String adminRoot() {
        return "redirect:/admin/panel";
    }

    @GetMapping("/")
    public String adminRootWithSlash() {
        return "redirect:/admin/panel";
    }

    /*
        LISTA UŻYTKOWNIKÓW
    */
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("usersCount", users.size());
        return "admin/users";
    }
    /*
        LISTA WSZYSTKICH REZERWACJI
    */
    @GetMapping("/all-reservations")
    public String allReservations(Model model) {
        /*
            Pobieramy wszystkie rezerwacje z bazy.
        */
        List<Reservation> reservations = reservationService.findAll();
        /*
            Przekazujemy do HTML-a jako ${reservations}.
        */
        model.addAttribute("reservations", reservations);
        /*
            Dodatkowy licznik.
        */
        model.addAttribute("reservationsCount", reservations.size());
        return "admin/all-reservations";
    }
    /*
        ALIAS DLA WSZYSTKICH REZERWACJI
    */
    @GetMapping("/reservations")
    public String reservationsAlias(Model model) {
        return allReservations(model);
    }
    /*
        ANULOWANIE REZERWACJI PRZEZ ADMINA
    */
    @GetMapping("/reservations/cancel/{id}")
    public String cancelReservationByAdmin(@PathVariable Long id,
                                           RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservationByAdmin(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rezerwacja została anulowana przez administratora."
            );
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/all-reservations";
    }
    @GetMapping("/all-reservations/cancel/{id}")
    public String cancelReservationByAdminAlias(@PathVariable Long id,
                                                RedirectAttributes redirectAttributes) {
        return cancelReservationByAdmin(id, redirectAttributes);
    }

    /*
        ANULOWANIE REZERWACJI PRZEZ POST
    */
    @PostMapping("/reservations/cancel/{id}")
    public String cancelReservationByAdminPost(@PathVariable Long id,
                                               RedirectAttributes redirectAttributes) {
        return cancelReservationByAdmin(id, redirectAttributes);
    }

    /*
        ZAKOŃCZENIE REZERWACJI
    */
    @GetMapping("/reservations/finish/{id}")
    public String finishReservation(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.finishReservation(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rezerwacja została oznaczona jako zakończona."
            );
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/all-reservations";
    }

    /*
        USUWANIE REZERWACJI PRZEZ ADMINA
    */
    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.delete(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rezerwacja została usunięta."
            );
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/all-reservations";
    }

    /*
        PRZEKIEROWANIE DO ZARZĄDZANIA MAIN OBJECT
    */
    @GetMapping("/objects")
    public String objectsAlias() {
        return "redirect:/admin/main-objects";
    }
}