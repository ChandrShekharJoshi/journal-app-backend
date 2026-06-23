package com.MoonCrest.journalApp;

import com.MoonCrest.journalApp.Entity.User;
import com.MoonCrest.journalApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest


public class UserTests {

    @Autowired
    private UserService userService;

    @Test
    public void UserTest() {
        String testUsername = "integration_test_user";

        // Pass the arguments directly into the constructor!
        User user = new User(testUsername, "superSecretPassword");

        // --- 2. ACT ---
        userService.saveEntry(user);
        User userFromDb = userService.findByUserName(testUsername);

        // --- 3. ASSERT ---
        assertNotNull(userFromDb, "The user should have been saved");
        assertEquals(testUsername, userFromDb.getUserName());
        assertEquals("superSecretPassword", userFromDb.getPassword());

        // --- 4. CLEANUP ---
        if (userFromDb.getId() != null) {
            userService.deleteById(userFromDb.getId());
        }

    }



    @Test
    public void testGetAllUsers() {
        // --- 1. ARRANGE (Add a couple of test users to the database) ---
        User user1 = new User();
        user1.setUserName("alice_test");
        user1.setPassword("pass123");

        User user2 = new User();
        user2.setUserName("bob_test");
        user2.setPassword("pass456");

        userService.saveEntry(user1);
        userService.saveEntry(user2);

        // --- 2. ACT (Call the getAll method) ---
        List<User> allUsers = userService.getAll();

        // --- 3. ASSERT (Check the results) ---
        assertNotNull(allUsers, "The returned list should not be null");

        // Since it's a real database, there might be older users in there too.
        // So we just check that the size is AT LEAST the 2 we just added.
        assertTrue(allUsers.size() >= 2, "There should be at least 2 users in the database");

        // --- 4. CLEANUP (Delete the test data so we don't clutter MongoDB) ---
        // Note: We have to fetch them by username first to get their generated IDs
        User savedUser1 = userService.findByUserName("alice_test");
        User savedUser2 = userService.findByUserName("bob_test");

        if (savedUser1 != null) userService.deleteById(savedUser1.getId());
        if (savedUser2 != null) userService.deleteById(savedUser2.getId());
    }
}



