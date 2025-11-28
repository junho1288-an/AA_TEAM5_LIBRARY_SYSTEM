package Project.LibrarySystem.Controller;

import Project.LibrarySystem.Model.User;
import Project.LibrarySystem.Model.Student;
import Project.LibrarySystem.Model.Librarian;
import java.util.Map;
import java.util.HashMap;

public class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public boolean login(String userId, String password) {
        User user = users.get(userId);
        if (user != null) {
            return user.login(password);
        }
        return false;
    }
}
