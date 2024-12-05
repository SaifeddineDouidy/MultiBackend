package ma.ensa.full_backend.soap;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import ma.ensa.full_backend.model.Reservation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateReservationRequest", propOrder = {"reservation"})
public class CreateReservationRequest {

    @XmlElement(required = true)
    private Reservation reservation; // This is your Reservation entity class

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
