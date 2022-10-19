package com.sparta.assignment05.repository;

import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoard(Board board);
    Optional<Comment> findById(Long commentId);
    Optional<Comment> findCommentById(Long commentId);
    void deleteCommentsByBoard(Board board);
}
