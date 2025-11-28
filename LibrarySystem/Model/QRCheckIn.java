package Project.LibrarySystem.Model;

import java.time.LocalDateTime;

public class QRCheckIn {
    private String checkInId;
    private String usageId;
    private LocalDateTime timestamp;
    private String qrCode;

    public QRCheckIn(String checkInId, String usageId, String qrCode) {
        this.checkInId = checkInId;
        this.usageId = usageId;
        this.qrCode = qrCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getCheckInId() {
        return checkInId;
    }

    public String getUsageId() {
        return usageId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
