package com.MoonCrest.journalApp;

import com.MoonCrest.journalApp.Entity.User;
import com.MoonCrest.journalApp.repository.UserRepository;
import com.MoonCrest.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLOutput;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j

public class UserTest1 {

    @Autowired
    private UserService userService;

    @Test
    public void UserTest() {
        String testUsername = "integration_test_user";
        User user = new User(testUsername, "superSecretPassword");

        userService.saveEntry(user);
        User userFromDb = userService.findByUserName(testUsername);

        // 3. Use log.info to print to the Spring Boot console!
        log.info("Successfully fetched user from database: {}", userFromDb.getUserName());
        log.info("User Password is: {}", userFromDb.getPassword());

        assertNotNull(userFromDb);
        assertEquals(testUsername, userFromDb.getUserName());

        if (userFromDb.getId() != null) {
            userService.deleteById(userFromDb.getId());
        }
    }

    @Test
    public  void testUserRepository(){
        List<User> list= userService.getAll();
        System.out.println(list);
    }
}
