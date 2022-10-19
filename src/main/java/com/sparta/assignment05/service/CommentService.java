package com.sparta.assignment05.service;

import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.response.CommentResponse;
import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Comment;
import com.sparta.assignment05.entity.Member;
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
    public GlobalResDto<?> createComment(Long boardId, Member member, String text) {
        Board board = isPresentPostId(boardId);
        if (board == null)  return GlobalResDto.fail("NOT_EXIST_BOARD", "존재하지 않는 게시물입니다.");

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
    public GlobalResDto<?> getCommentList(Long boardId) {
        Board board = isPresentPostId(boardId);
        if (board == null)  return GlobalResDto.fail("NOT_EXIST_BOARD", "존재하지 않는 게시물입니다.");

        List<Comment> commentList = commentRepository.findAllByBoard(board);
        List<CommentResponse> responseList = new ArrayList<>();
        for (Comment comment : commentList) {
            responseList.add(new CommentResponse(comment));
        }
        Optional<Comment> comment = commentRepository.findCommentById(boardId);
        return GlobalResDto.success(comment);
    }

    @Transactional
    public GlobalResDto<?> updateComment(Long boardId, Long commentId, String text, Member member) {
        Board board = isPresentPostId(boardId);
        if (board == null)  return GlobalResDto.fail("NOT_EXIST_BOARD", "존재하지 않는 게시물입니다.");
        // email 말고 member 로 비교해도 되겠다/.
        if (!member.getEmail().equals(board.getMember().getEmail())) {
            return GlobalResDto.fail("NO_AUTHOR", "작성자가 아닙니다.");
        }
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) return GlobalResDto.fail("NOT_EXIST_COMMENT","존재하지 않는 댓글입니다.");

        comment.get().update(text);

        return GlobalResDto.success(new CommentResponse(comment.get()));
    }

    @Transactional
    public GlobalResDto<?> deleteComment(Long boardId,
                                         Long commentId,
                                         Member member) {

        Board board = isPresentPostId(boardId);
        if (board == null)  return GlobalResDto.fail("NOT_EXIST_BOARD", "존재하지 않는 게시물입니다.");
        // email 말고 member 로 비교해도 되겠다/.
        if (!member.getEmail().equals(board.getMember().getEmail())) {
            return GlobalResDto.fail("NO_AUTHOR", "작성자가 아닙니다.");
        }
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) return GlobalResDto.fail("NOT_EXIST_COMMENT","존재하지 않는 댓글입니다.");

        commentRepository.delete(comment.get());
        board.deleteComment();
        return GlobalResDto.success("Deleted Data");
    }


    @Transactional(readOnly = true)
    Board isPresentPostId(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        return board.orElse(null);
    }
}
