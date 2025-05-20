package com.hs_esslingen.insy.repository;

import com.hs_esslingen.insy.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
}
