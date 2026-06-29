package com.example.egzamin.rest;

import com.example.egzamin.dto.MainObjectDto;
import com.example.egzamin.entity.Reservation;
import com.example.egzamin.entity.User;
import com.example.egzamin.repository.UserRepository;
import com.example.egzamin.service.MainObjectService;
import com.example.egzamin.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
    ============================================================
    REST: AdminRestController
    ============================================================

    Ten kontroler jest REST API dla administratora.

    Czyli zwraca JSON, a nie HTML.

    Strony admina HTML są w:

    AdminPanelController
    MainObjectController

    A tutaj mamy API, np.:

    GET    /api/admin/stats
    GET    /api/admin/users
    GET    /api/admin/reservations
    POST   /api/admin/main-objects
    PUT    /api/admin/main-objects/{id}
    DELETE /api/admin/main-objects/{id}
*/
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final MainObjectService mainObjectService;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

    public AdminRestController(MainObjectService mainObjectService,
                               ReservationService reservationService,
                               UserRepository userRepository) {
        this.mainObjectService = mainObjectService;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    /*
        ============================================================
        STATYSTYKI ADMINA
        ============================================================

        Zwraca proste statystyki do JSON-a.

        Przykład wyniku:

        {
          "usersCount": 2,
          "mainObjectsCount": 5,
          "reservationsCount": 3
        }
    */
    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("usersCount", userRepository.count());
        stats.put("mainObjectsCount", mainObjectService.count());
        stats.put("reservationsCount", reservationService.count());

        return stats;
    }

    /*
        ============================================================
        UŻYTKOWNICY
        ============================================================

        Nie zwracamy całego obiektu User bezpośrednio,
        żeby przypadkiem nie pokazać hasła.

        Zwracamy tylko:
        - id,
        - username,
        - role.
    */
    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::userToMap)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        WSZYSTKIE REZERWACJE
        ============================================================

        Zwraca rezerwacje jako JSON.

        To jest API admina, więc pokazuje wszystkie rezerwacje.
    */
    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return reservationService.findAll();
    }

    /*
        ============================================================
        WSZYSTKIE GŁÓWNE OBIEKTY
        ============================================================

        Zwraca wszystkie obiekty jako DTO.

        Przykład:
        - wszystkie sale,
        - wszystkie auta,
        - wszystkie pokoje.
    */
    @GetMapping("/main-objects")
    public List<MainObjectDto> getMainObjects() {
        return mainObjectService.findAll()
                .stream()
                .map(MainObjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        DODAWANIE OBIEKTU PRZEZ API
        ============================================================

        POST /api/admin/main-objects

        Dane przychodzą w JSON-ie jako MainObjectDto.
    */
    @PostMapping("/main-objects")
    public MainObjectDto createMainObject(@RequestBody MainObjectDto dto) {
        return MainObjectDto.fromEntity(mainObjectService.create(dto));
    }

    /*
        ============================================================
        EDYCJA OBIEKTU PRZEZ API
        ============================================================

        PUT /api/admin/main-objects/{id}
    */
    @PutMapping("/main-objects/{id}")
    public MainObjectDto updateMainObject(@PathVariable Long id,
                                          @RequestBody MainObjectDto dto) {
        dto.setId(id);
        return MainObjectDto.fromEntity(mainObjectService.update(id, dto));
    }

    /*
        ============================================================
        DEZAKTYWACJA OBIEKTU PRZEZ API
        ============================================================

        DELETE /api/admin/main-objects/{id}

        Nie kasujemy fizycznie z bazy.
        Ustawiamy active = false.
    */
    @DeleteMapping("/main-objects/{id}")
    public void deactivateMainObject(@PathVariable Long id) {
        mainObjectService.deactivate(id);
    }

    /*
        ============================================================
        ANULOWANIE REZERWACJI PRZEZ API
        ============================================================

        PATCH /api/admin/reservations/{id}/cancel
    */
    @PatchMapping("/reservations/{id}/cancel")
    public Reservation cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservationByAdmin(id);
    }

    /*
        ============================================================
        POMOCNICZA METODA USER -> MAP
        ============================================================

        Dzięki temu nie zwracamy hasła użytkownika w JSON-ie.
    */
    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();

        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("role", user.getRole());

        return map;
    }
}