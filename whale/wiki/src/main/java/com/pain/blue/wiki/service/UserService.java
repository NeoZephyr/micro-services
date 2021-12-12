package com.pain.blue.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pain.blue.exception.RestException;
import com.pain.blue.id.IdGenerator;
import com.pain.blue.json.JsonUtils;
import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.rest.response.PageResult;
import com.pain.blue.wiki.domain.dto.UserDTO;
import com.pain.blue.wiki.domain.dto.UserLoginDTO;
import com.pain.blue.wiki.domain.pojo.User;
import com.pain.blue.wiki.domain.pojo.UserExample;
import com.pain.blue.wiki.mapper.UserMapper;
import com.pain.blue.wiki.request.user.*;
import com.pain.blue.wiki.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final IdGenerator idGenerator;
    private final RedisTemplate redisTemplate;

    public PageResult<UserDTO> index(UserIndexRequest query) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isBlank(query.getName())) {
            criteria.andNameLike("%" + query.getName() + "%");
        }

        PageHelper.startPage(query.getPage(), query.getSize());
        List<User> users = userMapper.selectByExample(example);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        List<UserDTO> userDTOList = CopyUtils.copy(users, UserDTO.class);
        PageResult<UserDTO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPage(pageInfo.getPages());
        pageResult.setRows(userDTOList);

        return pageResult;
    }

    public void save(UserSaveRequest saveRequest) {
        User user = selectByName(saveRequest.getName());

        if (user != null) {
            throw new RestException(ResponseUtil.USER_EXIST);
        }

        user = CopyUtils.copy(saveRequest, User.class);
        user.setId(idGenerator.gen());
        userMapper.insert(user);
    }

    public void update(String name, UserUpdateRequest updateRequest) {
        User user = selectByName(name);

        if (user == null) {
            throw new RestException(ResponseUtil.USER_NOT_EXIST);
        }
        User updateUser = CopyUtils.copy(updateRequest, User.class);
        updateUser.setId(user.getId());
        userMapper.updateByPrimaryKeySelective(updateUser);
    }

    public void delete(String name) {
        User user = selectByName(name);

        if (user != null) {
            userMapper.deleteByPrimaryKey(user.getId());
        }
    }

    public void resetPassword(String name, UserRestPasswordRequest restPasswordRequest) {
        User user = selectByName(name);

        if (user == null) {
            throw new RestException(ResponseUtil.USER_NOT_EXIST);
        }

        User updateUser = CopyUtils.copy(restPasswordRequest, User.class);
        updateUser.setId(user.getId());
        userMapper.updateByPrimaryKeySelective(updateUser);
    }

    public UserLoginDTO login(UserLoginRequest loginRequest) {
        User user = selectByName(loginRequest.getName());

        if (user == null) {
            throw new RestException(ResponseUtil.USER_LOGIN_FAILED);
        }

        if (!StringUtils.equals(user.getPassword(), loginRequest.getPassword())) {
            throw new RestException(ResponseUtil.USER_LOGIN_FAILED);
        }

        UserLoginDTO loginDTO = CopyUtils.copy(user, UserLoginDTO.class);
        long token = idGenerator.gen();
        loginDTO.setToken(String.valueOf(token));
        redisTemplate.opsForValue().set(loginDTO.getToken(), JsonUtils.objToStr(loginDTO), 30 * 60, TimeUnit.SECONDS);

        return loginDTO;
    }

    private User selectByName(String name) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<User> users = userMapper.selectByExample(example);

        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        }

        return null;
    }
}
