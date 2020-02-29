package com.maxifom.note_app.repository;

import com.maxifom.note_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUserName(String userName);
    boolean existsUserByUserName(String userName);
}
