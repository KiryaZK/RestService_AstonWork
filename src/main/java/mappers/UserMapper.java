package mappers;

import dto.UserDTO;
import models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The UserMapper interface defines the mapping between User entities and UserDTO objects.
 * It uses the MapStruct framework to automatically generate the implementation for converting
 * between the two types.
 */
@Mapper
public interface UserMapper {
    /**
     * The singleton instance of the UserMapper.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    /**
     * Converts a User entity to a User.
     *
     * @param user The User entity to be converted.
     * @return The corresponding UserDTO.
     */
    UserDTO toUserDTO(User user);
    /**
     * Converts a UserDTO to a User entity.
     *
     * @param userDTO The UserDTO to be converted.
     * @return The corresponding User entity.
     */
    User toUser(UserDTO userDTO);
}
