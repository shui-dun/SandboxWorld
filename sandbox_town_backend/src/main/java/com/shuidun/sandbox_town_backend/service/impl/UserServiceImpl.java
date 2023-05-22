package com.shuidun.sandbox_town_backend.service.impl;

import com.shuidun.sandbox_town_backend.bean.User;
import com.shuidun.sandbox_town_backend.mapper.UserMapper;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import com.shuidun.sandbox_town_backend.service.UserService;
import com.shuidun.sandbox_town_backend.mapper.RoleMapper;
import com.shuidun.sandbox_town_backend.service.RoleService;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final RoleService roleService;

    public UserServiceImpl(UserMapper userMapper, RoleMapper roleMapper, RoleService roleService) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.roleService = roleService;
    }

    @Override
    public User findUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    @Override
    @Transactional
    public void signup(User user) {
        userMapper.insertUser(user);
        roleMapper.insertUserRole(user.getName(), "normal");
    }

    @Override
    @Transactional
    public int deleteNotAdminUser(String name) {
        Set<String> roleSet = roleService.getRolesByUserName(name);
        if (roleSet.contains("admin")) {
            throw new UnauthorizedException("无权删除该用户");
        }
        roleMapper.deleteByUsername(name);
        return userMapper.deleteUser(name);
    }

    @Override
    public Set<User> listAll() {
        return userMapper.listAll();
    }
}
