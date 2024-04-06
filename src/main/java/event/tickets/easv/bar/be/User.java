package event.tickets.easv.bar.be;

import event.tickets.easv.bar.be.enums.Language;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.be.enums.Theme;
import event.tickets.easv.bar.bll.cryptographic.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Entity<User> {
    private int id;
    private String username;
    private String mail;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private String location;
    private String phoneNumber;
    private String imageName;
    private Rank rank = Rank.EVENT_COORDINATOR;
    private Theme theme = Theme.LIGHT;
    private Language language = Language.EN_GB;
    private int fontSize = 14;
    private List<Event> events = new ArrayList<>();

    public User(String username,
                String mail,
                String plainTextPassword,
                String firstName,
                String lastName,
                String location,
                String phoneNumber,
                Rank rank) {
        this.username = username;
        this.mail = mail;
        setHashedPassword(plainTextPassword);
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.rank = rank;
    }

    public User(int id,
                String username,
                String mail,
                String firstName,
                String lastName,
                String location,
                String phoneNumber,
                String imageName,
                Rank rank,
                Theme theme,
                Language language,
                int fontSize) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.imageName = imageName;
        this.rank = rank;
        this.theme = theme;
        this.language = language;
        this.fontSize = fontSize;
    }


    public User(int id, String username, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public User(int id, String username, String mail, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.hashedPassword = hashedPassword;
    }

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = password;
    }

    public User(int id, User user) {
        this(id, user.username(), user.mail(),
                user.firstName(), user.lastName(),
                user.location(), user.phoneNumber(),
                user.imageName(), user.rank(),
                user.theme(), user.language(), user.fontSize());
    }

    public void setHashedPassword(String str) {
        String salt = BCrypt.gensalt(10);
        this.hashedPassword = BCrypt.hashpw(str, salt);
    }

    public String username() {
        return username;
    }

    public String hashedPassword() {
        return hashedPassword;
    }

    public String imageName() {
        return imageName;
    }

    public int getID() {
        return id;
    }

    public String mail() {
        return mail;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String location() {
        return location;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public Rank rank() {
        return rank;
    }

    public Theme theme() {
        return theme;
    }

    public Language language() {
        return language;
    }

    public int fontSize() {
        return fontSize;
    }

    public List<Event> events() {
        return events;
    }

    @Override
    public void update(User updatedData) {
        this.username = updatedData.username();
        this.hashedPassword = updatedData.hashedPassword();
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
        return username().equals(user.username());
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
