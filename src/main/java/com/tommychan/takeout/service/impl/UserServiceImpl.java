package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.entity.User;
import com.tommychan.takeout.mapper.UserMapper;
import com.tommychan.takeout.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
