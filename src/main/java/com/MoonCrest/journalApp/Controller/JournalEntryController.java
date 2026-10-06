package com.MoonCrest.journalApp.Controller;

import com.MoonCrest.journalApp.Entity.JournalEntry;
import com.MoonCrest.journalApp.Entity.User;
import com.MoonCrest.journalApp.service.JournalEntryService;
import com.MoonCrest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    public JournalEntryController(
            JournalEntryService journalEntryService,
            UserService userService) {

        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping("/entries/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(
            @PathVariable String userName) {

        User user = userService.findByUserName(userName);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        List<JournalEntry> entries = user.getJournalEntries();

        return ResponseEntity.ok(entries);
    }

    @PostMapping("/save/{userName}")
    public ResponseEntity<?> createEntry(
            @RequestBody JournalEntry journalEntry,
            @PathVariable String userName) {

        User user = userService.findByUserName(userName);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        journalEntryService.saveEntry(journalEntry, userName);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(journalEntry);
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getJournalEntryById(
            @PathVariable ObjectId myId) {

        JournalEntry journalEntry =
                journalEntryService.getEntryById(myId);

        if (journalEntry == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Journal entry not found");
        }

        return ResponseEntity.ok(journalEntry);
    }

    @DeleteMapping("/id/{username}/{id}")
    public ResponseEntity<?> deleteJournalEntryById(
            @PathVariable String username,
            @PathVariable ObjectId id) {

        boolean deleted =
                journalEntryService.deleteById(id, username);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Journal entry not found");
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{username}/{id}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable String username,
            @PathVariable ObjectId id,
            @RequestBody JournalEntry newEntry) {

        JournalEntry updated =
                journalEntryService.updateEntry(id, newEntry);

        if (updated == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Journal entry not found");
        }

        return ResponseEntity.ok(updated);
    }
}