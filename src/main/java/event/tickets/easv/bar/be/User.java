package event.tickets.easv.bar.be;

import event.tickets.easv.bar.bll.cryptographic.BCrypt;

public class User implements Entity<User> {
    private int id;
    private String username, password;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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

    public int getID() {
        return id;
    }

    @Override
    public void update(User updatedDate) {

    }

    @Override
    public int id() {
        return id;
    }
}
