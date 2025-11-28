package Project.LibrarySystem.Model;

import java.time.LocalDateTime;

public class SeatUsage {
    public enum UsageStatus {
        ASSIGNED,
        OCCUPIED,
        AWAY,
        EXPIRED,
        EMPTY,
        UNDETERMINED
    }

    private String usageId;
    private String reservationId;
    private String seatId;
    private String studentId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UsageStatus status;

    public SeatUsage(String usageId, String reservationId, String seatId, String studentId) {
        this.usageId = usageId;
        this.reservationId = reservationId;
        this.seatId = seatId;
        this.studentId = studentId;
        this.startTime = LocalDateTime.now();
        this.status = UsageStatus.ASSIGNED;
    }

    public String getUsageId() {
        return usageId;
    }

    public String getSeatId() {
        return seatId;
    }

    public String getStudentId() {
        return studentId;
    }

    public UsageStatus getStatus() {
        return status;
    }

    public void setStatus(UsageStatus status) {
        this.status = status;
    }
    
    public void endUsage() {
        this.endTime = LocalDateTime.now();
        this.status = UsageStatus.EMPTY;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
