package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Ticket;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.database.DBConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private DBConnector databaseConnector;

    public TicketDAO() throws IOException {
        this.databaseConnector = new DBConnector();
    }

    public Ticket getTicket(int id) throws SQLException {
        String sql = """
                SELECT T.*, TC.name AS categoryName
                FROM Ticket AS T
                INNER JOIN TicketCategory AS TC ON T.categoryId = TC.id
                INNER JOIN TicketCategory AS TC2 ON T.id = TC2.id
                WHERE T.id = ?;
            """;

        try (Connection conn = databaseConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int ticketId = rs.getInt("id");
                String title = rs.getString("title");
                String classification = rs.getString("classification");
                int categoryId = rs.getInt("categoryId");
                String categoryName = rs.getString("categoryName");

                return new Ticket(ticketId, title, classification, categoryId, categoryName);
            }
        }
        throw new SQLException("Error occurred");
    }

    public List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();

        String sql = """
                SELECT T.*, TC.name AS categoryName
                FROM Ticket AS T
                INNER JOIN TicketCategory AS TC ON T.categoryId = TC.id
                INNER JOIN TicketCategory AS TC2 ON T.id = TC2.id;
            """;

        try (Connection conn = databaseConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String classification = rs.getString("classification");
                int categoryId = rs.getInt("categoryId");
                String categoryName = rs.getString("categoryName");

                tickets.add(new Ticket(id, title, classification, categoryId, categoryName));
            }
        }
        return tickets;
    }
}
