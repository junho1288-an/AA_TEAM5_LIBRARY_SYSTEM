package Project.LibrarySystem.Model;

public abstract class User {
    protected String userId;
    protected String password;
    protected String name;

    public User(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
    
    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
