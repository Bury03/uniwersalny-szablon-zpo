package com.example.egzamin.controller;

import com.example.egzamin.dto.MainObjectDto;
import com.example.egzamin.entity.AdditionalEntity;
import com.example.egzamin.entity.MainObject;
import com.example.egzamin.repository.AdditionalEntityRepository;
import com.example.egzamin.service.MainObjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
    To jest kontroler MVC dla głównego obiektu aplikacji.
    MVC oznacza, że ten kontroler zwraca strony HTML, czyli widoki Thymeleaf.
    Ten kontroler działa na stronach typu:
    - /admin/main-objects
    - /admin/main-objects/new
    - /admin/main-objects/edit/{id}
    - /admin/main-objects/delete/{id}
*/
@Controller
@RequestMapping("/admin/main-objects")
public class MainObjectController {

    /*
        SERWIS GŁÓWNEGO OBIEKTU
    */
    private final MainObjectService mainObjectService;

    /*
        REPOZYTORIUM ENCJI DODATKOWEJ
    */
    private final AdditionalEntityRepository additionalEntityRepository;

    /*
        KONSTRUKTOR
    */
    public MainObjectController(MainObjectService mainObjectService,
                                AdditionalEntityRepository additionalEntityRepository) {
        this.mainObjectService = mainObjectService;
        this.additionalEntityRepository = additionalEntityRepository;
    }

    /*
        LISTA OBIEKTÓW
    */
    @GetMapping
    public String listMainObjects(Model model) {
        List<MainObject> mainObjects = mainObjectService.findAll();
        model.addAttribute("mainObjects", mainObjects);
        model.addAttribute("mainObjectDto", new MainObjectDto());
        model.addAttribute("additionalEntities", additionalEntityRepository.findAll());
        return "admin/main-objects";
    }

    /*
        ALIAS LISTY
    */
    @GetMapping("/")
    public String listMainObjectsWithSlash() {
        return "redirect:/admin/main-objects";
    }

    /*
        FORMULARZ DODAWANIA NOWEGO OBIEKTU
    */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("mainObjectDto", new MainObjectDto());
        model.addAttribute("additionalEntities", additionalEntityRepository.findAll());
        model.addAttribute("mainObjects", mainObjectService.findAll());
        model.addAttribute("editMode", false);
        return "admin/main-objects";
    }

    /*
        ZAPIS NOWEGO OBIEKTU
    */
    @PostMapping
    public String createMainObject(@ModelAttribute("mainObjectDto") MainObjectDto mainObjectDto) {
        mainObjectService.create(mainObjectDto);
        return "redirect:/admin/main-objects";
    }

    /*
        ALIAS ZAPISU
    */
    @PostMapping("/save")
    public String saveMainObject(@ModelAttribute("mainObjectDto") MainObjectDto mainObjectDto) {
        mainObjectService.saveDto(mainObjectDto);
        return "redirect:/admin/main-objects";
    }

    /*
        FORMULARZ EDYCJI
    */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MainObjectDto mainObjectDto = mainObjectService.getDtoById(id);
        model.addAttribute("mainObjectDto", mainObjectDto);
        model.addAttribute("additionalEntities", additionalEntityRepository.findAll());
        model.addAttribute("mainObjects", mainObjectService.findAll());
        model.addAttribute("editMode", true);

        return "admin/main-objects";
    }

    /*
        AKTUALIZACJA OBIEKTU
    */
    @PostMapping("/update/{id}")
    public String updateMainObject(@PathVariable Long id,
                                   @ModelAttribute("mainObjectDto") MainObjectDto mainObjectDto) {
        mainObjectDto.setId(id);
        mainObjectService.update(id, mainObjectDto);
        return "redirect:/admin/main-objects";
    }

    /*
        USUWANIE OBIEKTU
    */
    @GetMapping("/delete/{id}")
    public String deleteMainObject(@PathVariable Long id) {
        mainObjectService.delete(id);

        return "redirect:/admin/main-objects";
    }

    /*
        ALIAS USUWANIA PRZEZ POST
    */
    @PostMapping("/delete/{id}")
    public String deleteMainObjectPost(@PathVariable Long id) {
        mainObjectService.delete(id);

        return "redirect:/admin/main-objects";
    }

    /*
        DEZAKTYWACJA OBIEKTU
    */
    @GetMapping("/deactivate/{id}")
    public String deactivateMainObject(@PathVariable Long id) {
        mainObjectService.deactivate(id);

        return "redirect:/admin/main-objects";
    }

    /*
        AKTYWACJA OBIEKTU
    */
    @GetMapping("/activate/{id}")
    public String activateMainObject(@PathVariable Long id) {
        mainObjectService.activate(id);

        return "redirect:/admin/main-objects";
    }
}