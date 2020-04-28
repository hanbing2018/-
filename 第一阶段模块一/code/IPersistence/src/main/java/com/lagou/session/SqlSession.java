package com.lagou.session;

import java.util.List;

/**
 * @author hanbing
 * @date 2020-04-24 16:56
 */
public interface SqlSession {

    <E> List<E> selectList(String statementId, Object... params) throws Exception;


    <T> T selectOne(String statementId, Object... params) throws Exception;

    //新增增删改的方法
    void updateOne(String statementId, Object... params) throws Exception;

    <T> T getMapper(Class<?> mapperClass);

}
