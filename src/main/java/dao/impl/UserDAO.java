package dao.impl;

import dao.DAO;
import models.Department;
import models.Task;
import models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The UserDAO class is an implementation of the Dao interface. It provides concrete
 * implementations for data access operations related to User entities using a DataSource
 * to interact with the underlying database.
 */
public class UserDAO implements DAO<User, Long> {
    private final DataSource dataSource;
    private static final String INSERT_SQL = """
            INSERT INTO users (user_firstname, user_lastname, department_id)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET user_firstName = ?, user_lastName = ?, department_id = ?
            WHERE user_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE user_id = ?
            """;
    private static final String SELECT_ALL_SQL = """
            SELECT *
            FROM users u JOIN departments d ON u.department_id = d.department_id
            """;
    private static final String SELECT_ID_SQL = """
            SELECT *
            FROM users u JOIN departments d ON u.department_id = d.department_id
            WHERE u.user_id = ?
            """;
    private static final String SELECT_LIST_TASK = """
            SELECT *
            FROM tasks t JOIN users_tasks ut ON t.task_id = ut.task_id
            WHERE user_id = ?
            """;

    private static final String user_firstName = "user_firstName";
    private static final String user_lastName = "user_lastName";
    private static final String user_id = "user_id";
    private static final String department_id = "department_id";
    private static final String department_name = "department_name";
    private static final String task_id = "task_id";
    private static final String task_name = "task_name";
    /**
     * Constructs a new UserDAO with the specified DataSource.
     *
     * @param dataSource The UserDAO to be used for database connections.
     */
    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * Retrieves a User by its unique identifier.
     *
     * @param id The unique identifier of the User.
     * @return An Optional containing the User entity if found, or an empty Optional if not found.
     */
    @Override
    public Optional<User> get(Long id) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_SQL);
            PreparedStatement stmListTask = connection.prepareStatement(SELECT_LIST_TASK)) {

            preparedStatement.setLong(1, id);
            stmListTask.setLong(1, id);

            Optional<User> user;

            try (ResultSet resultSet = preparedStatement.executeQuery();
                ResultSet resListTask = stmListTask.executeQuery()) {

                if (resultSet.next()) {
                    Department tempDep = new Department(
                            resultSet.getLong(department_id),
                            resultSet.getString(department_name)
                    );

                    User tempUser = new User(
                            resultSet.getLong(user_id),
                            resultSet.getString(user_firstName),
                            resultSet.getString(user_lastName),
                            tempDep
                    );

                    while (resListTask.next()) {
                        Task task = new Task(
                                resListTask.getLong(task_id),
                                resListTask.getString(task_name),
                                tempDep
                        );

                        tempUser.getTaskList().add(task);
                    }

                    user = Optional.of(tempUser);
                }
                else {
                    user = Optional.empty();
                }
            }

            return user;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Retrieves a list of all User available in the database.
     *
     * @return A list of User entities, or an empty list if no Users are found.
     */
    @Override
    public List<User> getAll() {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement allUsersStm = connection.prepareStatement(SELECT_ALL_SQL)) {

            try (ResultSet resultSet = allUsersStm.executeQuery()) {

                List<User> userList = new ArrayList<>();

                while (resultSet.next()) {
                    Department tempDep = new Department(
                            resultSet.getLong(department_id),
                            resultSet.getString(department_name)
                    );

                    User tempUser = new User(
                            resultSet.getLong(user_id),
                            resultSet.getString(user_firstName),
                            resultSet.getString(user_lastName),
                            tempDep
                    );

                    try (PreparedStatement stmListTask = connection.prepareStatement(SELECT_LIST_TASK)) {

                        stmListTask.setLong(1, resultSet.getLong(user_id));

                        try (ResultSet resultListTask = stmListTask.executeQuery()) {

                            while (resultListTask.next()) {
                                Task tempTask = new Task(
                                        resultListTask.getLong(task_id),
                                        resultListTask.getString(task_name),
                                        tempDep
                                );

                                tempUser.getTaskList().add(tempTask);
                            }
                        }
                    }

                    userList.add(tempUser);
                }

                return userList;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Creates a new User in the database.
     *
     * @param obj The User entity to be created.
     */
    @Override
    public void create(User obj) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, obj.getUser_firstname());
            preparedStatement.setString(2, obj.getUser_lastname());
            preparedStatement.setLong(3, obj.getDepartment().getDepartment_id());

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("A new row for User doesn't create");
            }

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    obj.setUser_id(keys.getLong(1));
                }
                else {
                    throw new SQLException("Failed to create user, ID not received");
                }
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Updates an existing User in the database.
     *
     * @param obj The User entity to be updated.
     */
    @Override
    public void update(User obj) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, obj.getUser_firstname());
            preparedStatement.setString(2, obj.getUser_lastname());
            preparedStatement.setLong(3, obj.getDepartment().getDepartment_id());
            preparedStatement.setLong(4, obj.getUser_id());

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("A update row for User doesn't create");
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Deletes a User from the database by its unique identifier.
     *
     * @param id The unique identifier of the User to be deleted.
     */
    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, id);

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("Failed to delete a row in departments table");
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
