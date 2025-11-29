package Project.LibrarySystem.Model;

import java.time.LocalDateTime;

public class Reservation {
    public enum ReservationStatus {
        WAITING,
        PENDING,
        APPROVED,
        REJECTED,
        CANCELED,
        NOSHOW,
        USING
    }

    private String reservationId;
    private String studentId;
    private String roomId;
    private String seatId;
    private ReservationStatus status;
    private LocalDateTime timestamp;

    public Reservation(String reservationId, String studentId, String roomId, String seatId) {
        this.reservationId = reservationId;
        this.studentId = studentId;
        this.roomId = roomId;
        this.seatId = seatId;
        this.status = ReservationStatus.WAITING;
        this.timestamp = LocalDateTime.now();
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getSeatId() {
        return seatId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }
}
