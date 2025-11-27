package Project.LibrarySystem;

import java.util.UUID;

public class QRCodeSystem {
    
    // Generate a unique QR code for a seat
    public String generateQRCode(String seatId) {
        // In a real system, this might include encryption or signing
        return "QR-" + seatId + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    // Validate the input QR code against the seat's expected QR code
    public boolean validateQRCode(String inputQr, String seatId) {
        // In a real system, this would decode and verify the QR token
        // For this skeleton, we assume the seat object holds the valid QR string
        // But since we don't have access to the Seat object here directly without passing it,
        // we might need to change the design or assume the caller passes the expected QR.
        
        // However, based on the plan: "validate if the input QR matches the seat"
        // Let's assume the inputQr must start with "QR-" + seatId
        return inputQr != null && inputQr.startsWith("QR-" + seatId);
    }
}
