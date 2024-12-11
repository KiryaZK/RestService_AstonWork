package mappers;

import dto.TaskDTO;
import models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    TaskDTO toTaskDTO(Task task);
    Task toTask(TaskDTO taskDTO);
}
