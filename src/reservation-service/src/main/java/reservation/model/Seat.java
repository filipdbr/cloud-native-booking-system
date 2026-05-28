package reservation.model;

public class Seat {

    // attributes
    private int seatNumber;
    private String status;

    // constructor
    public Seat(int seatNumber, String status) {
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // getters and setters
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
