package com.sparta.assignment05.service;

import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.response.BoardResponse;
import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Member;
import com.sparta.assignment05.repository.BoardRepository;
import com.sparta.assignment05.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public GlobalResDto<?> getMyBoards(Member member) {
        List<Board> boardList = boardRepository.findAllByMember(member);
        return getGlobalResDto(boardList);
    }

    public GlobalResDto<?> getMyComments(Member member) {
        List<Board> boardList = boardRepository.findAllByMember(member);
        for (Board board : boardList) {
//            List<Comment> commentList =
        }

    }

    static GlobalResDto<?> getGlobalResDto(List<Board> memberList) {
        List<BoardResponse> responseList = new ArrayList<>();
        for (Board board : memberList) {
            BoardResponse response = BoardResponse.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .author(board.getMember().getEmail())
                    .heartCnt(board.getHeartCnt())
                    .commentCnt(board.getCommentCnt())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .build();

            responseList.add(response);
        }
        return GlobalResDto.success(responseList);
    }

}
