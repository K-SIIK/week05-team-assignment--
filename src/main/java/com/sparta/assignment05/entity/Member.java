package com.sparta.assignment05.entity;

import com.sparta.assignment05.dto.response.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Member extends TimeStamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String password;

    // 로그인 시 입력한 비밀번호 문자열과 회원가잆 시 repo에 저장한 인코딩된 비밀번호 비교하는 메서드
    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }


    public MemberResponse getResponceInstance() {
        return MemberResponse.builder()
                .memberId(this.getId())
                .email(this.getEmail())
                .nickName(this.getNickName())
                .createdAt(this.getCreatedAt())
                .modifiedAt(this.getModifiedAt())
                .build();
    }

}
