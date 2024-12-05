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
import java.util.Date;

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
            @WebParam(name = "typeChambre") TypeChambre typeChambre,
            @WebParam(name = "client") Client client) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkInDate = sdf.parse(checkInDateStr);
            Date checkOutDate = sdf.parse(checkOutDateStr);

            // Create a new reservation with provided details
            Reservation reservation = new Reservation(null, checkInDate, checkOutDate, typeChambre, client);
            return reservationService.createReservation(reservation);
        } catch (ParseException e) {
            // Handle error in date parsing
            e.printStackTrace();
            return null; // or throw an exception
        }
    }


    @WebMethod(action = "http://ws.full_backend.ensa.ma/updateReservation")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Reservation updateReservation(
            @WebParam(name = "id") Long id,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate,
            @WebParam(name = "typeChambre") TypeChambre typeChambre,
            @WebParam(name = "client") Client client) {

        // Retrieve the existing reservation by ID
        Reservation reservation = reservationService.getReservation(id);

        if (reservation != null) {
            // Update reservation details
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setTypeChambre(typeChambre);
            reservation.setClient(client);

            // Save the updated reservation
            return reservationService.updateReservation(id,reservation);
        } else {
            return null; // Reservation not found
        }
    }

    @WebMethod(action = "http://ws.full_backend.ensa.ma/deleteReservation")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public boolean deleteReservation(@WebParam(name = "id") Long id) {
        if (reservationService.existsById(id)) {
            reservationService.deleteReservation(id);
            return true;
        }
        return false;
    }
}
