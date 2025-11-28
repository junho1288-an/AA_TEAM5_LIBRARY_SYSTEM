package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.ReadingRoom;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RoomManager {
    private Map<String, ReadingRoom> readingRooms;

    public RoomManager() {
        this.readingRooms = new HashMap<>();
    }

    public String registerReadingRoom(String name, String location, int totalSeats, String operatingHours) {
        // 1. Validate Input
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Reading room name cannot be empty.");
        }
        if (totalSeats <= 0) {
            throw new IllegalArgumentException("Total seats must be greater than 0.");
        }
        
        // 2. Generate ID
        String roomId = "ROOM_" + (readingRooms.size() + 1);
        
        // 3. Create and Store
        ReadingRoom newRoom = new ReadingRoom(roomId, name, location, totalSeats, operatingHours);
        readingRooms.put(roomId, newRoom);
        
        System.out.println("Reading Room Registered: " + name + " (ID: " + roomId + ")");
        return roomId;
    }

    public void addReadingRoom(ReadingRoom room) {
        readingRooms.put(room.getRoomId(), room);
    }

    public ReadingRoom getReadingRoom(String roomId) {
        return readingRooms.get(roomId);
    }

    public List<ReadingRoom> searchReadingRooms(String name, String location) {
        return new ArrayList<>(readingRooms.values());
    }

    public List<ReadingRoom> getAllReadingRooms() {
        return new ArrayList<>(readingRooms.values());
    }

    public boolean hasFreeSeat() {
        return null;
    }
}
