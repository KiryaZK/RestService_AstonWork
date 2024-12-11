package mappers;

import dto.UserDTO;
import models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
