package services.impl;

import dao.DAO;
import dto.TaskDTO;
import dto.UserDTO;
import mappers.DepartmentMapper;
import mappers.TaskMapper;
import mappers.UserMapper;
import models.User;
import services.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService implements Service<UserDTO, Long> {
    private final DAO<User, Long> usertDAO;
    private final DepartmentMapper departMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public UserService(DAO<User, Long> usertDAO, DepartmentMapper departMapper, UserMapper userMapper, TaskMapper taskMapper) {
        this.usertDAO = usertDAO;
        this.departMapper = departMapper;
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> userList = usertDAO.getAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        for (User user : userList) {
            UserDTO userDTO = userMapper.toUserDTO(user);
            userDTO.setDepartment(departMapper.toDepartmentDTO(user.getDepartment()));

            List<TaskDTO> taskDTOList = user.getTaskList().stream()
                    .map(taskMapper::toTaskDTO)
                    .collect(Collectors.toList());

            userDTO.setTaskList(taskDTOList);

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    @Override
    public Optional<UserDTO> get(Long id) {
        Optional<User> user = usertDAO.get(id);
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.toUserDTO(user.get());
            userDTO.setDepartment(departMapper.toDepartmentDTO(user.get().getDepartment()));

            List<TaskDTO> taskDTOList = user.get().getTaskList().stream()
                    .map(taskMapper::toTaskDTO)
                    .collect(Collectors.toList());

            userDTO.setTaskList(taskDTOList);

            return Optional.of(userDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public void create(UserDTO obj) {
        User user = userMapper.toUser(obj);
        usertDAO.create(user);
    }

    @Override
    public void update(UserDTO obj) {
        User user = userMapper.toUser(obj);
        usertDAO.update(user);
    }

    @Override
    public void delete(Long id) {
        usertDAO.delete(id);
    }
}
