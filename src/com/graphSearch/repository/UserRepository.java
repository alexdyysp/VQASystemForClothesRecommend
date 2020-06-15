package com.graphSearch.repository;

import com.graphSearch.domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    //User findByUsernameAndPassword(String username, String password);
    //User findByUsername(String name);
}
