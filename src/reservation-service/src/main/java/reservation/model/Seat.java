package reservation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // attributes
    private int seatNumber;
    private String status;

    // constructors

    // JPA requires a no-argument constructor
    public Seat() {}

    // a standard constructor
    public Seat(int seatNumber, String status) {
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // getters and setters
    public Long getId() {
        return id;
    }
    public int getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
