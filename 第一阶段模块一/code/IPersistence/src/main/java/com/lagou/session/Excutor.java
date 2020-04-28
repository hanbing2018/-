package com.lagou.session;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.util.List;

/**
 * @author hanbing
 * @date 2020-04-24 17:34
 */
public interface Excutor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    void update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;


//    void insert(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
}
