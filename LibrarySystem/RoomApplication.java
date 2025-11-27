package Project.LibrarySystem;

import java.time.LocalDateTime;

public class RoomApplication {
    public enum ApplicationStatus {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED,
        USING
    }

    private String applicationId;
    private String studentId;
    private String roomId;
    private String seatId;
    private ApplicationStatus status;
    private LocalDateTime timestamp;

    public RoomApplication(String applicationId, String studentId, String roomId, String seatId) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.roomId = roomId;
        this.seatId = seatId;
        this.status = ApplicationStatus.WAITING;
        this.timestamp = LocalDateTime.now();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getSeatId() {
        return seatId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public void cancel() {
        this.status = ApplicationStatus.CANCELED;
    }
}
