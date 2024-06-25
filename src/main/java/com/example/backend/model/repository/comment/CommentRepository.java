package com.example.backend.model.repository.comment;
import com.example.backend.model.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAnnouncementId(Long announcementId);
}
