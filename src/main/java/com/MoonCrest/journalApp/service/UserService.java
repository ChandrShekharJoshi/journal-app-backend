package com.MoonCrest.journalApp.service;

import com.MoonCrest.journalApp.Entity.User;
import com.MoonCrest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void saveEntry(User user) {

        if (user.getPassword() != null
                && !user.getPassword().isBlank()) {

            String password = user.getPassword();

            if (!password.startsWith("$2a$")
                    && !password.startsWith("$2b$")
                    && !password.startsWith("$2y$")) {

                user.setPassword(
                        passwordEncoder.encode(password)
                );
            }
        }

        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
}