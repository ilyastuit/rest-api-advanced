package com.epam.esm.service.user;

import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserDTO;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.service.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getOne(int userId) throws UserNotFoundException {
        User user = getFromList(userRepository.findById(userId));

        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        return user;
    }

    private User getFromList(List<User> userList) {
        return userList.stream().findAny().orElse(null);
    }
}
