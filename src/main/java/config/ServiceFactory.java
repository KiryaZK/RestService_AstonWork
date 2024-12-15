package config;

import dao.DAO;
import dao.impl.DepartmentDAO;
import dao.impl.TaskDAO;
import dao.impl.UserDAO;
import mappers.DepartmentMapper;
import mappers.TaskMapper;
import mappers.UserMapper;
import models.Department;
import models.Task;
import models.User;
import services.impl.DepartmentService;
import services.impl.TaskService;
import services.impl.UserService;

import javax.sql.DataSource;

/**
 * The ServiceFactory class is a utility class that provides static methods to obtain
 * instances of service classes. Each service class is configured with its corresponding
 * DAO and Mapper instances, ensuring that the services have access to the necessary
 * data access and mapping functionalities.
 */
public class ServiceFactory {
    private static final DataSource dataSource = DBConnection.getDataSource();
    private static final DAO<Department, Long> departmentDAO = new DepartmentDAO(dataSource);
    private static final DAO<Task, Long> taskDAO = new TaskDAO(dataSource);
    private static final DAO<User, Long> userDAO = new UserDAO(dataSource);
    private static final DepartmentMapper departMapper = DepartmentMapper.INSTANCE;
    private static final TaskMapper taskMapper = TaskMapper.INSTANCE;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * Returns an instance of DepartmentService, configured with the appropriate departmentDAO, userMapper, taskMapper and departMapper.
     *
     * @return An instance of DepartmentService.
     */
    public static DepartmentService getDepartmentService() {
        return new DepartmentService(departmentDAO, departMapper, userMapper, taskMapper);
    }

    /**
     * Returns an instance of TaskService, configured with the appropriate taskDAO, taskMapper, userMapper and departMapper.
     *
     * @return An instance of TaskService.
     */
    public static TaskService getTaskService() {
        return new TaskService(taskDAO, taskMapper, userMapper, departMapper);
    }

    /**
     * Returns an instance of UserService, configured with the appropriate userDAO, departMapper, userMapper and taskMapper.
     *
     * @return An instance of UserService.
     */
    public static UserService getUserService() {
        return new UserService(userDAO, departMapper, userMapper, taskMapper);
    }

}
