//package ma.ensa.full_backend.controller;
//
//import ma.ensa.full_backend.model.Reservation;
//import ma.ensa.full_backend.model.TypeChambre;
//import ma.ensa.full_backend.service.ReservationService;
//import ma.ensa.full_backend.stubs.ReservationServiceGrpc;
//import net.devh.boot.grpc.server.service.GrpcService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import io.grpc.stub.StreamObserver;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@GrpcService
//public class ReservationGrpcImpl extends ReservationServiceGrpc.ReservationServiceImplBase {
//
//    private final ReservationService reservationService;
//
//    // Constructor injection of the reservation service
//    @Autowired
//    public ReservationGrpcImpl(ReservationService reservationService) {
//        this.reservationService = reservationService;
//    }
//
//    // Create a new reservation
//    @Override
//    public void createReservation(ReservationOuterClass.CreateReservationRequest request,
//                                  StreamObserver<ReservationOuterClass.CreateReservationResponse> responseObserver) throws ParseException {
//        // Convert the gRPC request into a Reservation object
//        var reservationReq = request.getReservation();
//        var reservation = new Reservation();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust pattern if needed
//
//        Date checkInDate = dateFormat.parse(reservationReq.getCheckInDate());
//        Date checkOutDate = dateFormat.parse(reservationReq.getCheckOutDate());
//
//        reservation.setCheckInDate(checkInDate);
//        reservation.setCheckOutDate(checkOutDate);
//        reservation.setTypeChambre(TypeChambre.valueOf(reservationReq.getTypeChambre()));
//
//        // Call the service to save the reservation
//        var savedReservation = reservationService.createReservation(reservation);
//
//        // Build the gRPC response
//        var grpcReservation = ReservationOuterClass.Reservation.newBuilder()
//                .setId(savedReservation.getId())
//                .setCheckInDate(savedReservation.getCheckInDate().toString())
//                .setCheckOutDate(savedReservation.getCheckOutDate().toString())
//                .setTypeChambre(savedReservation.getTypeChambre().name())
//                .build();
//
//        responseObserver.onNext(ReservationOuterClass.CreateReservationResponse.newBuilder()
//                .setReservation(grpcReservation)
//                .build());
//        responseObserver.onCompleted();
//    }
//
//    // Get reservation by ID
//    @Override
//    public void getReservation(ReservationOuterClass.GetReservationRequest request,
//                               StreamObserver<ReservationOuterClass.GetReservationResponse> responseObserver) {
//        var reservationId = request.getId();
//        var reservation = reservationService.getReservation(reservationId);
//
//        var grpcReservation = ReservationOuterClass.Reservation.newBuilder()
//                .setId(reservation.getId())
//                .setCheckInDate(reservation.getCheckInDate().toString())
//                .setCheckOutDate(reservation.getCheckOutDate().toString())
//                .setTypeChambre(reservation.getTypeChambre().name())
//                .build();
//
//        responseObserver.onNext(ReservationOuterClass.GetReservationResponse.newBuilder()
//                .setReservation(grpcReservation)
//                .build());
//        responseObserver.onCompleted();
//    }
//
//    // Update reservation
//    @Override
//    public void updateReservation(ReservationOuterClass.UpdateReservationRequest request,
//                                  StreamObserver<ReservationOuterClass.UpdateReservationResponse> responseObserver) throws ParseException {
//        var reservationReq = request.getReservation();
//        var reservation = new Reservation();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust pattern if needed
//
//        Date checkInDate = dateFormat.parse(reservationReq.getCheckInDate());
//        Date checkOutDate = dateFormat.parse(reservationReq.getCheckOutDate());
//
//        reservation.setCheckInDate(checkInDate);
//        reservation.setCheckOutDate(checkOutDate);
//        reservation.setId(reservationReq.getId());
//        reservation.setCheckInDate(checkInDate);
//        reservation.setCheckOutDate(checkOutDate);
//        reservation.setTypeChambre(TypeChambre.valueOf(reservationReq.getTypeChambre()));
//
//        var updatedReservation = reservationService.updateReservation(reservation.getId(), reservation);
//
//        var grpcReservation = ReservationOuterClass.Reservation.newBuilder()
//                .setId(updatedReservation.getId())
//                .setCheckInDate(updatedReservation.getCheckInDate().toString())
//                .setCheckOutDate(updatedReservation.getCheckOutDate().toString())
//                .setTypeChambre(updatedReservation.getTypeChambre().name())
//                .build();
//
//        responseObserver.onNext(ReservationOuterClass.UpdateReservationResponse.newBuilder()
//                .setReservation(grpcReservation)
//                .build());
//        responseObserver.onCompleted();
//    }
//
//    // Delete reservation by ID
//    @Override
//    public void deleteReservation(ReservationOuterClass.DeleteReservationRequest request,
//                                  StreamObserver<ReservationOuterClass.DeleteReservationResponse> responseObserver) {
//        var reservationId = request.getId();
//        reservationService.deleteReservation(reservationId);
//
//        responseObserver.onNext(ReservationOuterClass.DeleteReservationResponse.newBuilder().build());
//        responseObserver.onCompleted();
//    }
//}
