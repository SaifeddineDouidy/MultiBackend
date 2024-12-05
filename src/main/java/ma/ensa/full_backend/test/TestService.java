package ma.ensa.full_backend.test;

import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.TypeChambre;
import ma.ensa.full_backend.repository.ClientRepository;
import ma.ensa.full_backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Date;

@Service
public class TestService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public void createTestData() {
        // Create Client
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");

        // Save Client
        client = clientRepository.save(client);

        // Create Reservation for the client
        Reservation reservation = new Reservation();
        reservation.setClient(client);  // Link the client to the reservation

        // Set check-in date as tomorrow
        Date checkInDate = new Date();
        checkInDate.setTime(checkInDate.getTime() + (24 * 60 * 60 * 1000));  // Add one day (24 hours)
        reservation.setCheckInDate(checkInDate);

        // Set check-out date as 5 days from now
        Date checkOutDate = new Date();
        checkOutDate.setTime(checkOutDate.getTime() + (5 * 24 * 60 * 60 * 1000));  // Add five days
        reservation.setCheckOutDate(checkOutDate);

        reservation.setTypeChambre(TypeChambre.SINGLE); // Assuming SINGLE is one of the values in TypeChambre enum

        // Save Reservation
        reservationRepository.save(reservation);

        System.out.println("Test client and reservation have been created.");
    }
}
