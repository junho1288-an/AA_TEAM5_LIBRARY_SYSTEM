package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.ReadingRoom;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RoomManager {
    private Map<String, ReadingRoom> readingRooms;
    private CCTVManager cctvManager;
    private AuthService authService;

    public RoomManager(CCTVManager cctvManager, AuthService authService) {
        this.readingRooms = new HashMap<>();
        this.cctvManager = cctvManager;
        this.authService = authService;
    }
    
    // Default constructor for backward compatibility (if needed, but better to enforce dependency)
    public RoomManager() {
        this(new CCTVManager(), new AuthService(new Project.LibrarySystem.Controller.UserManager()));
    }

    public String registerReadingRoom(ReadingRoom room, String cctvInfo) {

        // 1. Validate Input
        if (!validateRoomData(room)) {
            throw new IllegalArgumentException("Invalid reading room input.");
        }
        
        // 2. Test CCTV Connection
        if (!cctvManager.testConnection(cctvInfo)) {
            throw new IllegalArgumentException("CCTV connection failed. Please check CCTV info.");
        }
        
        // 3. Generate ID
        String roomId = "ROOM_" + (readingRooms.size() + 1);
        
        // 4. Create and Store
        ReadingRoom newRoom = new ReadingRoom(roomId, room.getName(), room.getLocation(), room.getTotalSeats(), room.getOperatingHours());
        updateReadingRoomList(newRoom);
        
        System.out.println("Reading Room Registered: " + newRoom.getName() + " (ID: " + roomId + ")");
        return roomId;
    }

    private boolean validateRoomData(ReadingRoom room) {
        if (room.getName() == null || room.getName().isEmpty()) {
            return false;
        }
        if (room.getTotalSeats() <= 0) {
            return false;
        }
        
        // Check for duplicates
        for (ReadingRoom existingRoom : readingRooms.values()) {
            if (existingRoom.getName().equals(room.getName())) {
                return false;
            }
        }
        return true;
    }

    public void updateReadingRoomList(ReadingRoom room) {
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
