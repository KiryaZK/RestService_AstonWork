package mappers;

import dto.DepartmentDTO;
import models.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);
    DepartmentDTO toDepartmentDTO(Department department);
    Department toDepartment(DepartmentDTO departmentDTO);
}
