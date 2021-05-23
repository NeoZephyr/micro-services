package com.pain.yellow.app.domain.dto;

import com.pain.yellow.app.annotation.validator.MatchPassword;
import com.pain.yellow.app.annotation.validator.ValidEmail;
import com.pain.yellow.app.annotation.validator.ValidPassword;
import com.pain.yellow.app.util.constant.PatternConstants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@MatchPassword
@Data
public class UserDto {
    @NotNull
    @NotBlank
    @Size(min = 4, max = 32, message = "用户名长度错误")
    private String username;

    @NotNull
    @NotBlank
    @ValidPassword
    private String password;

    @NotNull
    @NotBlank
    private String matchPassword;

    @NotNull
    @NotBlank
    @ValidEmail
    private String email;

    @Pattern(regexp = PatternConstants.PATTERN_MOBILE)
    @NotNull
    private String mobile;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20, message = "姓名长度错误")
    private String name;
}
