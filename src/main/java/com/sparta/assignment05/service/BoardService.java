package com.sparta.assignment05.service;

import com.sparta.assignment05.dto.request.BoardRequest;
import com.sparta.assignment05.dto.response.BoardResponse;
import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.response.HeartResponse;
import com.sparta.assignment05.entity.Board;
import com.sparta.assignment05.entity.Comment;
import com.sparta.assignment05.entity.Heart;
import com.sparta.assignment05.entity.Member;
import com.sparta.assignment05.exception.NoAuthorException;
import com.sparta.assignment05.exception.NotExistBoardException;
import com.sparta.assignment05.repository.BoardRepository;
import com.sparta.assignment05.repository.CommentRepository;
import com.sparta.assignment05.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.assignment05.service.MyPageService.getGlobalResDto;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public GlobalResDto<?> createBoard(BoardRequest boardRequest, Member member) {

        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .member(member)
                .heartCnt(0L)
                .commentCnt(0L)
                .build();

        boardRepository.save(board);
        return GlobalResDto.success(new BoardResponse(board));
    }

    @Transactional(readOnly = true)
    public GlobalResDto<?> getBoardsList() {
        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
        return getGlobalResDto(boardList);
    }

    @Transactional(readOnly = true)
    public GlobalResDto<?> getOneBoard(Long boardId) throws NotExistBoardException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);

        List<Comment> commentList = commentRepository.findAllByBoard(board);

        BoardResponse response = BoardResponse.builder()
                .boardId(boardId)
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getMember().getEmail())
                .heartCnt(board.getHeartCnt())
                .commentList(BoardResponse.commentToResponse(commentList))
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
        return GlobalResDto.success(response);
    }

    @Transactional
    public GlobalResDto<?> updateBoard(Long boardId,
                                       Member member,
                                       BoardRequest boardRequest) throws NotExistBoardException, NoAuthorException {

        // 멤버 토큰 확인 해야돼 말아야돼
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);
        // 작성자인지 아닌지 확인
        member.checkAuthor(board);
        // 게시물 수정
        board.update(boardRequest);
        return GlobalResDto.success(new BoardResponse(board));
    }

    @Transactional
    public GlobalResDto<?> deleteBoard(Long boardId, Member member) throws NotExistBoardException, NoAuthorException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);

        member.checkAuthor(board);

        // 댓글 먼저 지운 후,
        commentRepository.deleteCommentsByBoard(board);
        // 게시물 좋아요 객체도 제거
        heartRepository.deleteHeartsByBoard(board);
        // 게시물 삭제
        boardRepository.delete(board);

        return GlobalResDto.success("Deleted Data");
    }

    @Transactional
    public GlobalResDto<?> heart(Long boardId, Member member) throws NotExistBoardException, NoAuthorException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);
        // 작성자인지 아닌지 확인
        member.checkAuthor(board);

        if (heartRepository.findHeartByBoardAndMember(board, member).isEmpty()) {
            // 좋아요
            Heart heart = new Heart(board, member);
            heartRepository.save(heart);
            board.addHeart();
            return GlobalResDto.success(new HeartResponse(heart));

        } else {
            // 좋아요 취소
            Heart heart = cancelHeart(board, member);
            return GlobalResDto.success(new HeartResponse(heart));
        }
    }

    // 좋아요 취소
    private Heart cancelHeart(Board board, Member member) {
        Heart heart = heartRepository.findHeartByBoardAndMember(board, member).get();
        board.cancelHeart();
        heartRepository.delete(heart);
        return heart;
    }
}
