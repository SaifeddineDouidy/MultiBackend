package ma.ensa.full_backend.controller;

import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.ReservationInput;
import ma.ensa.full_backend.model.TypeChambre;  // Import TypeChambre enum
import ma.ensa.full_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationGraphQLResolver {

    @Autowired
    private ReservationService service;

    // Query for a single reservation
    public Reservation getReservation(Long id) {
        return service.getReservation(id);
    }

    // Query for all reservations
    public List<Reservation> getReservations() {
        return service.listAllReservations();
    }

    // Mutation for creating a reservation
    public Reservation createReservation(ReservationInput input) {
        // Map ReservationInput to Reservation entity
        Reservation reservation = new Reservation();

        // Set the client using the clientId provided in the input
        // Assuming you have a method to fetch Client by ID
        reservation.setClient(service.getClientById(input.getClientId()));

        reservation.setCheckInDate(input.getCheckInDate());
        reservation.setCheckOutDate(input.getCheckOutDate());

        // Assuming TypeChambre is an enum, convert the String to the corresponding enum value
        reservation.setTypeChambre(TypeChambre.valueOf(input.getTypeChambre()));

        return service.createReservation(reservation);
    }

    // Mutation for deleting a reservation
    public Boolean deleteReservation(Long id) {
        service.deleteReservation(id);
        return true;
    }
}
