package Project.LibrarySystem.Controller;

public class FifoStrategy implements AssignmentStrategy {
    @Override
    public boolean canAssign(String studentId, String seatId) {
        // First-In-First-Out: Always allow if seat is available (checked elsewhere)
        return true;
    }
}
