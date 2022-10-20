package com.sparta.assignment05.repository;

import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedAtDesc();
    Optional<Board> findById(Long id);
    List<Board> findAllByMember(Member member);
}
