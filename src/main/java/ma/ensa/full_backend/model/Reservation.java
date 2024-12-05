package ma.ensa.full_backend.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client; // Reference to a Client entity

    @Temporal(TemporalType.DATE)
    private Date checkInDate;
    @Temporal(TemporalType.DATE)
    private Date checkOutDate;

    @Enumerated(EnumType.STRING)
    private TypeChambre typeChambre; // Room type preference

    public Reservation(Long id,  Date checkInDate, Date checkOutDate, TypeChambre typeChambre,Client client) {
        this.id = id;
        this.client = client;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.typeChambre = typeChambre;
    }

    public Reservation() {

    }


    // Getters and Setters

}