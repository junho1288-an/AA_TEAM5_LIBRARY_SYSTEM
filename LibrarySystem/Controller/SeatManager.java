package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.Seat;
import Project.LibrarySystem.Model.Seat.SeatStatus;
import Project.LibrarySystem.Model.SeatUsage;

public class SeatManager {
    
    public void updateSeatStatus(Seat seat, SeatStatus status) {
        seat.setStatus(status);
    }

    public void assignSeat(Seat seat, SeatUsage usage) {
        seat.assign(usage);
    }

    public boolean isSeatAvailable(Seat seat) {
        return seat.getStatus() == SeatStatus.AVAILABLE;
    }

    public boolean SeatAvailable(Seat seat) {
        return seat.getStatus() == SeatStatus.AVAILABLE;
    }
}
