package Project.LibrarySystem;

import java.util.List;
import java.util.ArrayList;

public class Librarian extends User {
    private String adminLevel;
    private List<ReadingRoom> managedRooms;

    public Librarian(String userId, String password, String name, String adminLevel) {
        super(userId, password, name);
        this.adminLevel = adminLevel;
        this.managedRooms = new ArrayList<>();
    }

    public String getAdminLevel() {
        return adminLevel;
    }
    
    public void registerReadingRoom(ReadingRoom room) {
        this.managedRooms.add(room);
    }
}
