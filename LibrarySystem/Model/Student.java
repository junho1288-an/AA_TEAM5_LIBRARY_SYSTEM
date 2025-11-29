package Project.LibrarySystem.Model;

import java.util.List;
import java.util.ArrayList;

public class Student extends User {
    private String studentId;
    private String department;
    private List<Reservation> reservations;
    private List<Notification> notifications;
    private int usageCount;
    private int noShowCount;
    private List<java.time.LocalDateTime> noShowHistory;

    public Student(String userId, String password, String name, String studentId, String department) {
        super(userId, password, name);
        this.studentId = studentId;
        this.department = department;
        this.reservations = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.usageCount = 0;
        this.noShowCount = 0;
        this.noShowHistory = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }

    public int getNoShowCount() {
        return noShowCount;
    }

    public void incrementNoShowCount() {
        this.noShowCount++;
        this.noShowHistory.add(java.time.LocalDateTime.now());
    }

    public List<java.time.LocalDateTime> getNoShowHistory() {
        return noShowHistory;
    }
}
