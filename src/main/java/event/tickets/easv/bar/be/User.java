package event.tickets.easv.bar.be;

import event.tickets.easv.bar.bll.cryptographic.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Entity<User> {
    private int id;
    private String username, password, imageName;
    private List<Event> events = new ArrayList<>();

    public User(int id, String username, String imageName) {
        this.id = id;
        this.username = username;
        this.imageName = imageName;
    }

    public User(int id, String username, String password, String imageName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.imageName = imageName;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setHashedPassword(String str) {
        String salt = BCrypt.gensalt(10);
        this.password = BCrypt.hashpw(str, salt);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String imageName() {
        return imageName;
    }

    public int getID() {
        return id;
    }

    public List<Event> events() {
        return events;
    }

    @Override
    public void update(User updatedData) {
        this.username = updatedData.getUsername();
        this.password = updatedData.getPassword();
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setAssociations(List<?> associations) {
        if (associations.isEmpty()) return;

        Object first = associations.getFirst();
        if (first instanceof Event) {
            this.events = (List<Event>) associations;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id() != user.id()) return false;
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
