package Project.LibrarySystem.Controller;

public class NoShowStrategy implements AssignmentStrategy {
    @Override
    public boolean canAssign(String studentId, String seatId) {
        // Check if student has recent no-show records
        // For now, simple mock implementation
        return true;
    }
}
