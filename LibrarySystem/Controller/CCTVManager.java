package Project.LibrarySystem.Controller;

public class CCTVManager {
    private ReservationManager reservationManager;

    public CCTVManager(ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
    }
    
    // UC_04: Assign Seat (Triggered by CCTV event)
    public void onEmptySeatDetected(String seatId) {
        // Logic to notify LibraryService about empty seat
        // LibraryService should then assign to next waiting student
        if (reservationManager != null) {
            reservationManager.assignSeatToNextStudent(seatId);
        }
    }
    
    // Helper to simulate getting current occupancy status
    public boolean isSeatOccupied(String seatId) {
        // Return true if camera detects a person
        return false;
    }

    public boolean testConnection(String cctvInfo) {
        // Simulate connection test
        if (cctvInfo == null || cctvInfo.isEmpty()) {
            return false;
        }
        System.out.println("Testing connection to CCTV: " + cctvInfo);
        return true; // Simulate success
    }
}
