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

public class DepartmentService implements Service<DepartmentDTO, Long> {
    private final DAO<Department, Long> departmentDAO;
    private final DepartmentMapper departMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public DepartmentService(DAO<Department, Long> departmentDAO, DepartmentMapper departMapper, UserMapper userMapper, TaskMapper taskMapper) {
        this.departmentDAO = departmentDAO;
        this.departMapper = departMapper;
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }

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

    @Override
    public void create(DepartmentDTO obj) {
        Department department = departMapper.toDepartment(obj);
        departmentDAO.create(department);
    }

    @Override
    public void update(DepartmentDTO obj) {
        Department department = departMapper.toDepartment(obj);
        departmentDAO.update(department);
    }

    @Override
    public void delete(Long id) {
        departmentDAO.delete(id);
    }
}
