package com.sparta.assignment05.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.assignment05.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter // Getter 안붙여주면 GlobalResDto 에 매핑할 때 에러남
public class MemberResponse {

    private Long memberId;

    private String email;

    private String nickName;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

}

