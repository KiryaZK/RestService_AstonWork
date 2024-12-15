package services.impl;

import dao.DAO;
import dto.TaskDTO;
import dto.UserDTO;
import mappers.DepartmentMapper;
import mappers.TaskMapper;
import mappers.UserMapper;
import models.Task;
import services.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The TaskService class is an implementation of the Service interface. It provides concrete
 * implementations for handling operations related to tasks. This service layer interacts with the
 * TaskDTO for data access and uses the TaskMapper for converting between Task entities and TaskDTOs.
 */
public class TaskService implements Service<TaskDTO, Long> {
    private final DAO<Task, Long> taskDAO;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;
    /**
     * Constructs a new TaskService with the specified TaskDao and TaskMapper.
     *
     * @param taskDAO  The DAO to be used for data access.
     * @param taskMapper  The TaskMapper to be used for mapping between entities and DTOs.
     * @param userMapper  The UserMapper to be used for mapping between entities and DTOs
     * @param departmentMapper  The DepartmentMapper to be used for mapping between entities and DTOs
     */
    public TaskService(DAO<Task, Long> taskDAO, TaskMapper taskMapper, UserMapper userMapper, DepartmentMapper departmentMapper) {
        this.taskDAO = taskDAO;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.departmentMapper = departmentMapper;
    }
    /**
     * Retrieves a list of all Tasks available in the system.
     *
     * @return A list of TaskDTO objects representing all Tasks.
     */
    @Override
    public List<TaskDTO> getAll() {
        List<Task> taskList = taskDAO.getAll();
        List<TaskDTO> taskDTOList = new ArrayList<>();

        for (Task task : taskList) {
            TaskDTO taskDTO = taskMapper.toTaskDTO(task);
            taskDTO.setDepartment(departmentMapper.toDepartmentDTO(task.getDepartment()));

            List<UserDTO> userDTOList = task.getUserList().stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());

            taskDTO.setUserList(userDTOList);

            taskDTOList.add(taskDTO);
        }

        return taskDTOList;
    }
    /**
     * Retrieves a Task by its unique identifier.
     *
     * @param id The unique identifier of the Task.
     * @return An Optional containing the TaskDTO if found, or an empty Optional if not found.
     */
    @Override
    public Optional<TaskDTO> get(Long id) {
        Optional<Task> task = taskDAO.get(id);
        if (task.isPresent()) {
            TaskDTO taskDTO = taskMapper.toTaskDTO(task.get());
            taskDTO.setDepartment(departmentMapper.toDepartmentDTO(task.get().getDepartment()));

            List<UserDTO> userDTOList = task.get().getUserList().stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());

            taskDTO.setUserList(userDTOList);

            return Optional.of(taskDTO);
        }
        else {
            return Optional.empty();
        }
    }
    /**
     * Creates a new Task in the system.
     *
     * @param obj The TaskDTO object representing the Task to be created.
     */
    @Override
    public void create(TaskDTO obj) {
        Task task = taskMapper.toTask(obj);
        taskDAO.create(task);
    }
    /**
     * Updates an existing Task in the system.
     *
     * @param obj The TaskDTO object representing the Task to be updated.
     */
    @Override
    public void update(TaskDTO obj) {
        Task task = taskMapper.toTask(obj);
        taskDAO.update(task);
    }
    /**
     * Deletes a Task from the system by its unique identifier.
     *
     * @param id The unique identifier of the Task to be deleted.
     */
    @Override
    public void delete(Long id) {
        taskDAO.delete(id);
    }
}
