package com.hs_esslingen.insy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Custom query methods can be defined here if needed

    @Query("SELECT u FROM User u WHERE u.name = ?1")
    Optional<User> findByName(String name);

    User getUsersByName(String name);

}
