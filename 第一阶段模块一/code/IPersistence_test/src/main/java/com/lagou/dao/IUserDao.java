package com.lagou.dao;

import com.lagou.pojo.User;

import java.util.List;

/**
 * @author hanbing
 * @date 2020-04-25 16:14
 */
public interface IUserDao {

    List<User> findAll();

    User findByCondition(User user);

    //新增三个方法
    void addUser(User user);

    void modifyUser(User user);

    void removeUser(User user);
}
