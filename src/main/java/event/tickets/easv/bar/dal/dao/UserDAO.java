package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {
    private final DBDaoHelper<User> daoHelper;

    public UserDAO(DBDaoHelper<User> daoHelper) {
        this.daoHelper = new DBDaoHelper<>(
                new UserSQLTemplate(),
                new UserResultSetMapper(),
                new UserInsertParameterSetter(),
                new UserUpdateParameterSetter(),
                new UserIdSetter(),
                List.of()
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
        return "SELECT * FROM dbo.Users WHERE id = ?";
    }

    @Override
    public String allSelectSQL() {
        return "SELECT * FROM dbo.Users";
    }

    @Override
    public String insertSQL() {
        return """
               INSERT INTO dbo.Users (username, password)
               VALUES (?, ?);
               """;
    }

    @Override
    public String updateSQL() {
        return """
               UPDATE dbo.Users
               SET username = ?, password = ?
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

        return new User(id, username);
    }
}

class UserInsertParameterSetter implements InsertParameterSetter<User> {
    @Override
    public void setParameters(PreparedStatement stmt, User entity) throws SQLException {
        stmt.setString(1, entity.getUsername());
        stmt.setString(2, entity.getPassword());
    }
}

class UserUpdateParameterSetter implements UpdateParameterSetter<User> {
    @Override
    public void setParameters(PreparedStatement stmt, User original, User updatedData) throws SQLException {
        stmt.setString(1, updatedData.getUsername());
        stmt.setString(2, updatedData.getPassword());
        stmt.setInt(3, original.id());
    }
}

class UserIdSetter implements IdSetter<User> {
    @Override
    public User setId(User entity, int id) {
        return new User(id, entity.getUsername());
    }
}