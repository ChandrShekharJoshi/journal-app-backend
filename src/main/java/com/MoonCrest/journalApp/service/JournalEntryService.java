package com.MoonCrest.journalApp.service;

import com.MoonCrest.journalApp.Entity.JournalEntry;
import com.MoonCrest.journalApp.Entity.User;
import com.MoonCrest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    public JournalEntryService(
            JournalEntryRepository journalEntryRepository,
            UserService userService) {

        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    @Cacheable(value = "journalCache", key = "#id.toString()")
    public JournalEntry getEntryById(ObjectId id) {

        System.out.println("Fetching journal from MongoDB...");

        return journalEntryRepository
                .findById(id)
                .orElse(null);
    }

    public void saveEntry(
            JournalEntry journalEntry,
            String userName) {

        User user = userService.findByUserName(userName);

        if (user == null) {
            return;
        }

        journalEntry.setDate(LocalDateTime.now());

        JournalEntry saved =
                journalEntryRepository.save(journalEntry);

        user.getJournalEntries().add(saved);

        userService.saveEntry(user);
    }

    public void saveEntry(JournalEntry journalEntry) {

        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {

        return journalEntryRepository.findAll();
    }



    @CacheEvict(
            value = "journalCache",
            key = "#id.toString()"
    )
    public boolean deleteById(
            ObjectId id,
            String userName) {

        User user = userService.findByUserName(userName);

        if (user == null) {
            return false;
        }

        boolean exists =
                journalEntryRepository.existsById(id);

        if (!exists) {
            return false;
        }

        user.getJournalEntries()
                .removeIf(entry ->
                        entry.getId().equals(id));

        userService.saveEntry(user);

        journalEntryRepository.deleteById(id);

        return true;
    }

    @CachePut(value = "journalCache", key = "#id.toString()")
    public JournalEntry updateEntry(
            ObjectId id,
            JournalEntry newEntry) {

        JournalEntry oldEntry =
                journalEntryRepository
                        .findById(id)
                        .orElse(null);

        if (oldEntry == null) {
            return null;
        }

        if (newEntry.getTitle() != null
                && !newEntry.getTitle().isBlank()) {

            oldEntry.setTitle(newEntry.getTitle());
        }

        if (newEntry.getContent() != null
                && !newEntry.getContent().isBlank()) {

            oldEntry.setContent(newEntry.getContent());
        }

        return journalEntryRepository.save(oldEntry);
    }
}