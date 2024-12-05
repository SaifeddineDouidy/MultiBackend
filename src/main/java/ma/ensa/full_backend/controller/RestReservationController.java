package ma.ensa.full_backend.controller;

import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class RestReservationController {
    @Autowired
    private ReservationService service;

    @PostMapping
    public Reservation create(@RequestBody Reservation reservation) {
        return service.createReservation(reservation);
    }

    @GetMapping("/{id}")
    public Reservation read(@PathVariable Long id) {
        return service.getReservation(id);
    }

    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        return service.updateReservation(id, updatedReservation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteReservation(id);
    }
}

