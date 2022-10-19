package com.sparta.assignment05.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {

    @NotBlank
    @Size(min = 2, max = 10)
    private String title;

    @NotBlank
    @Size(min = 10, max = 100)
    private String content;
}
