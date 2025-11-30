package Project.LibrarySystem.Model;

import java.util.List;
import java.util.ArrayList;

public class ReadingRoom {
    private String roomId;
    private String name;
    private String location;
    private int totalSeats;
    private List<Seat> seats;
    private List<SeatStatistic> statistics;
    private String operatingHours;

    public ReadingRoom(String roomId, String name, String location, int totalSeats, String operatingHours) {
        this.roomId = roomId;
        this.name = name;
        this.location = location;
        this.totalSeats = totalSeats;
        this.operatingHours = operatingHours;
        this.seats = new ArrayList<>();
        this.statistics = new ArrayList<>();
    }

    public String getRoomId() {
        return roomId;
    }

    public ReadingRoom getBriefInfo() {
        return null;
    }

    public ReadingRoom getDetailfInfo() {
        return null;
    }

    public String getName() {
        return name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }
    
    public void addStatistic(SeatStatistic statistic) {
        this.statistics.add(statistic);
    }
    
    public List<SeatStatistic> getStatistics() {
        return statistics;
    }
    
    public int getAvailableSeatCount() {
        int count = 0;
        for (Seat seat : seats) {
            if (seat.getStatus() == Seat.SeatStatus.AVAILABLE) {
                count++;
            }
        }
        return count;
    }
}
