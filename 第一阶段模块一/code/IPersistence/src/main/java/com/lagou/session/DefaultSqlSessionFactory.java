package com.lagou.session;

import com.lagou.pojo.Configuration;

/**
 * @author hanbing
 * @date 2020-04-24 16:52
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
