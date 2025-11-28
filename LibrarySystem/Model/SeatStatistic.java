package Project.LibrarySystem.Model;

import java.time.LocalDate;

public class SeatStatistic {
    private String seatId;
    private LocalDate date;
    private long totalUsageTime; // in minutes
    private int usageCount;
    private double utilizationRate;

    public String getSeatId() {
        return seatId;
    }

    public LocalDate getDate() {
        return date;
    }

    public SeatStatistic(String seatId, LocalDate date) {
        this.seatId = seatId;
        this.date = date;
        this.totalUsageTime = 0;
        this.usageCount = 0;
        this.utilizationRate = 0.0;
    }

    public void addUsage(long durationMinutes) {
        this.totalUsageTime += durationMinutes;
        this.usageCount++;
        calculateUtilization();
    }

    private void calculateUtilization() {
        // Assuming 24 hours (1440 minutes) as base
        this.utilizationRate = (double) totalUsageTime / 1440.0 * 100.0;
    }

    public double getUtilizationRate() {
        return utilizationRate;
    }
}
