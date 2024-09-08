package service.impl;

import pojo.User;
import service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Override
    public String getUserName(User user) {
//        throw new IllegalArgumentException();
        return Optional.ofNullable(user)
                .map(User::getName)
                .orElse("");
    }
}
