package com.example.egzamin.controller;

import com.example.egzamin.dto.ReservationForm;
import com.example.egzamin.entity.MainObject;
import com.example.egzamin.entity.Reservation;
import com.example.egzamin.service.MainObjectService;
import com.example.egzamin.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/*
    Ten kontroler obsługuje panel zwykłego użytkownika.
    Czyli nie admina, tylko zalogowanego użytkownika z rolą USER.
    Użytkownik może tutaj:
    - wejść do swojego panelu,
    - zobaczyć dostępne obiekty,
    - przejść do formularza rezerwacji,
    - utworzyć rezerwację,
    - zobaczyć swoje rezerwacje,
    - anulować swoją rezerwację.
*/

@Controller
@RequestMapping("/user")
public class UserPanelController {

    /*
        SERWIS GŁÓWNEGO OBIEKTU
    */
    private final MainObjectService mainObjectService;

    /*
        SERWIS REZERWACJI
    */
    private final ReservationService reservationService;

    /*
        KONSTRUKTOR
    */
    public UserPanelController(MainObjectService mainObjectService,
                               ReservationService reservationService) {
        this.mainObjectService = mainObjectService;
        this.reservationService = reservationService;
    }

    /*
        PANEL UŻYTKOWNIKA
    */
    @GetMapping("/panel")
    public String userPanel(Model model, Principal principal) {
        String username = getUsername(principal);
        List<MainObject> activeMainObjects = mainObjectService.findAllActive();
        List<Reservation> userReservations = reservationService.findByUsername(username);
        model.addAttribute("username", username);
        model.addAttribute("mainObjects", activeMainObjects);
        model.addAttribute("availableObjectsCount", activeMainObjects.size());
        model.addAttribute("reservations", userReservations);
        model.addAttribute("reservationsCount", userReservations.size());
        return "user/panel";
    }

    /*
        ALIAS PANELU
    */
    @GetMapping
    public String userRoot() {
        return "redirect:/user/panel";
    }

    /*
        MOJE REZERWACJE
    */
    @GetMapping("/my-reservations")
    public String myReservations(Model model, Principal principal) {
        String username = getUsername(principal);

        List<Reservation> reservations = reservationService.findByUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("reservations", reservations);

        return "user/my-reservations";
    }

    /*
        ALIAS DLA MOICH REZERWACJI
    */
    @GetMapping("/reservations")
    public String myReservationsAlias(Model model, Principal principal) {
        return myReservations(model, principal);
    }

    /*
        FORMULARZ REZERWACJI
    */
    @GetMapping("/reservation/new")
    public String showReservationForm(Model model) {
        prepareReservationFormModel(model, new ReservationForm());

        return "user/reservation-form";
    }

    /*
        ALIAS FORMULARZA REZERWACJI
    */
    @GetMapping("/reservations/new")
    public String showReservationFormAlias(Model model) {
        return showReservationForm(model);
    }

    /*
        FORMULARZ REZERWACJI DLA KONKRETNEGO OBIEKTU
    */
    @GetMapping("/reservation/new/{mainObjectId}")
    public String showReservationFormForObject(@PathVariable Long mainObjectId, Model model) {
        ReservationForm form = new ReservationForm();
        form.setMainObjectId(mainObjectId);
        prepareReservationFormModel(model, form);
        MainObject selectedMainObject = mainObjectService.findById(mainObjectId);
        model.addAttribute("selectedMainObject", selectedMainObject);

        return "user/reservation-form";
    }

    /*
        ALIAS: REZERWUJ KONKRETNY OBIEKT
    */
    @GetMapping("/reserve/{mainObjectId}")
    public String reserveObject(@PathVariable Long mainObjectId, Model model) {
        return showReservationFormForObject(mainObjectId, model);
    }

    /*
        ZAPIS REZERWACJI
    */
    @PostMapping("/reservation")
    public String createReservation(@ModelAttribute("reservationForm") ReservationForm reservationForm,
                                    Principal principal,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        String username = getUsername(principal);
        try {
            reservationService.createReservation(reservationForm, username);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rezerwacja została utworzona."
            );
            return "redirect:/user/my-reservations";
        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            prepareReservationFormModel(model, reservationForm);
            return "user/reservation-form";
        }
    }

    /*
        ALIAS ZAPISU REZERWACJI
    */
    @PostMapping("/reservations")
    public String createReservationAlias(@ModelAttribute("reservationForm") ReservationForm reservationForm,
                                         Principal principal,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        return createReservation(reservationForm, principal, model, redirectAttributes);
    }

    /*
        ALIAS ZAPISU REZERWACJI NR 2
    */
    @PostMapping("/reservation/save")
    public String saveReservation(@ModelAttribute("reservationForm") ReservationForm reservationForm,
                                  Principal principal,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        return createReservation(reservationForm, principal, model, redirectAttributes);
    }

    /*
        ANULOWANIE REZERWACJI
    */
    @GetMapping("/reservation/cancel/{id}")
    public String cancelReservation(@PathVariable Long id,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        String username = getUsername(principal);

        try {
            reservationService.cancelReservation(id, username);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rezerwacja została anulowana."
            );
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/user/my-reservations";
    }

    /*
        ALIAS ANULOWANIA
    */
    @GetMapping("/reservations/cancel/{id}")
    public String cancelReservationAlias(@PathVariable Long id,
                                         Principal principal,
                                         RedirectAttributes redirectAttributes) {
        return cancelReservation(id, principal, redirectAttributes);
    }

    /*
        ANULOWANIE PRZEZ POST
    */
    @PostMapping("/reservation/cancel/{id}")
    public String cancelReservationPost(@PathVariable Long id,
                                        Principal principal,
                                        RedirectAttributes redirectAttributes) {
        return cancelReservation(id, principal, redirectAttributes);
    }
    private void prepareReservationFormModel(Model model, ReservationForm reservationForm) {
        if (reservationForm == null) {
            reservationForm = new ReservationForm();
        }
        List<MainObject> activeMainObjects = mainObjectService.findAllActive();
        model.addAttribute("reservationForm", reservationForm);
        model.addAttribute("mainObjects", activeMainObjects);
        model.addAttribute("now", LocalDateTime.now());
    }

    /*
        POBIERANIE USERNAME Z PRINCIPAL
    */
    private String getUsername(Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new IllegalStateException("Brak zalogowanego użytkownika");
        }

        return principal.getName();
    }
}