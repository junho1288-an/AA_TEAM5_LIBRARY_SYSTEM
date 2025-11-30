package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.Reservation;
import Project.LibrarySystem.Model.Reservation.ReservationStatus;
import java.util.Map;
import java.util.HashMap;

import Project.LibrarySystem.Model.AssignmentAlgorithm;

public class ReservationManager {
    private Map<String, Reservation> reservations;
    private AssignmentStrategy assignmentStrategy;
    private AssignmentAlgorithm currentAlgorithm;

    public ReservationManager() {
        this.reservations = new HashMap<>();
        // Default strategy
        setAssignmentAlgorithm(AssignmentAlgorithm.FIFO);
    }
    
    public void setAssignmentAlgorithm(AssignmentAlgorithm algorithm) {
        this.currentAlgorithm = algorithm;
        switch (algorithm) {
            case FIFO:
                this.assignmentStrategy = new FifoStrategy();
                break;
            case FAIR_USAGE:
                this.assignmentStrategy = new FairUsageStrategy();
                break;
            case NOSHOW:
                this.assignmentStrategy = new NoShowStrategy();
                break;
        }
    }

    public Integer countActiveReservation(String studentId) {
        return 0;
    }
    
    public AssignmentAlgorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }

    public Reservation createReservation(String studentId, String roomId) {
        if (!assignmentStrategy.canAssign(studentId)) {
            return null; // Or throw exception
        }
        
        String reservationId = "RES-" + System.currentTimeMillis();
        Reservation reservation = new Reservation(reservationId, studentId, roomId);
        reservations.put(reservationId, reservation);
        return reservation;
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null) {
            reservation.cancel();
        }
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    public List<Reservation> getReservationByStudentId(String studentId) {
        return reservations.get(studentId);
    }

    public Reservation findReservationByStudentId(String studentId) {
        for (Reservation reservation : reservations.values()) {
            if (reservation.getStudentId().equals(studentId)) {
                return reservation;
            }
        }
        return null;
    }
}
