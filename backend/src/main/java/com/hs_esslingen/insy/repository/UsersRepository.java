package com.hs_esslingen.insy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    // Custom query methods can be defined here if needed
    // For example, to find a user by id: 
    //Optional<Users> findById(Integer id);
    

}
