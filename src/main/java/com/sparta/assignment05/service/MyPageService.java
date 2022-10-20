package com.sparta.assignment05.service;

import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.response.BoardResponse;
import com.sparta.assignment05.dto.response.CommentResponse;
import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Comment;
import com.sparta.assignment05.entity.Heart;
import com.sparta.assignment05.entity.Member;
import com.sparta.assignment05.repository.BoardRepository;
import com.sparta.assignment05.repository.CommentRepository;
import com.sparta.assignment05.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final HeartRepository heartRepository;

    public GlobalResDto<?> getMyBoards(Member member) {
        List<Board> boardList = boardRepository.findAllByMember(member);
        return getGlobalResDto(boardList);
    }

    public GlobalResDto<?> getMyComments(Member member) {
        List<Board> boardList = boardRepository.findAllByMember(member);
        List<BoardResponse> responseList = new ArrayList<>();

        for (Board board : boardList) {
            if (board.getCommentCnt() == 0) continue;
            List<Comment> commentList = commentRepository.findAllByBoard(board);

            BoardResponse response = BoardResponse.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .author(board.getMember().getEmail())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .commentList(BoardResponse.commentToResponse(commentList))
                    .image(board.getImage())
                    .build();

            responseList.add(response);
        }
        return GlobalResDto.success(responseList);
    }


    public GlobalResDto<?> getMyHearts(Member member) {
        // 한 멤버가 좋아요한 객체
        List<Heart> heartList = heartRepository.findHeartByMember(member);

        List<BoardResponse> responseList = new ArrayList<>();
        for (Heart heart : heartList) {
            Board board = heart.getBoard();
            BoardResponse response = BoardResponse.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .author(board.getMember().getEmail())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .commentCnt(board.getCommentCnt())
                    .image(board.getImage())
                    .build();

            responseList.add(response);
        }
        return GlobalResDto.success(responseList);
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
                    .image(board.getImage())
                    .build();

            responseList.add(response);
        }
        return GlobalResDto.success(responseList);
    }

}
