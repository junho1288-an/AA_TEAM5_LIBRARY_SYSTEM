package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.Reservation;

public interface AssignmentStrategy {
    // Returns true if assignment is allowed, false otherwise
    // In a real scenario, this might return a score or priority
    boolean canAssign(String studentId, String seatId);
}
