package ma.ensa.full_backend.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.TypeChambre;
import ma.ensa.full_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@WebService(targetNamespace = "http://ws.full_backend.ensa.ma/", serviceName = "ReservationWS")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class ReservationSoapService {

    @Autowired
    private ReservationService reservationService;

    @WebMethod(action = "http://ws.full_backend.ensa.ma/getReservationById")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Reservation getReservationById(@WebParam(name = "id") Long id) {
        return reservationService.getReservation(id);
    }

    @WebMethod(action = "http://ws.full_backend.ensa.ma/createReservation")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Reservation createReservation(
            @WebParam(name = "checkInDate") String checkInDateStr,
            @WebParam(name = "checkOutDate") String checkOutDateStr,
            @WebParam(name = "client") Client client,
            @WebParam(name = "chambreIds") List<Long> chambreIds) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkInDate = sdf.parse(checkInDateStr);
            Date checkOutDate = sdf.parse(checkOutDateStr);

            // Create a new reservation with provided details
            Reservation reservation = new Reservation();
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setClient(client);

            // If no chamber IDs provided, create an empty list
            if (chambreIds == null) {
                chambreIds = new ArrayList<>();
            }

            // Create reservation with selected chambers
            return reservationService.createReservation(reservation, chambreIds);
        } catch (ParseException e) {
            // Handle error in date parsing
            throw new RuntimeException("Invalid date format", e);
        } catch (IllegalArgumentException e) {
            // Handle cases where no chambers are available
            throw new RuntimeException("Unable to create reservation", e);
        }
    }

    @WebMethod(action = "http://ws.full_backend.ensa.ma/updateReservation")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Reservation updateReservation(
            @WebParam(name = "id") Long id,
            @WebParam(name = "checkInDate") String checkInDateStr,
            @WebParam(name = "checkOutDate") String checkOutDateStr,
            @WebParam(name = "client") Client client,
            @WebParam(name = "chambreIds") List<Long> chambreIds) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkInDate = sdf.parse(checkInDateStr);
            Date checkOutDate = sdf.parse(checkOutDateStr);

            // Create an updated reservation object
            Reservation updatedReservation = new Reservation();
            updatedReservation.setCheckInDate(checkInDate);
            updatedReservation.setCheckOutDate(checkOutDate);
            updatedReservation.setClient(client);

            // If no chamber IDs provided, create an empty list
            if (chambreIds == null) {
                chambreIds = new ArrayList<>();
            }

            // Update the reservation with new details and chambers
            return reservationService.updateReservation(id, updatedReservation, chambreIds);
        } catch (ParseException e) {
            // Handle error in date parsing
            throw new RuntimeException("Invalid date format", e);
        } catch (IllegalArgumentException e) {
            // Handle cases where no chambers are available
            throw new RuntimeException("Unable to update reservation", e);
        }
    }

    @WebMethod(action = "http://ws.full_backend.ensa.ma/deleteReservation")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public boolean deleteReservation(@WebParam(name = "id") Long id) {
        try {
            reservationService.deleteReservation(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}