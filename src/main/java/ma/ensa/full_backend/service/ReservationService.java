package ma.ensa.full_backend.service;

import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.repository.ClientRepository;
import ma.ensa.full_backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    // Create a new reservation
    public Reservation createReservation(Reservation reservation) {
        validateReservation(reservation);
        return repository.save(reservation);
    }

    // Retrieve a reservation by ID
    public Reservation getReservation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation with ID " + id + " not found"));
    }

    // Update an existing reservation
    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation existingReservation = getReservation(id);
        updateReservationFields(existingReservation, updatedReservation);
        validateReservation(existingReservation);
        return repository.save(existingReservation);
    }

    // Delete a reservation by ID
    public void deleteReservation(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Reservation with ID " + id + " not found");
        }
        repository.deleteById(id);
    }

    // List all reservations
    public List<Reservation> listAllReservations() {
        return repository.findAll();
    }

    // List reservations with pagination and sorting
    public Page<Reservation> listReservations(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    // Helper method to update reservation fields
    private void updateReservationFields(Reservation existing, Reservation updated) {
        existing.setClient(updated.getClient());
        existing.setCheckInDate(updated.getCheckInDate());
        existing.setCheckOutDate(updated.getCheckOutDate());
        existing.setTypeChambre(updated.getTypeChambre());
    }

    // Validation method
    private void validateReservation(Reservation reservation) {
        if (reservation.getCheckInDate().isAfter(reservation.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }
        if (reservation.getClient() == null) {
            throw new IllegalArgumentException("Reservation must have a client");
        }
        // Add more validations as needed
    }

    // Fetch a client by ID
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client with ID " + clientId + " not found"));
    }
}
