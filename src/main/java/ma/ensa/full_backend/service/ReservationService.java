package ma.ensa.full_backend.service;

import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.TypeChambre;
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

    public Reservation createReservation(Reservation reservation) {
        System.out.println("Creating reservation: " + reservation);
        validateReservation(reservation);
        return repository.save(reservation);
    }

    public Reservation getReservation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation with ID " + id + " not found"));
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        // Fetch the existing reservation
        Reservation existingReservation = getReservation(id);

        // Update fields
        existingReservation.setClient(updatedReservation.getClient());
        existingReservation.setCheckInDate(updatedReservation.getCheckInDate());
        existingReservation.setCheckOutDate(updatedReservation.getCheckOutDate());
        existingReservation.setTypeChambre(updatedReservation.getTypeChambre());

        validateReservation(existingReservation);
        return repository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Reservation with ID " + id + " not found");
        }
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public List<Reservation> listAllReservations() {
        return repository.findAll();
    }

    public Page<Reservation> listReservations(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client with ID " + clientId + " not found"));
    }

    private void validateReservation(Reservation reservation) {
        if (reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates must not be null");
        }
        if (reservation.getCheckInDate().after(reservation.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }
        if (reservation.getClient() == null) {
            throw new IllegalArgumentException("Reservation must have a client");
        }
    }
}