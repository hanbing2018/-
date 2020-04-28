package com.lagou.session;

/**
 * @author hanbing
 * @date 2020-04-24 14:55
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
