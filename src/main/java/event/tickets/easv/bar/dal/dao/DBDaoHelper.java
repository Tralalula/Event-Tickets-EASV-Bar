package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Helper class providing generic data access operations against a database.
 */
public class DBDaoHelper<T extends Entity<T>> implements DAO<T> {
    private DBConnector dbConnector = null;
    private final SQLTemplate<T> sqlTemplate;
    private final ResultSetMapper<T> resultSetMapper;
    private final InsertParameterSetter<T> insertParameterSetter;
    private final UpdateParameterSetter<T> updateParameterSetter;
    private final IdSetter<T> idSetter;
    private final List<EntityAssociation<?, ?>> associations;

    /**
     * Constructs a new DBDaoHelper that provides helper methods for data access operations against a database.
     *
     * @param sqlTemplate the template for generic SQL queries.
     * @param resultSetMapper the mapper for converting ResultSet rows into entities of type T.
     */
    public DBDaoHelper(SQLTemplate<T> sqlTemplate,
                       ResultSetMapper<T> resultSetMapper,
                       InsertParameterSetter<T> insertParameterSetter,
                       UpdateParameterSetter<T> updateParameterSetter,
                       IdSetter<T> idSetter,
                       List<EntityAssociation<?, ?>> associations) {
        this.sqlTemplate = sqlTemplate;
        this.resultSetMapper = resultSetMapper;
        this.insertParameterSetter = insertParameterSetter;
        this.updateParameterSetter = updateParameterSetter;
        this.idSetter = idSetter;
        this.associations = associations;
    }

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the unique identifier of the entity to retrieve.
     * @return a result which is either a success or a failure if any issues occurred during data retrieval.
     *         The success contains optional which either contains the entity if found; otherwise an empty optional.
     */
    public Result<Optional<T>> get(Object id) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.get() - Failed to read from the data source", e);
        }

        String sql = sqlTemplate.getSelectSQL();
        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            switch (id) {
                case String s -> stmt.setString(1, s);
                case Integer i -> stmt.setInt(1, i);
                default -> {
                    return Failure.of(FailureType.INVALID_ENTITY_TYPE, "Unexpected entity type: " + id.getClass().getName());
                }
            }
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Success.of(Optional.of(resultSetMapper.map(rs)));
            } else {
                return Success.of(Optional.empty());
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.get() - Failed to retrieve data from the database", e);
        }
    }

    /**
     * Retrieves all entities of type T from the database.
     *
     * @return a {@code Result<List<T>>} which is either a Success containing the list of entities,
     *         or a Failure if any issues occur during data retrieval.
     */
    public Result<List<T>> all() {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.all() - Failed to read from the data source", e);
        }

        List<T> results = new ArrayList<>();
        String sql = sqlTemplate.allSelectSQL();

        try (Connection conn = dbConnector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(resultSetMapper.map(rs));
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.all() - Failed to retrieve all entities", e);
        }

        return Success.of(results);
    }

    @Override
    public Result<T> add(T entity) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.add() - Failed to read from the data source", e);
        }

        String sql = sqlTemplate.insertSQL();
        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            insertParameterSetter.setParameters(stmt, entity);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return Success.of(idSetter.setId(entity, id));
            } else {
                return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.add() - No ID generated for entity: " + entity.getClass().getName());
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.add() - Failed to add entity: " + entity.getClass().getName() + " to the database", e);
        }
    }

    @Override
    public Result<List<T>> addAll(List<T> entities) {
        // https://github.com/microsoft/mssql-jdbc/issues/245
        // Cannot retrieve all generated keys for batch execution
        // so this the solution for when adding multiple entries to DB
        // and wanting to retrieve back a list of entities with their ids in the db.
        // Works similarly to the basic add() method, just a transaction & for loop.
        if (entities == null || entities.isEmpty()) {
            return Success.of(Collections.emptyList());
        }

        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.addAll() - Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection()) {
            conn.setAutoCommit(false); // Start transaction

            List<T> addedEntities = new ArrayList<>();
            for (T entity : entities) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlTemplate.insertSQL(), Statement.RETURN_GENERATED_KEYS)) {
                    insertParameterSetter.setParameters(stmt, entity);
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        addedEntities.add(idSetter.setId(entity, id));
                    }
                }
            }

            conn.commit();
            return Success.of(addedEntities);
        } catch (SQLException e) {
            // todo: rollback mangler
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.addAll() - Failed to connect to database", e);
        }
    }

    @Override
    public Result<Integer> batchAdd(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return Success.of(0);
        }

        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.addAll() - Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sqlTemplate.insertSQL())) {
            conn.setAutoCommit(false); // start transaction

            for (T entity : entities) {
                insertParameterSetter.setParameters(stmt, entity);
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            int rowsAffected = Arrays.stream(results).sum();
            conn.commit(); // commit transaction
            return Success.of(rowsAffected);
        } catch (SQLException e) {
            // todo: rollback mangler
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.addAll() - Failed to connect to database", e);
        }
    }

    @Override
    public Result<Boolean> update(T original, T updatedData) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.update() - Failed to read from the data source", e);
        }

        String sql = sqlTemplate.updateSQL();
        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            updateParameterSetter.setParameters(stmt, original, updatedData);

            original.update(updatedData);
            int rowsAffected = stmt.executeUpdate();
            return Success.of(rowsAffected > 0);
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.update() - Failed to update entity: " + original.getClass().getName() + " in the database", e);
        }
    }

    @Override
    public Result<Boolean> delete(T entity) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.delete() - Failed to read from the data source", e);
        }

        String sql = sqlTemplate.deleteSQL();
        try (Connection conn = dbConnector.connection()) {
            conn.setAutoCommit(false); // start transaction

            for (EntityAssociation<?, ?> association : associations) {
                association.deleteAssociationsFor(entity);
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, entity.id());
                int rowsAffected = stmt.executeUpdate();

                conn.commit(); // commit transactions
                return Success.of(rowsAffected > 0);
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction if an error happens
                return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.delete() - Failed to delete entity: " + entity.getClass().getName(), e);
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.delete() - Failed to access the database: " + entity.getClass().getName(), e);
        }
    }

    public Result<Optional<T>> getEntityByField(Object id, String selectSQL, ResultSetMapper<T> resultSetMapper, int numParams) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.delete() - Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            for (int i = 1; i < numParams + 1; i++) {
                switch (id) {
                    case String s -> stmt.setString(i, s);
                    case Integer in -> stmt.setInt(i, in);
                    default -> {
                        return Failure.of(FailureType.INVALID_ENTITY_TYPE, "DBDaoHelper.getEntityByField() - Unexpected entity type: " + id.getClass().getName());
                    }
                }
            }

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Success.of(Optional.of(resultSetMapper.map(rs)));
            } else {
                return Success.of(Optional.empty());
            }
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.getEntityByField() - Failed to retrieve data from the database", e);
        }
    }

    public Result<Boolean> updateField(T original, T updatedData, String updateSQL, UpdateParameterSetter<T> updateParameterSetter) {
        try {
            setupDBConnector();
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "DBDaoHelper.updateEntityByField() - Failed to read from the data source", e);
        }

        try (Connection conn = dbConnector.connection();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            updateParameterSetter.setParameters(stmt, original, updatedData);

            original.update(updatedData);
            int rowsAffected = stmt.executeUpdate();
            return Success.of(rowsAffected > 0);
        } catch (SQLException e) {
            return Failure.of(FailureType.DB_DATA_RETRIEVAL_FAILURE, "DBDaoHelper.updateEntityByField() - Failed to update entity in the database", e);
        }
    }

    /**
     * Should be called internally by every method that tries to perform database operations.
     */
    void setupDBConnector() throws IOException {
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
