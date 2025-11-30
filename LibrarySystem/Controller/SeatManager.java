package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.Seat;
import Project.LibrarySystem.Model.Seat.SeatStatus;
import Project.LibrarySystem.Model.SeatUsage;

public class SeatManager {
    private AuthService authService;
    private RoomManager roomManager;
    private ReservationManager reservationManager;

    public SeatManager(AuthService authService, RoomManager roomManager, ReservationManager reservationManager) {
        this.authService = authService;
        this.roomManager = roomManager;
        this.reservationManager = reservationManager;
    }
    
    public void updateSeatStatus(Seat seat, SeatStatus status) {
        seat.setStatus(status);
    }

    public void assignSeat(Seat seat) {
        seat.assign(usage);
    }

    public boolean hasFreeSeat(String readingroomId) {
        return null;
    }

    public boolean isSeatAvailable(Seat seat) {
        return seat.getStatus() == SeatStatus.AVAILABLE;
    }

    public String requestSeatStatus(String studentId, String authToken) {
        // 1. Validate User
        if (!authService.isAuthenticated(studentId)) { // Assuming authToken validation logic is handled or simplified here
             return "Error: User not authenticated.";
        }
        
        // In a real scenario, authToken should be validated against the session. 
        // Here we trust the studentId if the user is logged in (simplified).
        
        // 2. Find Seat
        for (Project.LibrarySystem.Model.ReadingRoom room : roomManager.getAllReadingRooms()) {
            for (Seat seat : room.getSeats()) {
                SeatUsage usage = seat.getCurrentUsage();
                if (usage != null && studentId.equals(usage.getStudentId()) && 
                    (usage.getStatus() == SeatUsage.UsageStatus.OCCUPIED || usage.getStatus() == SeatUsage.UsageStatus.OCCUPIED)) {
                    
                    return String.format("Seat Status: %s\nRoom: %s\nSeat: %s\nTime Remaining: %s", 
                        usage.getStatus(), room.getName(), seat.getSeatId(), "2 hours"); // Mock time
                }
            }
        }
        
        // 3. Check for Canceled Reservation
        Reservation reservation = reservationManager.findReservationByStudentId(studentId);
        if (reservation != null && reservation.getStatus() == Project.LibrarySystem.Model.Reservation.ReservationStatus.CANCELED) {
             return String.format("Seat Status: %s\nRoom: %s\nSeat: %s\nCanceled Time: %s", 
                        reservation.getStatus(), reservation.getRoomId(), reservation.getSeatId(), "Just now"); // Mock time
        }
        
        return "No active seat reservation found.";
    }
}
