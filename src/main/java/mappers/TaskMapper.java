package mappers;

import dto.TaskDTO;
import models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The TaskMapper interface defines the mapping between Task entities and TaskDTO objects.
 * It uses the MapStruct framework to automatically generate the implementation for converting
 * between the two types.
 */
@Mapper
public interface TaskMapper {
    /**
     * The singleton instance of the TaskMapper.
     */
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    /**
     * Converts a Task entity to a Task.
     *
     * @param task The Task entity to be converted.
     * @return The corresponding TaskDTO.
     */
    TaskDTO toTaskDTO(Task task);
    /**
     * Converts a TaskDTO to a Task entity.
     *
     * @param taskDTO The TaskDTO to be converted.
     * @return The corresponding Task entity.
     */
    Task toTask(TaskDTO taskDTO);
}
