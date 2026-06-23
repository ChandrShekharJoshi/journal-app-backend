package com.MoonCrest.journalApp.repository;


import com.MoonCrest.journalApp.Entity.JournalEntry;
import com.MoonCrest.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends MongoRepository<User, ObjectId> {

    User findByUserName(String username);
//    User findAll (String Username);

}
