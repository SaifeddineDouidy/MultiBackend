package ma.ensa.full_backend.grpc;

import io.grpc.stub.StreamObserver;
import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.TypeChambre;
import ma.ensa.full_backend.service.ReservationService;
import ma.ensa.full_backend.stubs.CreateReservationRequest;
import ma.ensa.full_backend.stubs.CreateReservationResponse;
import ma.ensa.full_backend.stubs.DeleteReservationRequest;
import ma.ensa.full_backend.stubs.DeleteReservationResponse;
import ma.ensa.full_backend.stubs.GetReservationRequest;
import ma.ensa.full_backend.stubs.GetReservationResponse;
import ma.ensa.full_backend.stubs.ReservationServiceGrpc;
import ma.ensa.full_backend.stubs.UpdateReservationRequest;
import ma.ensa.full_backend.stubs.UpdateReservationResponse;
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

    @Override
    public void createReservation(CreateReservationRequest request, StreamObserver<CreateReservationResponse> responseObserver) {
        try {
            // Validate and parse dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date checkInDate = parseDate(request.getCheckInDate(), dateFormat);
            Date checkOutDate = parseDate(request.getCheckOutDate(), dateFormat);

            if (checkInDate.after(checkOutDate)) {
                responseObserver.onError(new IllegalArgumentException("Check-in date must be before check-out date."));
                return;
            }

            // Fetch client by ID
            Optional<Client> clientOptional = Optional.ofNullable(reservationService.getClientById(request.getClientId()));
            if (clientOptional.isEmpty()) {
                responseObserver.onError(new IllegalArgumentException("Client with ID " + request.getClientId() + " not found."));
                return;
            }

            // Validate room type
            TypeChambre typeChambre;
            try {
                typeChambre = TypeChambre.valueOf(request.getTypeChambre().toUpperCase());
            } catch (IllegalArgumentException e) {
                responseObserver.onError(new IllegalArgumentException("Invalid room type: " + request.getTypeChambre()));
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
        }  catch (ParseException e) {
            // More specific error handling
            io.grpc.Status status = io.grpc.Status.INVALID_ARGUMENT
                .withDescription("Invalid date format. Expected format is yyyy-MM-dd: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (IllegalArgumentException e) {
            // Propagate specific validation errors
            io.grpc.Status status = io.grpc.Status.INVALID_ARGUMENT
                .withDescription(e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (Exception e) {
            // Log the full exception for server-side debugging
            e.printStackTrace();
            io.grpc.Status status = io.grpc.Status.INTERNAL
                .withDescription("Unexpected error during reservation creation: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        }
    }

    private Date parseDate(String date, SimpleDateFormat dateFormat) throws ParseException {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date must not be null or empty.");
        }
        return dateFormat.parse(date);
    }
    @Override
    public void getReservation(GetReservationRequest request, StreamObserver<GetReservationResponse> responseObserver) {
        try {
            // Retrieve the reservation
            Reservation reservation = reservationService.getReservation(request.getId());

            // Convert to gRPC response
            ma.ensa.full_backend.stubs.Reservation grpcReservation = ma.ensa.full_backend.stubs.Reservation.newBuilder()
                .setId(reservation.getId())
                .setCheckInDate(new SimpleDateFormat("yyyy-MM-dd").format(reservation.getCheckInDate()))
                .setCheckOutDate(new SimpleDateFormat("yyyy-MM-dd").format(reservation.getCheckOutDate()))
                .setTypeChambre(reservation.getTypeChambre().name())
                .build();

            // Build and send the response
            GetReservationResponse response = GetReservationResponse.newBuilder()
                .setReservation(grpcReservation)
                .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NoSuchElementException e) {
            io.grpc.Status status = io.grpc.Status.NOT_FOUND
                .withDescription("Reservation not found: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (Exception e) {
            io.grpc.Status status = io.grpc.Status.INTERNAL
                .withDescription("Unexpected error retrieving reservation: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void updateReservation(UpdateReservationRequest request, StreamObserver<UpdateReservationResponse> responseObserver) {
        try {
            // Validate dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date checkInDate = dateFormat.parse(request.getReservation().getCheckInDate());
            Date checkOutDate = dateFormat.parse(request.getReservation().getCheckOutDate());

            if (checkInDate.after(checkOutDate)) {
                responseObserver.onError(new IllegalArgumentException("Check-in date must be before check-out date."));
                return;
            }

            // Validate room type
            TypeChambre typeChambre = TypeChambre.valueOf(request.getReservation().getTypeChambre().toUpperCase());

            // Prepare reservation object for update
            Reservation reservationToUpdate = new Reservation();
            reservationToUpdate.setId(request.getReservation().getId());
            reservationToUpdate.setCheckInDate(checkInDate);
            reservationToUpdate.setCheckOutDate(checkOutDate);
            reservationToUpdate.setTypeChambre(typeChambre);

            // Fetch and set the client (assuming client ID is passed or retrieved)
            Client client = reservationService.getClientById(request.getReservation().getId()); // You might need to adjust this
            reservationToUpdate.setClient(client);

            // Perform the update
            Reservation updatedReservation = reservationService.updateReservation(
                reservationToUpdate.getId(), 
                reservationToUpdate
            );

            // Convert to gRPC response
            ma.ensa.full_backend.stubs.Reservation grpcReservation = ma.ensa.full_backend.stubs.Reservation.newBuilder()
                .setId(updatedReservation.getId())
                .setCheckInDate(dateFormat.format(updatedReservation.getCheckInDate()))
                .setCheckOutDate(dateFormat.format(updatedReservation.getCheckOutDate()))
                .setTypeChambre(updatedReservation.getTypeChambre().name())
                .build();

            // Build and send the response
            UpdateReservationResponse response = UpdateReservationResponse.newBuilder()
                .setReservation(grpcReservation)
                .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            io.grpc.Status status = io.grpc.Status.INVALID_ARGUMENT
                .withDescription(e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (NoSuchElementException e) {
            io.grpc.Status status = io.grpc.Status.NOT_FOUND
                .withDescription("Reservation or client not found: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (Exception e) {
            io.grpc.Status status = io.grpc.Status.INTERNAL
                .withDescription("Unexpected error updating reservation: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void deleteReservation(DeleteReservationRequest request, StreamObserver<DeleteReservationResponse> responseObserver) {
        try {
            // Attempt to delete the reservation
            reservationService.deleteReservation(request.getId());

            // Build and send the response
            DeleteReservationResponse response = DeleteReservationResponse.newBuilder().build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NoSuchElementException e) {
            io.grpc.Status status = io.grpc.Status.NOT_FOUND
                .withDescription("Reservation not found: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (Exception e) {
            io.grpc.Status status = io.grpc.Status.INTERNAL
                .withDescription("Unexpected error deleting reservation: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        }
    }
}