package Project.LibrarySystem.Model;

public class Seat {
    public enum SeatStatus {
        AVAILABLE,
        BOOKED,
        OCCUPIED,
        AWAY,
        UNAVAILABLE
    }

    private String seatId;
    private String roomId;
    private SeatStatus status;
    private String qrCode;
    private SeatUsage currentUsage;

    public Seat(String seatId, String roomId, String qrCode) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.qrCode = qrCode;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getSeatId() {
        return seatId;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }
    
    public void assign(SeatUsage usage) {
        this.currentUsage = usage;
        this.status = SeatStatus.BOOKED;
    }
    
    public SeatUsage getCurrentUsage() {
        return currentUsage;
    }
    
    public boolean validateQr(String inputQr) {
        return this.qrCode.equals(inputQr);
    }
}
