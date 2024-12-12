package ma.ensa.full_backend.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import ma.ensa.full_backend.model.Client;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public TypeChambre getTypeChambre() {
		return typeChambre;
	}

	public void setTypeChambre(TypeChambre typeChambre) {
		this.typeChambre = typeChambre;
	}
    


    // Getters and Setters

}