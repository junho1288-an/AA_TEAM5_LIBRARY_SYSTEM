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
}
