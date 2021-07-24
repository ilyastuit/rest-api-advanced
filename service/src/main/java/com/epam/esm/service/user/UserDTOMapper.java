package com.epam.esm.service.user;

import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    UserDTO userToUserDTO(User user);

    List<UserDTO> userListToUserDTOList(List<User> userList);

}
