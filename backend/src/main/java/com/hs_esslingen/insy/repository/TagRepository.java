package com.hs_esslingen.insy.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    // Custom query methods can be defined here if needed
    @Query("SELECT t from Tag t where t.name = ?1")
    Optional<Tag> findByName(String name);


}
