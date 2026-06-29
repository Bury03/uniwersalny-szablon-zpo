package com.example.egzamin.service;

import com.example.egzamin.entity.Role;
import com.example.egzamin.entity.User;
import com.example.egzamin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String rawPassword, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Użytkownik o takiej nazwie już istnieje");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}