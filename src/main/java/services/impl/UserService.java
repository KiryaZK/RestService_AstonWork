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

/**
 * The UserService class is an implementation of the Service interface. It provides concrete
 * implementations for handling operations related to users. This service layer interacts with the
 * UserDTO for data access and uses the UserMapper for converting between User entities and UserDTOs.
 */
public class UserService implements Service<UserDTO, Long> {
    private final DAO<User, Long> usertDAO;
    private final DepartmentMapper departMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    /**
     * Constructs a new UserService with the specified UserDTO and UserMapper.
     *
     * @param usertDAO  The DAO to be used for data access.
     * @param userMapper  The UserMapper to be used for mapping between entities and DTOs.
     * @param departMapper  The DepartmentMapper to be used for mapping between entities and DTOs
     * @param taskMapper  The TaskMapper to be used for mapping between entities and DTOs
     */
    public UserService(DAO<User, Long> usertDAO, DepartmentMapper departMapper, UserMapper userMapper, TaskMapper taskMapper) {
        this.usertDAO = usertDAO;
        this.departMapper = departMapper;
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }
    /**
     * Retrieves a list of all Users available in the system.
     *
     * @return A list of UserDTO objects representing all Tasks.
     */
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
    /**
     * Retrieves a User by its unique identifier.
     *
     * @param id The unique identifier of the User.
     * @return An Optional containing the UserDTO if found, or an empty Optional if not found.
     */
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
    /**
     * Creates a new User in the system.
     *
     * @param obj The UserDTO object representing the User to be created.
     */
    @Override
    public void create(UserDTO obj) {
        User user = userMapper.toUser(obj);
        usertDAO.create(user);
    }
    /**
     * Updates an existing User in the system.
     *
     * @param obj The UserDTO object representing the User to be updated.
     */
    @Override
    public void update(UserDTO obj) {
        User user = userMapper.toUser(obj);
        usertDAO.update(user);
    }
    /**
     * Deletes a User from the system by its unique identifier.
     *
     * @param id The unique identifier of the User to be deleted.
     */
    @Override
    public void delete(Long id) {
        usertDAO.delete(id);
    }
}
