package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.be.enums.Language;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.be.enums.Theme;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {
    private final DBDaoHelper<User> daoHelper;

    public UserDAO() {
        this.daoHelper = new DBDaoHelper<>(
                new UserSQLTemplate(),
                new UserResultSetMapper(),
                new UserInsertParameterSetter(),
                new UserUpdateParameterSetter(),
                new UserIdSetter(),
                List.of(new EventUserDAO())
        );
    }

    @Override
    public Result<Optional<User>> get(int id) {
        return daoHelper.get(id);
    }

    @Override
    public Result<List<User>> all() {
        return daoHelper.all();
    }

    @Override
    public Result<User> add(User entity) {
        return daoHelper.add(entity);
    }

    @Override
    public Result<Boolean> update(User original, User updatedData) {
        return daoHelper.update(original, updatedData);
    }

    @Override
    public Result<Boolean> delete(User entity) {
        return daoHelper.delete(entity);
    }
}

class UserSQLTemplate implements SQLTemplate<User> {
    @Override
    public String getSelectSQL() {
        return "SELECT id, username, mail, firstName, lastName, location, phoneNumber, imageName, rank, theme, language, fontSize FROM dbo.Users WHERE id = ?";
    }

    @Override
    public String allSelectSQL() {
        return "SELECT id, username, mail, firstName, lastName, location, phoneNumber, imageName, rank, theme, language, fontSize FROM dbo.Users";
    }

    @Override
    public String insertSQL() {
        return """
               INSERT INTO dbo.Users (username, mail, password, firstName, lastName, location, phoneNumber, rank)
               VALUES (?, ?, ?, ?, ?, ?, ?, ?);
               """;
    }

    @Override
    public String updateSQL() {
        return """
               UPDATE dbo.Users
               SET username = ?, mail = ?, password = ?, firstName = ?, lastName = ?, location = ?, phoneNumber = ?, imageName = ?, rank = ?, theme = ?, language = ?, fontSize = ?
               WHERE id = ?;
               """;
    }

    @Override
    public String deleteSQL() {
        return "DELETE FROM dbo.Users WHERE id = ?;";
    }
}

class UserResultSetMapper implements ResultSetMapper<User> {
    @Override
    public User map(@NotNull ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String mail = rs.getString("mail");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        String location = rs.getString("location");
        String phoneNumber = rs.getString("phoneNumber");
        String imageName = rs.getString("imageName");
        Rank rank = Rank.fromDbValue(rs.getString("rank"));
        Theme theme = Theme.fromDbValue(rs.getString("theme"));
        Language language = Language.fromDbValue(rs.getString("language"));
        int fontSize = rs.getInt("fontSize");

        return new User(id, username, mail, firstName, lastName, location, phoneNumber, imageName, rank, theme, language, fontSize);
    }
}

class UserInsertParameterSetter implements InsertParameterSetter<User> {
    @Override
    public void setParameters(PreparedStatement stmt, User entity) throws SQLException {
        stmt.setString(1, entity.username());
        stmt.setString(2, entity.mail());
        stmt.setString(3, entity.hashedPassword());
        stmt.setString(4, entity.firstName());
        stmt.setString(5, entity.lastName());
        stmt.setString(6, entity.location());
        stmt.setString(7, entity.phoneNumber());
        stmt.setString(8, entity.rank().toDbValue());
    }
}

class UserUpdateParameterSetter implements UpdateParameterSetter<User> {
    @Override
    public void setParameters(PreparedStatement stmt, User original, User updatedData) throws SQLException {
        stmt.setString(1, updatedData.username());
        stmt.setString(2, updatedData.mail());
        stmt.setString(3, updatedData.hashedPassword());
        stmt.setString(4, updatedData.firstName());
        stmt.setString(5, updatedData.lastName());
        stmt.setString(6, updatedData.location());
        stmt.setString(7, updatedData.phoneNumber());
        stmt.setString(8, updatedData.imageName());
        stmt.setString(9, updatedData.rank().toDbValue());
        stmt.setString(10, updatedData.theme().toDbValue());
        stmt.setString(11, updatedData.language().toDbValue());
        stmt.setInt(12, updatedData.fontSize());
        stmt.setInt(13, original.id());
    }
}

class UserIdSetter implements IdSetter<User> {
    @Override
    public User setId(User entity, int id) {
        return new User(id, entity);
    }
}