package com.sparta.assignment05.repository;

import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Heart;
import com.sparta.assignment05.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findHeartByBoardAndMember(Board board, Member member);

}
