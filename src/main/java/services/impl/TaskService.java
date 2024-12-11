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

public class TaskService implements Service<TaskDTO, Long> {
    private final DAO<Task, Long> taskDAO;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;

    public TaskService(DAO<Task, Long> taskDAO, TaskMapper taskMapper, UserMapper userMapper, DepartmentMapper departmentMapper) {
        this.taskDAO = taskDAO;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.departmentMapper = departmentMapper;
    }

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

    @Override
    public void create(TaskDTO obj) {
        Task task = taskMapper.toTask(obj);
        taskDAO.create(task);
    }

    @Override
    public void update(TaskDTO obj) {
        Task task = taskMapper.toTask(obj);
        taskDAO.update(task);
    }

    @Override
    public void delete(Long id) {
        taskDAO.delete(id);
    }
}
