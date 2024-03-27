package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.database.DBConnector;

import java.io.IOException;
import java.sql.*;

public class AuthDAO {

    private DBConnector databaseConnector;

    public AuthDAO() throws IOException {
        databaseConnector = new DBConnector();
    }

    public User getUser(User user) throws Exception {
        String sql = "SELECT id, username, password FROM dbo.users WHERE username = ?";

        try (Connection conn = databaseConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String usr = rs.getString("username");
                String pass = rs.getString("password");

                return new User(id, usr, pass);
            }
        }
        throw new Exception("User doesn't exist");
    }

    public boolean userExists(String username) throws Exception {
        String sql = "SELECT username FROM dbo.users WHERE username = ?";

        try (Connection conn = databaseConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String usr = rs.getString("username");
                return usr != null;
            }
        }
        throw new Exception("User doesn't exist");
    }

    public User createUser(User user) throws Exception {
        // SQL command
        String sql = "INSERT INTO dbo.users (username,password) VALUES (?,?);";

        //
        try (Connection conn = databaseConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next())
                id = rs.getInt(1);

            // Create user object and send up the layers
            User createdUser = new User(id, user.getUsername(), user.getPassword());
            return createdUser;
        } catch (SQLException ex) {
            throw new Exception("Could not create user", ex);
        }
    }
}