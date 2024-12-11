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

public class ServiceFactory {
    private static final DataSource dataSource = DBConnection.getDataSource();
    private static final DAO<Department, Long> departmentDAO = new DepartmentDAO(dataSource);
    private static final DAO<Task, Long> taskDAO = new TaskDAO(dataSource);
    private static final DAO<User, Long> userDAO = new UserDAO(dataSource);
    private static final DepartmentMapper departMapper = DepartmentMapper.INSTANCE;
    private static final TaskMapper taskMapper = TaskMapper.INSTANCE;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public static DepartmentService getDepartmentService() {
        return new DepartmentService(departmentDAO, departMapper, userMapper, taskMapper);
    }

    public static TaskService getTaskService() {
        return new TaskService(taskDAO, taskMapper, userMapper, departMapper);
    }

    public static UserService getUserService() {
        return new UserService(userDAO, departMapper, userMapper, taskMapper);
    }

}
