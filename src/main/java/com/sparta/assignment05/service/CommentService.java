package com.sparta.assignment05.service;

import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.response.CommentResponse;
import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Comment;
import com.sparta.assignment05.entity.Member;
import com.sparta.assignment05.exception.NoAuthorException;
import com.sparta.assignment05.exception.NotExistBoardException;
import com.sparta.assignment05.exception.NotExistCommentException;
import com.sparta.assignment05.repository.BoardRepository;
import com.sparta.assignment05.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public GlobalResDto<?> createComment(Long boardId, Member member, String text)
            throws NotExistBoardException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);

        Comment comment = Comment.builder()
                .comment(text)
                .board(board)
                .member(member)
                .build();

        commentRepository.save(comment);
        board.addComment();
        return GlobalResDto.success(new CommentResponse(comment));
    }

    // 왜 여기만 읽기 전용?
    @Transactional(readOnly = true)
    public GlobalResDto<?> getCommentList(Long boardId) throws NotExistBoardException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);

        List<Comment> commentList = commentRepository.findAllByBoard(board);
        List<CommentResponse> responseList = new ArrayList<>();
        for (Comment comment : commentList) {
            responseList.add(new CommentResponse(comment));
        }
        Optional<Comment> comment = commentRepository.findCommentById(boardId);
        return GlobalResDto.success(comment);
    }

    @Transactional
    public GlobalResDto<?> updateComment(Long boardId, Long commentId, String text, Member member)
            throws NotExistBoardException, NoAuthorException, NotExistCommentException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);
        // 작성자인지 아닌지 확인
        member.checkAuthor(board);
        // 댓글이 존재하지 않으면 예외처리
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotExistCommentException::new);

        comment.update(text);

        return GlobalResDto.success(new CommentResponse(comment));
    }

    @Transactional
    public GlobalResDto<?> deleteComment(Long boardId, Long commentId, Member member)
            throws NotExistBoardException, NoAuthorException, NotExistCommentException {

        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);

        member.checkAuthor(board);

        // 댓글이 존재하지 않으면 예외처리
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotExistCommentException::new);

        commentRepository.delete(comment);
        board.deleteComment();
        return GlobalResDto.success("Deleted Data");
    }
}
