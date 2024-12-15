package mappers;

import dto.DepartmentDTO;
import models.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The DepartmentMapper interface defines the mapping between Department entities and DepartmentDTO objects.
 * It uses the MapStruct framework to automatically generate the implementation for converting
 * between the two types.
 */
@Mapper
public interface DepartmentMapper {
    /**
     * The singleton instance of the DepartmentMapper.
     */
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);
    /**
     * Converts a Department entity to a Department.
     *
     * @param department The Department entity to be converted.
     * @return The corresponding DepartmentDTO.
     */
    DepartmentDTO toDepartmentDTO(Department department);
    /**
     * Converts a DepartmentDTO to a Department entity.
     *
     * @param departmentDTO The DepartmentDTO to be converted.
     * @return The corresponding Department entity.
     */
    Department toDepartment(DepartmentDTO departmentDTO);
}
