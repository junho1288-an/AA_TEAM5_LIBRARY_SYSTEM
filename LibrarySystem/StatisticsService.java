package Project.LibrarySystem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    
    // UC_08: Get Reading Room Utilization
    public Map<String, Double> getReadingRoomUtilization(String roomId, LocalDate startDate, LocalDate endDate) {
        // Logic to calculate utilization rate for a reading room over a period
        // Returns map of Date -> Utilization %
        return null;
    }

    // UC_09: Get Seat Utilization (24h)
    public Map<String, Double> getSeatUtilization(String roomId, LocalDate date) {
        // Logic to calculate utilization for each seat in a room for a specific date
        // Returns map of SeatId -> Utilization %
        return null;
    }
    
    // UC_10: Get Seat Status Detail
    public Map<String, String> getSeatStatusDetail(String roomId) {
        // Logic to get current detailed status of all seats in a room
        // Returns map of SeatId -> Status Description (Occupied, Away, Empty)
        return null;
    }
}
