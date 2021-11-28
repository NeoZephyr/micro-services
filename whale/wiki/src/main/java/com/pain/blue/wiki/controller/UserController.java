package com.pain.blue.wiki.controller;

import com.pain.blue.rest.response.PageResult;
import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.wiki.domain.dto.UserDTO;
import com.pain.blue.wiki.request.user.UserIndexRequest;
import com.pain.blue.wiki.request.user.UserSaveRequest;
import com.pain.blue.wiki.request.user.UserUpdateRequest;
import com.pain.blue.wiki.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public RestResponse index(@Valid UserIndexRequest query) {
        PageResult<UserDTO> result = userService.index(query);
        return RestResponse.success(result);
    }

    @PostMapping("")
    public RestResponse save(@Valid @RequestBody UserSaveRequest saveRequest) {
        userService.save(saveRequest);
        return RestResponse.success();
    }

    @PutMapping("/{name}")
    public RestResponse update(@PathVariable String name, @Valid @RequestBody UserUpdateRequest updateRequest) {
        userService.update(name, updateRequest);
        return RestResponse.success();
    }

    @DeleteMapping("/{name}")
    public RestResponse delete(@PathVariable String name) {
        userService.delete(name);
        return RestResponse.success();
    }
}
