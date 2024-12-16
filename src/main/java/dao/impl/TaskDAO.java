package dao.impl;

import dao.DAO;
import models.Department;
import models.Task;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The TaskDAO class is an implementation of the Dao interface. It provides concrete
 * implementations for data access operations related to Task entities using a DataSource
 * to interact with the underlying database.
 */
public class TaskDAO implements DAO<Task, Long> {
    private final DataSource dataSource;

    private static final String SELECT_ALL_SQL = """
            SELECT task_id, task_name, department_id, department_name 
            FROM tasks t JOIN departments d 
            ON t.departments_id = d.department_id
            """;
    private static final String SELECT_ID_SQL = """
            SELECT task_id, task_name, department_id, department_name
            FROM tasks t JOIN departments d ON t.departments_id = d.department_id
            WHERE task_id = ?
            """;
    private static final String INSERT_SQL = """
            INSERT INTO tasks (task_name, departments_id)
            VALUES (?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE tasks SET task_name = ?, departments_id = ?
            WHERE task_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM tasks
            WHERE task_id = ?
            """;
    private static final String SELECT_LIST_USERS_SQL = """
            SELECT u.user_id AS user_id, task_id, user_firstname, user_lastname, department_id
            FROM users_tasks ut JOIN users u ON ut.user_id = u.user_id
            WHERE ut.task_id = ?
            """;
    private static final String INSERT_INTO_USERS_TASK_SQL = """
            INSERT INTO users_tasks (user_id, task_id)
            VALUES (?, ?)
            """;
    private static final String DELETE_USERS_TASKS_SQL = """
            DELETE FROM users_tasks
            WHERE task_id = ?
            """;

    private static final String task_id = "task_id";
    private static final String task_name = "task_name";
    private static final String departments_id = "departments_id";
    private static final String department_id = "department_id";
    private static final String department_name = "department_name";
    private static final String user_id = "user_id";
    private static final String user_firstname = "user_firstname";
    private static final String user_lastname = "user_lastname";
    private static final Logger log = LoggerFactory.getLogger(DepartmentDAO.class.getName());

    /**
     * Constructs a new TaskDAO with the specified DataSource.
     *
     * @param dataSource The DataSource to be used for database connections.
     */
    public TaskDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * Retrieves a Task by its unique identifier.
     *
     * @param id The unique identifier of the Task.
     * @return An Optional containing the Task entity if found, or an empty Optional if not found.
     */
    @Override
    public Optional<Task> get(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_SQL);
            PreparedStatement stmListUsers = connection.prepareStatement(SELECT_LIST_USERS_SQL)) {

            preparedStatement.setLong(1, id);
            stmListUsers.setLong(1, id);

            Optional<Task> task;
            try (ResultSet resultSet = preparedStatement.executeQuery();
                ResultSet resListUsers = stmListUsers.executeQuery()) {

                if (resultSet.next()) {
                    Department tempDep = new Department(
                            resultSet.getLong(department_id),
                            resultSet.getString(department_name)
                    );

                    Task tempTask = new Task(
                            resultSet.getLong(task_id),
                            resultSet.getString(task_name),
                            tempDep
                    );

                    while (resListUsers.next()) {
                        User tempUser = new User(
                                resListUsers.getLong(user_id),
                                resListUsers.getString(user_firstname),
                                resListUsers.getString(user_lastname),
                                tempDep
                        );

                        tempTask.getUserList().add(tempUser);
                    }

                    task = Optional.of(tempTask);
                }
                else {
                    task = Optional.empty();
                }
                return task;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Retrieves a list of all Task available in the database.
     *
     * @return A list of Task entities, or an empty list if no Tasks are found.
     */
    @Override
    public List<Task> getAll() {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement stmAllTasks = connection.prepareStatement(SELECT_ALL_SQL)) {


            try (ResultSet rslAllTasks = stmAllTasks.executeQuery()) {

                List<Task> taskList = new ArrayList<>();

                while (rslAllTasks.next()) {
                    Department tempDep = new Department(
                            rslAllTasks.getLong(department_id),
                            rslAllTasks.getString(department_name)
                    );

                    Task task = new Task(
                            rslAllTasks.getLong(task_id),
                            rslAllTasks.getString(task_name),
                            tempDep
                    );

                    try (PreparedStatement stmListUsers = connection.prepareStatement(SELECT_LIST_USERS_SQL)) {

                        stmListUsers.setLong(1, rslAllTasks.getLong(task_id));

                        try (ResultSet rslListUsersForTask = stmListUsers.executeQuery()) {

                            while (rslListUsersForTask.next()) {
                                User tempUsr = new User(
                                        rslListUsersForTask.getLong(user_id),
                                        rslListUsersForTask.getString(user_firstname),
                                        rslListUsersForTask.getString(user_lastname),
                                        tempDep
                                );
                                task.getUserList().add(tempUsr);
                            }
                        }
                    }

                    taskList.add(task);
                }

                return taskList;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Creates a new Task in the database.
     *
     * @param obj The Task entity to be created.
     */
    @Override
    public void create(Task obj) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, obj.getTask_name());
            preparedStatement.setLong(2, obj.getDepartment().getDepartment_id());

            int res = preparedStatement.executeUpdate();
            if (res == 0) {
                throw new SQLException("A new task row doesn't create");
            }

            if (obj.getUserList().isEmpty()) return;

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {


                if (keys.next()) {
                    Long tas_id = keys.getLong(1);
                    Long task_dep_id = obj.getDepartment().getDepartment_id();

                    for (User user : obj.getUserList()) {
                        Long user_dep_id = user.getDepartment().getDepartment_id();

                        if (task_dep_id.equals(user_dep_id)) {
                            addUserToTasksUsers(tas_id, user.getUser_id(), connection);
                        }
                    }
                }
                else {
                    throw new SQLException("Failed to create task, ID not received");
                }
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Updates an existing Task in the database.
     *
     * @param obj The Task entity to be updated.
     */
    @Override
    public void update(Task obj) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, obj.getTask_name());
            preparedStatement.setLong(2, obj.getDepartment().getDepartment_id());
            preparedStatement.setLong(3, obj.getTask_id());

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("Failed to update a row in tasks table");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Deletes a Task from the database by its unique identifier.
     *
     * @param id The unique identifier of the Task to be deleted.
     */
    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            deleteUserToTasksUsers(id, connection);

            preparedStatement.setLong(1, id);

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("Failed to delete a row in tasks table");
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUserToTasksUsers(Long task_id, Long user_id, Connection connection) {
        try (PreparedStatement preparedStatement  = connection.prepareStatement(INSERT_INTO_USERS_TASK_SQL)) {

            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2, task_id);

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("Failed to update a row in tasks_users table");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserToTasksUsers(Long task_id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_TASKS_SQL)) {

            preparedStatement.setLong(1, task_id);
            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("Failed to delete a row in users_tasks table");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
