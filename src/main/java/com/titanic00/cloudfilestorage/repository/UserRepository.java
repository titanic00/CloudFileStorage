package com.titanic00.cloudfilestorage.repository;

import com.titanic00.cloudfilestorage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
