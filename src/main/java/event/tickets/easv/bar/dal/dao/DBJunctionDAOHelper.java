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
    private final Class<A> classA;
    private final Class<B> classB;
    private final AssociationSQLTemplate<A, B> sqlTemplate;
    private final AssociationParameterSetter<A, B> insertParameterSetter;
    private final ResultSetMapper<A> resultSetMapperA;
    private final ResultSetMapper<B> resultSetMapperB;

    public DBJunctionDAOHelper(Class<A> classA, Class<B> classB,
                               AssociationSQLTemplate<A, B> sqlTemplate,
                               AssociationParameterSetter<A, B> insertParameterSetter,
                               ResultSetMapper<A> resultSetMapperA,
                               ResultSetMapper<B> resultSetMapperB) {
        this.classA = classA;
        this.classB = classB;
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
            return Failure.of(FailureType.IO_FAILURE, "DBJunctionDAOHelper.addAssociation() - Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sqlTemplate.insertRelationSQL())) {
            insertParameterSetter.setParameters(stmt, entityA, entityB);
            int rowsAffected = stmt.executeUpdate();
            return Success.of(rowsAffected > 0);
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBJunctionDAOHelper.addAssociation() - Failed to add association between: " + entityA.getClass().getName() + " and " + entityB.getClass().getName() , e);
        }
    }

    @Override
    public Result<Boolean> removeAssociation(A entityA, B entityB) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBJunctionDAOHelper.removeAssociation() - Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sqlTemplate.deleteRelationSQL())) {
            insertParameterSetter.setParameters(stmt, entityA, entityB);
            int rowsAffected = stmt.executeUpdate();
            return Success.of(rowsAffected > 0);
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBJunctionDAOHelper.removeAssociation() - Failed to remove association between: " + entityA.getClass().getName() + " and " + entityB.getClass().getName() , e);
        }
    }

    @Override
    public Result<List<B>> findAssociatesOfA(A entityA) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBJunctionDAOHelper.findAssociatesOfA() - Failed to read from the data source", e);
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
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBJunctionDAOHelper.findAssociatesOfA() - Failed to find associates of: " + entityA.getClass().getName(), e);
        }

        return Success.of(results);
    }

    @Override
    public Result<List<A>> findAssociatesOfB(B entityB) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBJunctionDAOHelper.findAssociatesOfB() - Failed to read from the data source", e);
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
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBJunctionDAOHelper.findAssociatesOfB() - Failed to find associates of: " + entityB.getClass().getName(), e);        }

        return Success.of(results);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<List<?>> findAssociatesOf(Entity<?> entity) {
        if (entity.getClass().equals(classA)) {
            return Success.of(findAssociatesOfA((A) entity).get());
        } else if (entity.getClass().equals(classB)) {
            return Success.of(findAssociatesOfB((B) entity).get());
        } else {
            return Failure.of(FailureType.INVALID_ENTITY_TYPE, "DBJunctionDAOHelper.findAssociatesOf() - Entity does not match expected types A or B: " + entity.getClass().getName());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<Boolean> deleteAssociationsFor(Entity<?> entity) {
        if (entity == null) throw new IllegalArgumentException("Entity must not be null");

        if (classA.isAssignableFrom(entity.getClass())) {
            A aEntity = (A) entity;
            return executeDelete(sqlTemplate.deleteAssociationsForASQL(), aEntity.id());
        } else if (classB.isAssignableFrom(entity.getClass())) {
            B bEntity = (B) entity;
            return executeDelete(sqlTemplate.deleteAssociationsForBSQL(), bEntity.id());
        } else {
            throw new IllegalArgumentException("Unexpected entity: " + entity);
        }
    }

    private Result<Boolean> executeDelete(String sql, int id) {
        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return Success.of(rowsAffected > 0);
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBJunctionDAOHelper.executeDelete() - failed to delete associations", e);
        }
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
