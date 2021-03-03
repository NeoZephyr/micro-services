package com.pain.yellow.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pain.yellow.security.config.AuthProperties;
import com.pain.yellow.security.domain.Role;
import com.pain.yellow.security.domain.User;
import com.pain.yellow.security.domain.dto.LoginDto;
import com.pain.yellow.security.domain.dto.UserDto;
import com.pain.yellow.security.repository.RoleRepo;
import com.pain.yellow.security.repository.UserRepo;
import com.pain.yellow.security.util.JwtUtil;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.pain.yellow.security.util.Constants.ROLE_ADMIN;
import static com.pain.yellow.security.util.Constants.ROLE_USER;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private PasswordGenerator passwordGenerator;

    @WithMockUser
    @Test
    public void test() throws Exception {
        mockMvc.perform(get("/customers/greeting")).andExpect(status().isOk());
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        passwordGenerator = new PasswordGenerator();
        userRepo.deleteAllInBatch();
        roleRepo.deleteAllInBatch();
        Role roleUser = Role.builder()
                .authority(ROLE_USER)
                .build();
        Role roleAdmin = Role.builder()
                .authority(ROLE_ADMIN)
                .build();
        Role savedRoleUser = roleRepo.save(roleUser);
        roleRepo.save(roleAdmin);

        Set<Role> roles = new HashSet<>();
        roles.add(savedRoleUser);

        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("12345678"))
                .mobile("13012341234")
                .name("New User")
                .email("user@local.dev")
                .authorities(roles)
                .build();
        userRepo.save(user);
    }

    @Test
    public void givenUserDto_thenRegisterSuccess() throws Exception {
        // 使用 Passay 提供的 PasswordGenerator 生成符合规则的密码
        String password = passwordGenerator.generatePassword(8,
                // 至少有一个大写字母
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // 至少有一个小写字母
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // 至少有一个数字
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // 至少有一个特殊字符
                new CharacterRule(EnglishCharacterData.Special, 1));
        UserDto userDto = UserDto.builder()
                .username("new_user")
                .password(password)
                .matchPassword(password)
                .mobile("13912341234")
                .name("New User")
                .email("new_user@local.dev")
                .build();

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenLoginDto_shouldReturnJwt() throws Exception {
        String username = "user";
        String password = "12345678";
        LoginDto loginDto = new LoginDto(username, password);
        mockMvc.perform(post("/users/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequest_shouldSucceedWith200() throws Exception {
        String username = "user";
        Set<Role> authorities = new HashSet<>();
        authorities.add(Role.builder()
                .authority(ROLE_USER)
                .build());
        authorities.add(Role.builder()
                .authority(ROLE_ADMIN)
                .build());
        User user = User.builder()
                .username(username)
                .authorities(authorities)
                .build();
        String token = jwtUtil.genAccessToken(user);
        mockMvc.perform(get("/api/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }
}