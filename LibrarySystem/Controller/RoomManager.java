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

    public String registerReadingRoom(String adminId, String authToken, ReadingRoom room, String cctvInfo) {
        // 0. Validate Admin
        if (!authService.isLibrarian(adminId, authToken)) {
            throw new SecurityException("Access Denied: Only librarians can register reading rooms.");
        }

        // 1. Validate Input
        validateRoom(room);
        
        // 2. Test CCTV Connection
        if (!cctvManager.testConnection(cctvInfo)) {
            throw new IllegalArgumentException("CCTV connection failed. Please check CCTV info.");
        }
        
        // 3. Generate ID
        String roomId = "ROOM_" + (readingRooms.size() + 1);
        
        // 4. Create and Store
        ReadingRoom newRoom = new ReadingRoom(roomId, name, location, totalSeats, operatingHours);
        readingRooms.put(roomId, newRoom);
        
        System.out.println("Reading Room Registered: " + name + " (ID: " + roomId + ")");
        return roomId;
    }

    private void validateRoom(ReadingRoom room) {
        if (room.getName() == null || room.getName().isEmpty()) {
            throw new IllegalArgumentException("Reading room name cannot be empty.");
        }
        if (room.getTotalSeats() <= 0) {
            throw new IllegalArgumentException("Total seats must be greater than 0.");
        }
        
        // Check for duplicates
        for (ReadingRoom existingRoom : readingRooms.values()) {
            if (existingRoom.getName().equals(room.getName())) {
                throw new IllegalArgumentException("Reading room name already exists.");
            }
        }
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
