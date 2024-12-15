package services.impl;

import dao.DAO;
import dto.DepartmentDTO;
import dto.TaskDTO;
import dto.UserDTO;
import mappers.DepartmentMapper;
import mappers.TaskMapper;
import mappers.UserMapper;
import models.Department;
import services.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The BookServiceImpl class is an implementation of the Service interface. It provides concrete
 * implementations for handling operations related to departments. This service layer interacts with the
 * DepartmentDTO for data access and uses the DepartmentMapper for converting between Department entities and DepartmentDTOs.
 */
public class DepartmentService implements Service<DepartmentDTO, Long> {
    private final DAO<Department, Long> departmentDAO;
    private final DepartmentMapper departMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    /**
     * Constructs a new DepartmentService with the specified DepartmentDao and DepartmentMapper.
     *
     * @param departmentDAO  The DAO to be used for data access.
     * @param departMapper  The DepartmentMapper to be used for mapping between entities and DTOs.
     * @param userMapper  The UserMapper to be used for mapping between entities and DTOs
     * @param taskMapper  The TaskMapper to be used for mapping between entities and DTOs
     */
    public DepartmentService(DAO<Department, Long> departmentDAO,
                             DepartmentMapper departMapper,
                             UserMapper userMapper, TaskMapper taskMapper) {
        this.departmentDAO = departmentDAO;
        this.departMapper = departMapper;
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }
    /**
     * Retrieves a list of all Departments available in the system.
     *
     * @return A list of DepartmentDTO objects representing all Departments.
     */
    @Override
    public List<DepartmentDTO> getAll() {
        List<Department> departmentList = departmentDAO.getAll();
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();

        for (Department department : departmentList) {
            DepartmentDTO departmentDTO = departMapper.toDepartmentDTO(department);

            List<UserDTO> userDTOList = department.getUserList().stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());

            List<TaskDTO> taskDTOList = department.getTaskList().stream()
                    .map(taskMapper::toTaskDTO)
                    .collect(Collectors.toList());

            departmentDTO.setUserList(userDTOList);
            departmentDTO.setTaskList(taskDTOList);

            departmentDTOList.add(departmentDTO);
        }

        return departmentDTOList;
    }
    /**
     * Retrieves a Department by its unique identifier.
     *
     * @param id The unique identifier of the Department.
     * @return An Optional containing the DepartmentDTO if found, or an empty Optional if not found.
     */
    @Override
    public Optional<DepartmentDTO> get(Long id) {
        Optional<Department> department = departmentDAO.get(id);
        if (department.isPresent()) {
            DepartmentDTO departmentDTO = departMapper.toDepartmentDTO(department.get());

            List<UserDTO> userDTOList = department.get().getUserList().stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());

            List<TaskDTO> taskDTOList = department.get().getTaskList().stream()
                    .map(taskMapper::toTaskDTO)
                    .collect(Collectors.toList());

            departmentDTO.setUserList(userDTOList);
            departmentDTO.setTaskList(taskDTOList);

            return Optional.of(departmentDTO);
        }
        else {
            return Optional.empty();
        }

    }
    /**
     * Creates a new Department in the system.
     *
     * @param obj The BookDTO object representing the Department to be created.
     */
    @Override
    public void create(DepartmentDTO obj) {
        Department department = departMapper.toDepartment(obj);
        departmentDAO.create(department);
    }
    /**
     * Updates an existing Department in the system.
     *
     * @param obj The DepartmentDTO object representing the Department to be updated.
     */
    @Override
    public void update(DepartmentDTO obj) {
        Department department = departMapper.toDepartment(obj);
        departmentDAO.update(department);
    }
    /**
     * Deletes a department from the system by its unique identifier.
     *
     * @param id The unique identifier of the department to be deleted.
     */
    @Override
    public void delete(Long id) {
        departmentDAO.delete(id);
    }
}
