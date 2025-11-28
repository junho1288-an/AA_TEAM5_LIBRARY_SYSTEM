package Project.LibrarySystem.Controller;

public class FairUsageStrategy implements AssignmentStrategy {
    @Override
    public boolean canAssign(String studentId, String seatId) {
        // Check if student has exceeded usage limits or has lower priority
        // For now, simple mock implementation
        return true; 
    }
}
