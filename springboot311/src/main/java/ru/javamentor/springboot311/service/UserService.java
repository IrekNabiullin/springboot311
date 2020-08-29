package ru.javamentor.springboot311.service;
import ru.javamentor.springboot311.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    List<User> listUsers();

    User getUserById(Long id);

    User getUserByName(String username);

    void updateUser(User user);

    void deleteUser(Long id);

    void deleteAllUsersFromTable();
}
