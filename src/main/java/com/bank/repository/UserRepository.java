package com.bank.repository;

import com.bank.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.requireNonNull;

public class UserRepository {

    private static Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final static String FIND_USER_BY_ID_SQL = "SELECT * FROM User WHERE UserId = ? ";
    private final static String GET_ALL_USERS_SQL = "SELECT * FROM User";
    private final static String GET_USER_BY_NAME_SQL = "SELECT * FROM User WHERE UserName = ? ";
    private final static String INSERT_USER_SQL = "INSERT INTO User (UserName, EmailAddress) VALUES (?, ?)";
    private final static String UPDATE_USER_SQL = "UPDATE User SET UserName = ?, EmailAddress = ? WHERE UserId = ? ";
    private final static String DELETE_USER_BY_ID_SQL = "DELETE FROM User WHERE UserId = ? ";
    private final static String DELETE_ALL = "DELETE FROM User";

    public List<User> getAllUsers() {
        try (final var connection = DataSourceConnection.getConnection();
             final var statement = connection.prepareStatement(GET_ALL_USERS_SQL)) {
            List list = new LinkedList();
            try (final var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(User
                            .builder()
                            .userId(resultSet.getLong("UserId"))
                            .userName(resultSet.getString("UserName"))
                            .emailAddress(resultSet.getString("EmailAddress"))
                            .build());
                }
                return Collections.unmodifiableList(list);
            }
        } catch (SQLException e) {
            throw new DataAccessError(format("Error when performing %s ", GET_ALL_USERS_SQL), e);
        }
    }


    public Optional<User> findById(Long id) {
        requireNonNull(id);
        try (final var connection = DataSourceConnection.getConnection();
             final var statement = connection.prepareStatement(FIND_USER_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (final var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional
                            .of(User
                                    .builder()
                                    .userId(resultSet.getLong("UserId"))
                                    .userName(resultSet.getString("UserName"))
                                    .emailAddress(resultSet.getString("EmailAddress"))
                                    .build());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessError(format("Error when performing %s ", FIND_USER_BY_ID_SQL), e);
        }
        return Optional.empty();
    }

    public long save(User user) {
        requireNonNull(user);
        try (final var connection = DataSourceConnection.getConnection();
             final var statement = connection.prepareStatement(INSERT_USER_SQL, RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmailAddress());
            if (statement.executeUpdate() == 0) {
                throw new DataAccessError(format("Error executing query %s", INSERT_USER_SQL));
            }
            try (final var resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
                throw new DataAccessError(format("Error executing query %s", INSERT_USER_SQL));
            }
        } catch (SQLException e) {
            throw new DataAccessError(format("Error when performing %s ", INSERT_USER_SQL), e);
        }
    }

    public int deleteById(Long id) {
        requireNonNull(id);
        try (final var connection = DataSourceConnection.getConnection();
             final var statement = connection.prepareStatement(DELETE_USER_BY_ID_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessError(format("Error when performing %s ", DELETE_USER_BY_ID_SQL), e);
        }
    }

    public int deleteAll() {
        try (final var connection = DataSourceConnection.getConnection();
             final var statement = connection.prepareStatement(DELETE_ALL)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessError(format("Error when performing %s ", DELETE_ALL), e);
        }
    }


}
