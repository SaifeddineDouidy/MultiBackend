package ma.ensa.full_backend.grpc;

import io.grpc.stub.StreamObserver;
import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.TypeChambre;
import ma.ensa.full_backend.service.ReservationService;
import ma.ensa.full_backend.stubs.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@GrpcService
public class ReservationGrpcService extends ReservationServiceGrpc.ReservationServiceImplBase {

    @Autowired
    private ReservationService reservationService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void createReservation(CreateReservationRequest request, StreamObserver<CreateReservationResponse> responseObserver) {
        try {
            // Validate and parse dates
            Date checkInDate = parseDate(request.getCheckInDate());
            Date checkOutDate = parseDate(request.getCheckOutDate());

            if (checkInDate.after(checkOutDate)) {
                responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                        .withDescription("Check-in date must be before check-out date.")
                        .asRuntimeException());
                return;
            }

            // Fetch client by ID
            Optional<Client> clientOptional = Optional.ofNullable(reservationService.getClientById(request.getClientId()));
            if (clientOptional.isEmpty()) {
                responseObserver.onError(io.grpc.Status.NOT_FOUND
                        .withDescription("Client with ID " + request.getClientId() + " not found.")
                        .asRuntimeException());
                return;
            }

            // Validate room type
            TypeChambre typeChambre;
            try {
                typeChambre = TypeChambre.valueOf(request.getTypeChambre().toUpperCase());
            } catch (IllegalArgumentException e) {
                responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                        .withDescription("Invalid room type: " + request.getTypeChambre())
                        .asRuntimeException());
                return;
            }

            // Create and save the reservation
            Reservation reservation = new Reservation();
            reservation.setClient(clientOptional.get());
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setTypeChambre(typeChambre);

            Reservation savedReservation = reservationService.createReservation(reservation);

            // Build and send the response
            CreateReservationResponse response = CreateReservationResponse.newBuilder()
                    .setId(savedReservation.getId())
                    .setMessage("Reservation created successfully.")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (ParseException e) {
            responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                    .withDescription("Invalid date format. Expected format is yyyy-MM-dd: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Unexpected error during reservation creation: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getReservation(GetReservationRequest request, StreamObserver<GetReservationResponse> responseObserver) {
        try {
            Reservation reservation = reservationService.getReservation(request.getId());

            ma.ensa.full_backend.stubs.Reservation grpcReservation = ma.ensa.full_backend.stubs.Reservation.newBuilder()
                    .setId(reservation.getId())
                    .setCheckInDate(DATE_FORMAT.format(reservation.getCheckInDate()))
                    .setCheckOutDate(DATE_FORMAT.format(reservation.getCheckOutDate()))
                    .setTypeChambre(reservation.getTypeChambre().name())
                    .build();

            GetReservationResponse response = GetReservationResponse.newBuilder()
                    .setReservation(grpcReservation)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NoSuchElementException e) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Reservation not found: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Unexpected error retrieving reservation: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void updateReservation(UpdateReservationRequest request, StreamObserver<UpdateReservationResponse> responseObserver) {
        try {
            Date checkInDate = parseDate(request.getReservation().getCheckInDate());
            Date checkOutDate = parseDate(request.getReservation().getCheckOutDate());

            if (checkInDate.after(checkOutDate)) {
                responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                        .withDescription("Check-in date must be before check-out date.")
                        .asRuntimeException());
                return;
            }

            TypeChambre typeChambre = TypeChambre.valueOf(request.getReservation().getTypeChambre().toUpperCase());

            Reservation reservationToUpdate = new Reservation();
            reservationToUpdate.setId(request.getReservation().getId());
            reservationToUpdate.setCheckInDate(checkInDate);
            reservationToUpdate.setCheckOutDate(checkOutDate);
            reservationToUpdate.setTypeChambre(typeChambre);

            Client client = reservationService.getClientById(request.getReservation().getClientId());
            reservationToUpdate.setClient(client);

            Reservation updatedReservation = reservationService.updateReservation(
                    reservationToUpdate.getId(), reservationToUpdate);

            ma.ensa.full_backend.stubs.Reservation grpcReservation = ma.ensa.full_backend.stubs.Reservation.newBuilder()
                    .setId(updatedReservation.getId())
                    .setCheckInDate(DATE_FORMAT.format(updatedReservation.getCheckInDate()))
                    .setCheckOutDate(DATE_FORMAT.format(updatedReservation.getCheckOutDate()))
                    .setTypeChambre(updatedReservation.getTypeChambre().name())
                    .build();

            UpdateReservationResponse response = UpdateReservationResponse.newBuilder()
                    .setReservation(grpcReservation)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (NoSuchElementException e) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Reservation or client not found: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Unexpected error updating reservation: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteReservation(DeleteReservationRequest request, StreamObserver<DeleteReservationResponse> responseObserver) {
        try {
            reservationService.deleteReservation(request.getId());

            DeleteReservationResponse response = DeleteReservationResponse.newBuilder().build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NoSuchElementException e) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Reservation not found: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Unexpected error deleting reservation: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    private Date parseDate(String date) throws ParseException {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date must not be null or empty.");
        }
        return DATE_FORMAT.parse(date);
    }
}
