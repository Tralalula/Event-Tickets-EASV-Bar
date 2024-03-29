package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBJunctionDAOHelper<A extends Entity<A>, B extends Entity<B>> implements EntityAssociation<A, B> {
    private DBConnector dbConnector = null;
    private final AssociationSQLTemplate<A, B> sqlTemplate;
    private final AssociationInsertParameterSetter<A, B> insertParameterSetter;
    private final ResultSetMapper<A> resultSetMapperA;
    private final ResultSetMapper<B> resultSetMapperB;

    public DBJunctionDAOHelper(AssociationSQLTemplate<A, B> sqlTemplate,
                               AssociationInsertParameterSetter<A, B> insertParameterSetter,
                               ResultSetMapper<A> resultSetMapperA,
                               ResultSetMapper<B> resultSetMapperB) {
        this.sqlTemplate = sqlTemplate;
        this.insertParameterSetter = insertParameterSetter;
        this.resultSetMapperA = resultSetMapperA;
        this.resultSetMapperB = resultSetMapperB;
    }

    @Override
    public Result<Boolean> addAssociation(A entityA, B entityB) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sqlTemplate.insertRelationSQL())) {
            insertParameterSetter.setParameters(stmt, entityA, entityB);
            int rowsAffected = stmt.executeUpdate();
            return Success.of(rowsAffected > 0);
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "Failed to access the database", e);
        }
    }

    @Override
    public Result<Boolean> removeAssociation(A entityA, B entityB) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sqlTemplate.deleteRelationSQL())) {
            insertParameterSetter.setParameters(stmt, entityA, entityB);
            int rowsAffected = stmt.executeUpdate();
            return Success.of(rowsAffected > 0);
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "Failed to access the database", e);
        }
    }

    @Override
    public Result<List<B>> findAssociatesOfA(A entityA) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "Failed to read from the data source", e);
        }

        List<B> results = new ArrayList<>();
        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sqlTemplate.selectBForASQL())) {
            stmt.setInt(1, entityA.id());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(resultSetMapperB.map(rs));
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "Failed to access the database", e);
        }

        return Success.of(results);
    }

    @Override
    public Result<List<A>> findAssociatesOfB(B entityB) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "Failed to read from the data source", e);
        }

        List<A> results = new ArrayList<>();
        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sqlTemplate.selectAForBSQL())) {
            stmt.setInt(1, entityB.id());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(resultSetMapperA.map(rs));
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "Failed to access the database", e);
        }

        return Success.of(results);
    }

    @Override
    public Result<Boolean> deleteAssociationsFor(Object entity) {
        return null;
    }

    /**
     * Should be called internally by every method that tries to perform database operations.
     */
    private void setupDBConnector() throws IOException {
        if (dbConnector == null) {
            dbConnector = new DBConnector();
        }
    }

    /**
     * To set DBConnector for testing purposes.
     */
    void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }
}