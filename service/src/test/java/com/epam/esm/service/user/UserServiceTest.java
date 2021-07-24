package com.epam.esm.service.user;

import com.epam.esm.entity.user.User;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    private UserService service;

    @BeforeEach
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
        service = new UserService(repository, new UserDTOMapperImpl());
    }

    @Test
    public void successGetAll() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "admin@mail.ru", "123", LocalDateTime.now(), LocalDateTime.now(), null));

        when(repository.findAll()).thenReturn(users);

        service.getAll();

        verify(repository, times(1)).findAll();
    }

    @Test
    void successGetOne() throws UserNotFoundException {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "admin@mail.ru", "123", LocalDateTime.now(), LocalDateTime.now(), null));

        when(repository.findById(1)).thenReturn(users);

        service.getOne(1);

        verify(repository, times(1)).findById(1);
    }

    @Test
    void failGetOne() {
        List<User> users = new ArrayList<>();
        when(repository.findById(1)).thenReturn(users);

        assertThrows(UserNotFoundException.class, () -> {
            service.getOne(1);
        });

        verify(repository, times(1)).findById(1);
    }
}
