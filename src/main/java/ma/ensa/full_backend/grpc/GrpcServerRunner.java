//package ma.ensa.full_backend.grpc; // Utilisez votre package
//
//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GrpcServerRunner implements CommandLineRunner {
//
//    private final ReservationGrpcService reservationGrpcService;
//
//    @Autowired
//    public GrpcServerRunner(ReservationGrpcService reservationGrpcService) {
//        this.reservationGrpcService = reservationGrpcService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Créez le serveur gRPC sur le port 9090
//        Server server = ServerBuilder.forPort(9090)  // Assurez-vous que le port est correct
//                .addService(reservationGrpcService)  // Enregistrer le service
//                .build();
//
//        // Démarrer le serveur
//        server.start();
//        System.out.println("Serveur gRPC démarré sur le port 9090");
//
//        // Attendez la terminaison du serveur
//        server.awaitTermination();
//    }
//}
