package ma.ensa.full_backend.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.ensa.full_backend.stubs.CreateReservationRequest;
import ma.ensa.full_backend.stubs.CreateReservationResponse;
import ma.ensa.full_backend.stubs.ReservationServiceGrpc;

public class GrpcClient {
    public static void main(String[] args) {
        // Create channel and stub
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                                                      .usePlaintext()
                                                      .build();
        ReservationServiceGrpc.ReservationServiceBlockingStub stub = ReservationServiceGrpc.newBlockingStub(channel);

        // Build request
        CreateReservationRequest request = CreateReservationRequest.newBuilder()
            .setClientId(1)
            .setCheckInDate("2024-12-12")
            .setCheckOutDate("2024-12-26")
            .setTypeChambre("STANDARD")
            .build();

        // Call the service
        System.out.println("Sending request with clientId: " + request.getClientId());
        CreateReservationResponse response = stub.createReservation(request);
        System.out.println("Response: " + response.getMessage());

        // Close the channel
        channel.shutdown();
    }
}
