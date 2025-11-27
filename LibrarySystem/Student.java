package Project.LibrarySystem;

import java.util.List;
import java.util.ArrayList;

public class Student extends User {
    private String studentId;
    private String department;
    private List<RoomApplication> applications;
    private List<Notification> notifications;

    public Student(String userId, String password, String name, String studentId, String department) {
        super(userId, password, name);
        this.studentId = studentId;
        this.department = department;
        this.applications = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void addApplication(RoomApplication application) {
        this.applications.add(application);
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }
}
