package ma.ensa.full_backend.controller;

import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.TypeChambre;
import ma.ensa.full_backend.service.ReservationService;
import ma.ensa.full_backend.soap.CreateReservationRequest;
import ma.ensa.full_backend.soap.CreateReservationResponse;
import ma.ensa.full_backend.soap.GetReservationRequest;
import ma.ensa.full_backend.soap.GetReservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;

@Endpoint
public class ReservationSoapEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/reservations";

    @Autowired
    private ReservationService reservationService;

    // Get a reservation by ID
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetReservationRequest")
    @ResponsePayload
    public GetReservationResponse getReservation(@RequestPayload GetReservationRequest request) {
        // Retrieve the reservation by id
        Reservation reservation = reservationService.getReservation(request.getId());

        // Prepare the response
        GetReservationResponse response = new GetReservationResponse();
        response.setReservation(mapToReservation(reservation)); // Convert Reservation entity to SOAP response format
        return response;
    }

    // Create a reservation
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateReservationRequest")
    @ResponsePayload
    public CreateReservationResponse createReservation(@RequestPayload CreateReservationRequest request) {
        // Map request to entity and save
        Reservation reservation = mapToEntity(request.getReservation());
        Reservation createdReservation = reservationService.createReservation(reservation);

        // Prepare response
        CreateReservationResponse response = new CreateReservationResponse();
        response.setReservation(mapToReservation(createdReservation)); // Convert Reservation entity to SOAP response format
        return response;
    }

    // Helper method to map Reservation entity to the SOAP response structure
    private Reservation mapToReservation(ma.ensa.full_backend.model.Reservation reservation) {
        Reservation reservationResponse = new Reservation();
        reservationResponse.setId(reservation.getId());
        reservationResponse.setClient(reservation.getClient());  // Ensure Client is serialized appropriately
        reservationResponse.setCheckInDate(reservation.getCheckInDate()); // Convert LocalDate to String
        reservationResponse.setCheckOutDate(reservation.getCheckOutDate()); // Convert LocalDate to String
        reservationResponse.setTypeChambre(reservation.getTypeChambre()); // Convert Enum to String
        return reservationResponse;
    }


    // Helper method to map SOAP request to Reservation entity
    private Reservation mapToEntity(Reservation request) {
        Reservation reservation = new Reservation();
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        // Set the Client and TypeChambre appropriately
        return reservation;
    }
}
