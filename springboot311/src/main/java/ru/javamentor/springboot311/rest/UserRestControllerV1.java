package ru.javamentor.springboot311.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.springboot311.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/users")
public class UserRestControllerV1 {

    private List<User> USERS = Stream.of(
            new User(1L, "Ivan", "Ivanov"),
            new User(2L, "Sergey", "Sergeev"),
            new User(3L, "Petr", "Petrov")
    ).collect(Collectors.toList());

    @GetMapping
    public List<User> getAll() {
        return USERS;
    }

    @GetMapping("/{Id}")
    @PreAuthorize("hasAuthority('users:read')")
    public User getById(@PathVariable long id) {
        return USERS.stream().filter(user -> user.getId().equals(id))
        .findFirst()
                .orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public User create(@RequestBody User user) {
        this.USERS.add(user);
        return user;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public void deleteById(@PathVariable Long id) {
        this.USERS.removeIf(user -> user.getId().equals(id));
    }
}
