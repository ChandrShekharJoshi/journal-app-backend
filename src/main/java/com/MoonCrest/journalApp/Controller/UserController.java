package com.MoonCrest.journalApp.Controller;

import com.MoonCrest.journalApp.Entity.User;
import com.MoonCrest.journalApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userService.getAll();
    }

    @PostMapping("/save")
    public ResponseEntity<?> createUser(
            @RequestBody User user) {

        userService.saveEntry(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @PutMapping("/username/{username}")
    public ResponseEntity<?> updateUser(
            @RequestBody User user,
            @PathVariable String username) {

        User userInDb =
                userService.findByUserName(username);

        if (userInDb == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        if (user.getUserName() != null
                && !user.getUserName().isBlank()) {

            userInDb.setUserName(user.getUserName());
        }

        if (user.getPassword() != null
                && !user.getPassword().isBlank()) {

            userInDb.setPassword(user.getPassword());
        }

        userService.saveEntry(userInDb);

        return ResponseEntity.ok(userInDb);
    }
}