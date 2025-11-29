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

    private LocalDate selectedStartDate;
    private LocalDate selectedEndDate;
    private String selectedRoomId;

    public void selectPeriod(LocalDate startDate, LocalDate endDate) {
        this.selectedStartDate = startDate;
        this.selectedEndDate = endDate;
    }

    public void selectReadingRoom(String roomId) {
        this.selectedRoomId = roomId;
    }

    /**
     * UC_08: 열람실별 좌석 활용률 조회
     * 
     * Main Success Scenario:
     * 1. 도서관 관리자는 조회하고 싶은 기간을 선택한다. (selectPeriod)
     * 2. 도서관 관리자는 조회하고 싶은 열람실을 선택한다. (selectReadingRoom)
     * 3. 도서관 관리자는 선택된 기간과 열람실에 관한 좌석 활용율을 조회한다. (getReadingRoomUtilization)
     * 4. 시스템은 열람실별 좌석 활용률을 관리자 화면에 표시한다. (Return Map)
     * 
     * Extension:
     * 1.a / 2.a 기간 또는 열람실이 선택되지 않은 상태에서 조회 요청한 경우
     *  - 시스템은 "조회 기간과 열람실을 먼저 선택해야 합니다."라는 오류 메시지를 표시하고 조회를 중단한다. (IllegalStateException 발생)
     * 
     * 3.a 선택된 기간에 해당 열람실의 좌석 활용률 통계 데이터가 하나도 없는 경우
     *  - 시스템은 선택된 기간의 각 날짜에 대해 열람실별 좌석 활용률을 0%로 설정한다.
     *  - 시스템은 "선택한 기간에 통계 데이터가 없어 0%로 표시됩니다."라는 안내 메시지와 함께 결과를 표시한다.
     * 
     * @throws IllegalStateException 기간 또는 열람실이 선택되지 않은 경우 발생
     */
    public Map<String, Double> getReadingRoomUtilization() {
        if (selectedRoomId == null || selectedStartDate == null || selectedEndDate == null) {
            throw new IllegalStateException("조회 기간과 열람실을 먼저 선택해야 합니다.");
        }
        
        // 1. Get ReadingRoom
        Project.LibrarySystem.Model.ReadingRoom room = roomManager.getReadingRoom(selectedRoomId);
        if (room == null) return new java.util.HashMap<>();
        
        // 2. Get Statistics
        java.util.List<Project.LibrarySystem.Model.SeatStatistic> stats = room.getStatistics();
        
        // 3. Filter by date range and aggregate
        Map<LocalDate, java.util.List<Double>> dailyRates = filterStatisticsByDate(stats, selectedStartDate, selectedEndDate);
        
        // 4. Calculate average per day
        return calculateDailyAverages(dailyRates, selectedStartDate, selectedEndDate);
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

    private Map<String, Double> calculateDailyAverages(Map<LocalDate, java.util.List<Double>> dailyRates, LocalDate startDate, LocalDate endDate) {
        Map<String, Double> result = new java.util.HashMap<>();
        boolean hasData = false;
        
        // Iterate from startDate to endDate
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (dailyRates.containsKey(date)) {
                double avg = dailyRates.get(date).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                result.put(date.toString(), avg);
                hasData = true;
            } else {
                // Extension 3.a: If no data, calculate as 0%
                result.put(date.toString(), 0.0);
            }
        }
        
        if (!hasData) {
            System.out.println("선택한 기간에 통계 데이터가 없어 0%로 표시됩니다.");
        }
        
        return result;
    }

    private String selectedSeatId;

    public void selectSeat(String seatId) {
        this.selectedSeatId = seatId;
    }

    // ... existing select methods ...

    /**
     * UC_09: 좌석별 활용률 조회 (기간 내 일자별 활용률)
     * 
     * Main Success Scenario:
     * 1. 도서관 관리자는 조회하고 싶은 기간(시작일,종료일)을 선택한다. (selectPeriod)
     * 2. 도서관 관리자는 조회하고 싶은 열람실을 선택한다. (selectReadingRoom)
     * 3. 도서관 관리자는 조회하고 싶은 좌석을 선택한다. (selectSeat)
     * 4. 도서관 관리자는 선택된 기간과 좌석에 관한 좌석 활용률 조회를 요청한다. (getSeatUtilization)
     * 5. 시스템은 선택된 기간과 좌석에 대한 활용률을 계산하여 날짜별 좌석 활용률을 도서관 관리자에게 전달한다.
     * 
     * Extension:
     * 1.a / 2.a 기간, 열람실 또는 좌석이 선택되지 않은 상태에서 조회 요청한 경우
     *  1) "조회 기간과 열람실, 좌석을 먼저 선택해야 합니다"라는 오류 메세지를 표시하고 조회를 중단한다. (IllegalStateException)
     * 
     * 3.b 선택된 기간에 해당 좌석별 활용률 통계 데이터가 전혀 존재하지 않는 경우
     *  1) 시스템은 좌석별 좌석 활용율을 0%로 계산한다.
     * 
     * @throws IllegalStateException 기간, 열람실, 좌석 중 하나라도 선택되지 않은 경우
     */
    public Map<String, Double> getSeatUtilization() {
        if (selectedRoomId == null || selectedSeatId == null || selectedStartDate == null || selectedEndDate == null) {
            throw new IllegalStateException("조회 기간과 열람실, 좌석을 먼저 선택해야 합니다.");
        }

        // 1. Get ReadingRoom
        Project.LibrarySystem.Model.ReadingRoom room = roomManager.getReadingRoom(selectedRoomId);
        if (room == null) return new java.util.HashMap<>();

        // 2. Get Statistics for the specific seat
        java.util.List<Project.LibrarySystem.Model.SeatStatistic> allStats = room.getStatistics();
        java.util.List<Project.LibrarySystem.Model.SeatStatistic> seatStats = new java.util.ArrayList<>();
        
        for (Project.LibrarySystem.Model.SeatStatistic stat : allStats) {
            if (stat.getSeatId().equals(selectedSeatId)) {
                seatStats.add(stat);
            }
        }

        // 3. Filter by date range and aggregate (reuse existing logic but for single seat)
        // Since it's a single seat, we can just map date -> utilization
        Map<String, Double> result = new java.util.HashMap<>();
        boolean hasData = false;

        for (LocalDate date = selectedStartDate; !date.isAfter(selectedEndDate); date = date.plusDays(1)) {
            Double utilization = 0.0;
            for (Project.LibrarySystem.Model.SeatStatistic stat : seatStats) {
                if (stat.getDate().equals(date)) {
                    utilization = stat.getUtilizationRate();
                    hasData = true;
                    break;
                }
            }
            result.put(date.toString(), utilization);
        }
        
        if (!hasData) {
            System.out.println("선택한 기간에 해당 좌석의 통계 데이터가 없어 0%로 표시됩니다.");
        }

        return result;
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
