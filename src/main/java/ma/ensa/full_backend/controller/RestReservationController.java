package ma.ensa.full_backend.controller;

import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class RestReservationController {
    @Autowired
    private ReservationService service;

    @PostMapping
    public Reservation create(
            @RequestBody ReservationRequest reservationRequest
    ) {
        return service.createReservation(
                reservationRequest.getReservation(),
                reservationRequest.getChambreIds()
        );
    }

    @PutMapping("/{id}")
    public Reservation update(
            @PathVariable Long id,
            @RequestBody ReservationRequest reservationRequest
    ) {
        return service.updateReservation(
                id,
                reservationRequest.getReservation(),
                reservationRequest.getChambreIds()
        );
    }

    @GetMapping("/{id}")
    public Reservation read(@PathVariable Long id) {
        return service.getReservation(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    // Static inner class to handle request with both Reservation and Chamber IDs
    public static class ReservationRequest {
        private Reservation reservation;
        private List<Long> chambreIds;

        public Reservation getReservation() {
            return reservation;
        }

        public void setReservation(Reservation reservation) {
            this.reservation = reservation;
        }

        public List<Long> getChambreIds() {
            return chambreIds;
        }

        public void setChambreIds(List<Long> chambreIds) {
            this.chambreIds = chambreIds;
        }
    }
}