package com.sparta.assignment05.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.sparta.assignment05.S3.CommonUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.sparta.assignment05.service.MyPageService.getGlobalResDto;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public GlobalResDto<?> createBoard(MultipartFile multipartFile, BoardRequest boardRequest, Member member) throws IOException {
        String imgurl = null;

        if (!multipartFile.isEmpty()) {
            String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();
        }

        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .member(member)
                .heartCnt(0L)
                .commentCnt(0L)
                .image(imgurl)
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
                .image(board.getImage())
                .build();
        return GlobalResDto.success(response);
    }

    @Transactional
    public GlobalResDto<?> updateBoard(Long boardId, Member member, BoardRequest boardRequest)
            throws NotExistBoardException, NoAuthorException {

        // 멤버 토큰 확인 해야돼 말아야돼
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);
        // 작성자인지 아닌지 확인
        member.checkAuthor(board);
        // 게시물 수정
        board.update(boardRequest);
        return GlobalResDto.success(new BoardResponse(board));
    }

    @Transactional
    public GlobalResDto<?> deleteBoard(Long boardId, Member member)
            throws NotExistBoardException, NoAuthorException {
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
    public GlobalResDto<?> heart(Long boardId, Member member)
            throws NotExistBoardException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotExistBoardException::new);

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
