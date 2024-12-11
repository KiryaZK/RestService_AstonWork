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

public class DepartmentDAO implements DAO<Department, Long> {
    private final DataSource dataSource;
    private static final String INSERT_SQL = """
            INSERT INTO departments (department_name)
            VALUES (?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE departments
            SET department_name = ?
            WHERE department_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM departments
            WHERE department_id = ?
            """;
    private static final String SELECT_ALL_SQL = """
            SELECT * FROM departments
            """;
    private static final String SELECT_ID_SQL = """
            SELECT * FROM departments WHERE department_id = ?
            """;
    private static final String SELECT_LIST_TASKS = """
            SELECT * FROM tasks WHERE departments_id = ?
            """;
    private static final String SELECT_LIST_USERS = """
            SELECT * FROM users WHERE department_id = ?
            """;
    private static final String department_id = "department_id";
    private static final String department_name = "department_name";
    private static final String task_id = "task_id";
    private static final String task_name = "task_name";
    public static final String departments_id = "departments_id";
    public static final String user_id = "user_id";
    private static final String user_firstName = "user_firstname";
    private static final String user_lastName = "user_lastname";

    public DepartmentDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Department> get(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statementForIdDep = connection.prepareStatement(SELECT_ID_SQL);
             PreparedStatement statementListTasks = connection.prepareStatement(SELECT_LIST_TASKS);
             PreparedStatement statementListUsers = connection.prepareStatement(SELECT_LIST_USERS)) {

            statementForIdDep.setLong(1, id);
            statementListTasks.setLong(1, id);
            statementListUsers.setLong(1, id);

            Optional<Department> department;

            try (ResultSet resultSet = statementForIdDep.executeQuery();
                ResultSet resultSetListTasks = statementListTasks.executeQuery();
                ResultSet resultSetListUsers = statementListUsers.executeQuery()) {

                if (resultSet.next()) {
                    Department temp = new Department(
                            resultSet.getLong(department_id),
                            resultSet.getString(department_name)
                    );

                    while (resultSetListTasks.next()) {
                        temp.getTaskList().add(
                                new Task(
                                        resultSetListTasks.getLong(task_id),
                                        resultSetListTasks.getString(task_name),
                                        temp
                                )
                        );
                    }

                    while (resultSetListUsers.next()) {
                        temp.getUserList().add(
                                new User(
                                        resultSetListUsers.getLong(user_id),
                                        resultSetListUsers.getString(user_firstName),
                                        resultSetListUsers.getString(user_lastName),
                                        temp
                                )
                        );
                    }

                    department = Optional.of(temp);

                }
                else {
                    department = Optional.empty();
                }
            }

            return department;

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Department> getAll() {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL)) {

            try (ResultSet resultSet = preparedStatement.executeQuery();
                 PreparedStatement statementListTasks = connection.prepareStatement(SELECT_LIST_TASKS);
                 PreparedStatement statementListUsers = connection.prepareStatement(SELECT_LIST_USERS)) {

                List<Department> departmentList = new ArrayList<>();

                while (resultSet.next()) {
                    Department temp = new Department(
                            resultSet.getLong(department_id),
                            resultSet.getString(department_name)
                    );

                    statementListTasks.setLong(1, resultSet.getLong(departments_id));
                    statementListUsers.setLong(1, resultSet.getLong(department_id));

                    try (ResultSet resultSetListTasks = statementListTasks.executeQuery();
                         ResultSet resultSetListUsers = statementListUsers.executeQuery()) {

                        while (resultSetListTasks.next()) {
                            temp.getTaskList().add(
                                    new Task(
                                            resultSetListTasks.getLong(task_id),
                                            resultSetListTasks.getString(task_name),
                                            temp
                                    )
                            );
                        }

                        while (resultSetListUsers.next()) {
                            temp.getUserList().add(
                                    new User(
                                            resultSetListUsers.getLong(user_id),
                                            resultSetListUsers.getString(user_firstName),
                                            resultSetListUsers.getString(user_lastName),
                                            temp
                                    )
                            );
                        }
                    }

                    departmentList.add(temp);
                }

                return departmentList;

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Department obj) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, obj.getDepartment_name());
            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("A new department row doesn't create");
            }

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    obj.setDepartment_id(keys.getLong(1));
                }
                else {
                    throw new SQLException("Failed to create department, ID not received");
                }
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Department obj) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, obj.getDepartment_name());
            preparedStatement.setLong(2, obj.getDepartment_id());

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("Failed to update a row in departments table");
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, id);

            int res = preparedStatement.executeUpdate();

            if (res == 0) {
                throw new SQLException("Failed to delete a row in users table");
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
