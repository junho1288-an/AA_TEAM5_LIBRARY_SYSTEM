package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.User;
import Project.LibrarySystem.Model.Student;
import Project.LibrarySystem.Model.Librarian;
import java.util.HashSet;
import java.util.Set;

public class AuthService {
    private UserManager userManager;
    private Set<String> activeSessions; // Stores userIds of logged-in users

    public AuthService(UserManager userManager) {
        this.userManager = userManager;
        this.activeSessions = new HashSet<>();
    }

    public boolean login(String userId, String password) {
        if (userManager.login(userId, password)) {
            activeSessions.add(userId);
            System.out.println("User " + userId + " logged in successfully.");
            return true;
        }
        System.out.println("Login failed for user " + userId);
        return false;
    }

    public void logout(String userId) {
        if (activeSessions.contains(userId)) {
            activeSessions.remove(userId);
            System.out.println("User " + userId + " logged out.");
        }
    }

    public boolean isAuthenticated(String userId) {
        return activeSessions.contains(userId);
    }

    public boolean isStudent(String userId) {
        User user = userManager.getUser(userId);
        return user != null && user instanceof Student;
    }

    public boolean isLibrarian(String userId, String authToken) {
        if (!isAuthenticated(authToken)) {
            return false;
        }
        User user = userManager.getUser(userId);
        return user != null && user instanceof Librarian;
    }
    
    public User getCurrentUser(String userId) {
        if (isAuthenticated(userId)) {
            return userManager.getUser(userId);
        }
        return null;
    }
}
