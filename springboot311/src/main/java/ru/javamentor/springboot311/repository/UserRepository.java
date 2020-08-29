package ru.javamentor.springboot311.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javamentor.springboot311.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByEmail(String email);
}
