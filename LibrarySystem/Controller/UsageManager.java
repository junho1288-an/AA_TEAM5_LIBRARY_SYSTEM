package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.SeatUsage;
import Project.LibrarySystem.Model.SeatUsage.UsageStatus;
import Project.LibrarySystem.Model.SeatStatistic;
import Project.LibrarySystem.Model.ReadingRoom;
import Project.LibrarySystem.Model.Seat;
import java.util.Map;
import java.util.HashMap;
import java.time.Duration;
import java.time.LocalDate;

public class UsageManager {
    private Map<String, SeatUsage> activeUsages;
    private RoomManager roomManager;

    public UsageManager(RoomManager roomManager) {
        this.activeUsages = new HashMap<>();
        this.roomManager = roomManager;
    }

    public SeatUsage startUsage(String reservationId, String seatId, String studentId) {
        String usageId = "USAGE-" + System.currentTimeMillis();
        SeatUsage usage = new SeatUsage(usageId, reservationId, seatId, studentId);
        activeUsages.put(usageId, usage);
        return usage;
    }

    public void endUsage(String usageId) {
        SeatUsage usage = activeUsages.get(usageId);
        if (usage != null) {
            usage.endUsage();
            
            // Calculate usage duration and update statistics
            if (usage.getStartTime() != null && usage.getEndTime() != null) {
                long durationMinutes = Duration.between(usage.getStartTime(), usage.getEndTime()).toMinutes();
                LocalDate usageDate = usage.getStartTime().toLocalDate();
                
                // Find the room containing this seat and add statistics
                // In a real app, we'd query the DB to find which room contains this seat
                // For now, we iterate through all rooms
                for (ReadingRoom room : roomManager.getAllReadingRooms()) {
                    for (Seat seat : room.getSeats()) {
                        if (seat.getSeatId().equals(usage.getSeatId())) {
                            // Find or create statistic for this seat and date
                            SeatStatistic statistic = null;
                            for (SeatStatistic s : room.getStatistics()) {
                                if (s.getSeatId().equals(usage.getSeatId()) && s.getDate().equals(usageDate)) {
                                    statistic = s;
                                    break;
                                }
                            }
                            
                            if (statistic == null) {
                                statistic = new SeatStatistic(usage.getSeatId(), usageDate);
                                room.addStatistic(statistic);
                            }
                            
                            statistic.addUsage(durationMinutes);
                            break;
                        }
                    }
                }
            }
            
            activeUsages.remove(usageId); // Move to history in real app
        }
    }
    
    // Helper for UC_09
    public java.util.List<SeatUsage> getUsages(String seatId, java.time.LocalDate date) {
        // In a real app, query DB for usages of seatId on date
        return new java.util.ArrayList<>();
    }
}
