package com.hs_esslingen.insy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users getUsersByName(String name);

}
