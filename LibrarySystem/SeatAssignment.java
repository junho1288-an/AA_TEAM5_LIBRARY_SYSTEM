package Project.LibrarySystem;

import java.time.LocalDateTime;

public class SeatAssignment {
    public enum AssignmentStatus {
        ASSIGNED,
        OCCUPIED,
        AWAY,
        EXPIRED,
        ENDED
    }

    private String assignmentId;
    private String applicationId;
    private String seatId;
    private String studentId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AssignmentStatus status;

    public SeatAssignment(String assignmentId, String applicationId, String seatId, String studentId) {
        this.assignmentId = assignmentId;
        this.applicationId = applicationId;
        this.seatId = seatId;
        this.studentId = studentId;
        this.startTime = LocalDateTime.now();
        this.status = AssignmentStatus.ASSIGNED;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getSeatId() {
        return seatId;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }
    
    public void endAssignment() {
        this.endTime = LocalDateTime.now();
        this.status = AssignmentStatus.ENDED;
    }
}
