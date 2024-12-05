//package ma.ensa.full_backend.controller;
//
//import ma.ensa.full_backend.model.Client;
//import ma.ensa.full_backend.model.Reservation;
//import ma.ensa.full_backend.model.ReservationInput;
//import ma.ensa.full_backend.model.TypeChambre;
//import ma.ensa.full_backend.service.ReservationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@Component
//public class ReservationGraphQLResolver {
//
//    @Autowired
//    private ReservationService service;
//
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    @QueryMapping
//    public Reservation getReservation(@Argument Long id) {
//        return service.getReservation(id);
//    }
//
//    @QueryMapping
//    public List<Reservation> getReservations() {
//        return service.listAllReservations();
//    }
//
//    @MutationMapping
//    public Reservation createReservation(@Argument ReservationInput input) {
//        Reservation reservation = new Reservation();
//
//        // Fetch client by ID
//        Client client = service.getClientById(input.getClientId());
//        reservation.setClient(client);
//
//        // Parsing dates into variables first
//        String checkInDateString = String.valueOf(input.getCheckInDate());
//        String checkOutDateString = String.valueOf(input.getCheckOutDate());
//
//        // Convert String to LocalDate using the DATE_FORMATTER
//        LocalDate checkInDate = LocalDate.parse(checkInDateString, DATE_FORMATTER);
//        LocalDate checkOutDate = LocalDate.parse(checkOutDateString, DATE_FORMATTER);
//
//        // Set the parsed dates
//        reservation.setCheckInDate(checkInDate);
//        reservation.setCheckOutDate(checkOutDate);
//
//        // Set the room type
//        reservation.setTypeChambre(TypeChambre.valueOf(input.getTypeChambre()));
//
//        // Save reservation
//        return service.createReservation(reservation);
//    }
//
//    @MutationMapping
//    public Reservation updateReservation(@Argument Long id, @Argument ReservationInput input) {
//        Reservation existingReservation = service.getReservation(id);
//
//        // Update client if provided
//        if (input.getClientId() != null) {
//            Client client = service.getClientById(input.getClientId());
//            existingReservation.setClient(client);
//        }
//
//        // Parsing dates into variables first
//        if (input.getCheckInDate() != null) {
//            String checkInDateString = String.valueOf(input.getCheckInDate());
//            LocalDate checkInDate = LocalDate.parse(checkInDateString, DATE_FORMATTER);
//            existingReservation.setCheckInDate(checkInDate);
//        }
//
//        if (input.getCheckOutDate() != null) {
//            String checkOutDateString = String.valueOf(input.getCheckOutDate());
//            LocalDate checkOutDate = LocalDate.parse(checkOutDateString, DATE_FORMATTER);
//            existingReservation.setCheckOutDate(checkOutDate);
//        }
//
//        // Update room type if provided
//        if (input.getTypeChambre() != null) {
//            existingReservation.setTypeChambre(TypeChambre.valueOf(input.getTypeChambre()));
//        }
//
//        // Save updated reservation
//        return service.updateReservation(id, existingReservation);
//    }
//
//    @MutationMapping
//    public Boolean deleteReservation(@Argument Long id) {
//        service.deleteReservation(id);
//        return true;
//    }
//}
