package Project.LibrarySystem;

import java.time.LocalDateTime;

public class QRCheckIn {
    private String checkInId;
    private String assignmentId;
    private LocalDateTime timestamp;
    private String qrCode;

    public QRCheckIn(String checkInId, String assignmentId, String qrCode) {
        this.checkInId = checkInId;
        this.assignmentId = assignmentId;
        this.qrCode = qrCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getCheckInId() {
        return checkInId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
