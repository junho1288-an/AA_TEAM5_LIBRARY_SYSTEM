package Project.LibrarySystem.Controller;

import java.time.LocalDate;
import java.util.Map;

public class StatisticsManager {
    private RoomManager roomManager;
    private CCTVManager cctvManager;
    private UsageManager usageManager;

    // UC_09: Get Seat Utilization (24h)
    private void setSeatUtilization(String seatId, Double utilizationRate) {
        // 좌석 배정된 통계 데이터가 없으면 utilizationRate를 0으로 설정
        // 좌석 배정된 통계 데이터가 있으면 utilizationRate를 통계 데이터로 설정
    }
    
    public StatisticsManager(RoomManager roomManager, CCTVManager cctvManager, UsageManager usageManager) {
        this.roomManager = roomManager;
        this.cctvManager = cctvManager;
        this.usageManager = usageManager;
    }

    // UC_08: Get Reading Room Utilization
    public Map<String, Double> getReadingRoomUtilization(String roomId, LocalDate startDate, LocalDate endDate) {
        // 1. Get ReadingRoom
        Project.LibrarySystem.Model.ReadingRoom room = roomManager.getReadingRoom(roomId);
        if (room == null) return new java.util.HashMap<>();
        
        // 2. Get Statistics
        java.util.List<Project.LibrarySystem.Model.SeatStatistic> stats = room.getStatistics();
        
        // 3. Filter by date range and aggregate
        Map<LocalDate, java.util.List<Double>> dailyRates = filterStatisticsByDate(stats, startDate, endDate);
        
        // 4. Calculate average per day
        return calculateDailyAverages(dailyRates);
    }

    private Map<LocalDate, java.util.List<Double>> filterStatisticsByDate(java.util.List<Project.LibrarySystem.Model.SeatStatistic> stats, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, java.util.List<Double>> dailyRates = new java.util.HashMap<>();
        for (Project.LibrarySystem.Model.SeatStatistic stat : stats) {
            if (!stat.getDate().isBefore(startDate) && !stat.getDate().isAfter(endDate)) {
                dailyRates.computeIfAbsent(stat.getDate(), k -> new java.util.ArrayList<>()).add(stat.getUtilizationRate());
            }
        }
        return dailyRates;
    }

    private Map<String, Double> calculateDailyAverages(Map<LocalDate, java.util.List<Double>> dailyRates) {
        Map<String, Double> result = new java.util.HashMap<>();
        for (Map.Entry<LocalDate, java.util.List<Double>> entry : dailyRates.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            result.put(entry.getKey().toString(), avg);
        }
        return result;
    }

    // UC_09: Get Seat Utilization (24h)
    public Map<String, Double> getSeatUtilization(String roomId, LocalDate date) {
        // 1. Get all seats
        // List<Seat> seats = roomManager.getReadingRoom(roomId).getSeats();
        // 2. For each seat, get usages
        // for (Seat seat : seats) {
        //     List<SeatUsage> usages = usageManager.getUsages(seat.getSeatId(), date);
        //     rate = calcSeatUtilization(usages.getStartTime(), usages.getEndTime());
        // }
        return null;
    }
    
    // UC_10: Get Seat Status Detail
    public Map<String, String> getSeatStatusDetail(String roomId) {
        // 1. Get all seats in the room
        // List<Seat> seats = roomManager.getReadingRoom(roomId).getSeats();
        // 2. Iterate and check status
        // for (Seat seat : seats) {
        //     SeatUsage usage = seat.getCurrentUsage();
        //     boolean isOccupied = cctvManager.isSeatOccupied(seat.getSeatId());
        //     // Apply rules:
        //     // if (usage != null && usage.getStatus() == OCCUPIED && isOccupied) -> USING
        //     // if (usage != null && !isOccupied) -> AWAY
        //     // if (usage == null && !isOccupied) -> EMPTY
        // }
        return null;
    }
}
