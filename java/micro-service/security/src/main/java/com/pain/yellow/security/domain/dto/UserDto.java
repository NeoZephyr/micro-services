package com.pain.yellow.security.domain.dto;

import com.pain.yellow.security.annotation.MatchPassword;
import com.pain.yellow.security.annotation.ValidEmail;
import com.pain.yellow.security.annotation.ValidPassword;
import com.pain.yellow.security.util.Constants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@MatchPassword
@Data
public class UserDto implements Serializable {
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "用户名长度错误")
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

    @Pattern(regexp = Constants.PATTERN_MOBILE)
    @NotNull
    private String mobile;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20, message = "姓名长度错误")
    private String name;
}
