package com.hs_esslingen.insy.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Tags;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {
    // Custom query methods can be defined here if needed
    // For example, to find a tag by id: 
    // Optional<Tags> findById(Integer id);
    @Query("SELECT t from Tags t where t.name = ?1")
    Optional<Tags> findByName(String name);


}
