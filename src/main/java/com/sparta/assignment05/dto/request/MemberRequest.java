package com.sparta.assignment05.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email // email 형식을 갖춰야 하는 어노테이션
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
            message = "비밀번호 양식을 지켜주세요")
    private String password;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 3, max = 8, message = "이름은 최소 3자, 최대 8자로 작성하셔야 합니다.")
    private String nickName;


    @NotBlank(message = "비밀번호를 한번 더 입력해 주세요.")
    private String passwordCheck;
}
