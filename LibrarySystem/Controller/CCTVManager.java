package Project.LibrarySystem.Controller;

public class CCTVManager {
    
    // UC_04: Assign Seat (Triggered by CCTV event)
    public void onEmptySeatDetected(String seatId) {
        // Logic to notify LibraryService about empty seat
        // LibraryService should then assign to next waiting student
    }
    
    // Helper to simulate getting current occupancy status
    public boolean isSeatOccupied(String seatId) {
        // Return true if camera detects a person
        return false;
    }
}
