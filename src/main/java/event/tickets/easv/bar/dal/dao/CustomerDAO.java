package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Customer;
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

public class CustomerDAO implements DAO<Customer> {
    private final DBDaoHelper<Customer> daoHelper;

    public CustomerDAO() {
        this.daoHelper = new DBDaoHelper<>(
                new CustomerSQLTemplate(),
                new CustomerResultSetMapper(),
                new CustomerInsertParameterSetter(),
                new CustomerUpdateParameterSetter(),
                new CustomerIdSetter(),
                List.of(new EventUserDAO())
        );
    }

    @Override
    public Result<Optional<Customer>> get(int id) {
        return daoHelper.get(id);
    }

    @Override
    public Result<List<Customer>> all() {
        return daoHelper.all();
    }

    @Override
    public Result<Customer> add(Customer entity) {
        return daoHelper.add(entity);
    }

    @Override
    public Result<List<Customer>> addAll(List<Customer> entities) {
        return daoHelper.addAll(entities);
    }

    @Override
    public Result<Integer> batchAdd(List<Customer> entities) {
        return daoHelper.batchAdd(entities);
    }

    @Override
    public Result<Boolean> update(Customer original, Customer updatedData) {
        return daoHelper.update(original, updatedData);
    }

    @Override
    public Result<Boolean> delete(Customer entity) {
        return daoHelper.delete(entity);
    }
}

class CustomerSQLTemplate implements SQLTemplate<Customer> {
    @Override
    public String getSelectSQL() {
        return "SELECT id, mail FROM dbo.Customers WHERE mail = ?";
    }

    @Override
    public String allSelectSQL() {
        return "SELECT id, mail FROM dbo.Customers";
    }

    @Override
    public String insertSQL() {
        return """
               INSERT INTO dbo.Customers (mail)
               VALUES (?);
               """;
    }

    @Override
    public String updateSQL() {
        return """
               UPDATE dbo.Customers
               SET mail = ?
               WHERE id = ?;
               """;
    }

    @Override
    public String deleteSQL() {
        return "DELETE FROM dbo.Customers WHERE id = ?;";
    }
}

class CustomerResultSetMapper implements ResultSetMapper<Customer> {
    @Override
    public Customer map(@NotNull ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");

        return new Customer(id, email);
    }
}

class CustomerInsertParameterSetter implements InsertParameterSetter<Customer> {
    @Override
    public void setParameters(PreparedStatement stmt, Customer entity) throws SQLException {
        stmt.setString(1, entity.mail());
    }
}

class CustomerUpdateParameterSetter implements UpdateParameterSetter<Customer> {
    @Override
    public void setParameters(PreparedStatement stmt, Customer original, Customer updatedData) throws SQLException {
        stmt.setInt(1, updatedData.id());
        stmt.setString(2, updatedData.mail());
    }
}

class CustomerIdSetter implements IdSetter<Customer> {
    @Override
    public Customer setId(Customer entity, int id) {
        return new Customer(id, entity);
    }
}