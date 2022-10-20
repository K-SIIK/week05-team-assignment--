package com.sparta.assignment05.entity;

import com.sparta.assignment05.dto.request.BoardRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String image;

    @Column(nullable = false)
    private String content;

    /*  No SNo serializer found for class 라는 에러가 났다.
        이유는 ManyToOne 의 옵션의 Lazy 여서 나는것같다.
        LAZY 옵션은 필요할때 조회를 해오는 옵션이다.
        필요가 없으면 조회를 안해서 비어있는 객체를 serializer 하려고 해서 발생되는 문제인것 같다.
        해결방법은 3가지가 있다.
        1. application 파일에 spring.jackson.serialization.fail-on-empty-beans=false 설정해주기
        2. 오류가 나는 엔티티의 LAZY 설정을 EAGER 로 바꿔주기
        3. 오류가 나는 컬럼에 @JsonIgnore 를 설정해주기

        4. (내 방법) repo 에서 불러온 boardList 로 바로 리턴하지 않고, response 에 하나하나 담아서 리턴해서 해결

     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column
    private Long heartCnt;

    @Column
    private Long commentCnt;

    public void update(BoardRequest boardRequest) {
        this.title = boardRequest.getTitle();
        this.content = boardRequest.getContent();
    }

    public void addHeart() {
        this.heartCnt++;
    }

    public void cancelHeart() {
        this.heartCnt--;
    }

    public void addComment() {
        this.commentCnt++;
    }

    public void deleteComment() {
        this.commentCnt--;
    }

}
