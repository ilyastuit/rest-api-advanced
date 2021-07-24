package com.epam.esm.service.user;

import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserDTO;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.service.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper dtoMapper;

    public UserService(UserRepository userRepository, UserDTOMapper dtoMapper) {
        this.userRepository = userRepository;
        this.dtoMapper = dtoMapper;
    }

    public List<UserDTO> getAll() {
        return dtoMapper.userListToUserDTOList(userRepository.findAll());
    }

    public UserDTO getOne(int userId) throws UserNotFoundException {
        User user = getFromList(userRepository.findById(userId));

        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        return dtoMapper.userToUserDTO(user);
    }

    private User getFromList(List<User> userList) {
        return userList.stream().findAny().orElse(null);
    }
}
