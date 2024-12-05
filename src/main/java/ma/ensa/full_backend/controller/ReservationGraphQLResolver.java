package ma.ensa.full_backend.controller;

import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.ReservationInput;
import ma.ensa.full_backend.model.TypeChambre;
import ma.ensa.full_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ReservationGraphQLResolver {

    @Autowired
    private ReservationService service;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @QueryMapping
    public Reservation getReservation(@Argument Long id) {
        return service.getReservation(id);
    }

    @QueryMapping
    public List<Reservation> getReservations() {
        System.out.println("WE IN GET");
        List<Reservation> reservations = service.listAllReservations();
        System.out.println(reservations); // Debugging step
        return reservations;
    }


    @MutationMapping
    public Reservation createReservation(@Argument ReservationInput input) {
        Reservation reservation = new Reservation();

        // Fetch client by ID
        Client client = service.getClientById(input.getClientId());
        reservation.setClient(client);

        // Parse and set check-in date
        Date checkInDate = input.getCheckInDate();
        reservation.setCheckInDate(checkInDate);

        // Parse and set check-out date
        Date checkOutDate = input.getCheckOutDate();
        reservation.setCheckOutDate(checkOutDate);

        // Set the room type
        reservation.setTypeChambre(TypeChambre.valueOf(input.getTypeChambre()));

        // Save reservation
        return service.createReservation(reservation);
    }

    @MutationMapping
    public Reservation updateReservation(@Argument Long id, @Argument ReservationInput input) {
        Reservation existingReservation = service.getReservation(id);

        // Update client if provided
        if (input.getClientId() != null) {
            Client client = service.getClientById(input.getClientId());
            existingReservation.setClient(client);
        }

        // Update check-in date if provided
        if (input.getCheckInDate() != null) {
            Date checkInDate = input.getCheckInDate();
            existingReservation.setCheckInDate(checkInDate);
        }

        // Update check-out date if provided
        if (input.getCheckOutDate() != null) {
            Date checkOutDate = input.getCheckOutDate();
            existingReservation.setCheckOutDate(checkOutDate);
        }

        // Update room type if provided
        if (input.getTypeChambre() != null) {
            existingReservation.setTypeChambre(TypeChambre.valueOf(input.getTypeChambre()));
        }

        // Save updated reservation
        return service.updateReservation(id, existingReservation);
    }

    @MutationMapping
    public Boolean deleteReservation(@Argument Long id) {
        service.deleteReservation(id);
        return true;
    }
}