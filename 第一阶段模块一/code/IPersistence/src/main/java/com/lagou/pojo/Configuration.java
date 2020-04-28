package com.lagou.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hanbing
 * @date 2020-04-24 14:29
 */
public class Configuration {

    private DataSource dataSource;

    //map的id是statementId
    private Map<String, MappedStatement> map = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMap() {
        return map;
    }

    public void setMap(Map<String, MappedStatement> map) {
        this.map = map;
    }
}
