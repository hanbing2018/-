package com.lagou.session;

import com.lagou.config.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanbing
 * @date 2020-04-24 17:36
 */
public class SimpleExcutor implements Excutor {
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //注册驱动，获取连接
        DataSource dataSource = configuration.getDataSource();
        Connection connection =  dataSource.getConnection();
        //获取sql语句    select * from user where id = #{id} and username = #{username}
            //对sql语句替换#{}
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //获取预处理对象prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //设置参数
            //获取参数的全路径，进而通过反射得到对象
        String paramterType = mappedStatement.getParamterType();
        Class<?> paramtertypeClass = getClassType(paramterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1,o);
        }

        //封装返回结果到Bean
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();

        while (resultSet.next()) {
            Object o =resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);

                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);

            }
            objects.add(o);
        }

        return (List<E>) objects;
    }

    @Override
    public void update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //注册驱动，获取连接
        DataSource dataSource = configuration.getDataSource();
        Connection connection =  dataSource.getConnection();
        //获取sql语句    insert into user(id, username) values(#{id}, #{username})
        //              update user set username = #{username} where id = #{id}
        //              delete from user where id = #{id}
        //对sql语句替换#{}
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //获取预处理对象prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //设置参数
        //获取参数的全路径，进而通过反射得到对象
        String paramterType = mappedStatement.getParamterType();
        Class<?> paramtertypeClass = getClassType(paramterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1,o);
        }
        int i = preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if (paramterType!=null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }


        return null;
    }

    private BoundSql getBoundSql(String sql) {

        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出的sql
        String parseSql = genericTokenParser.parse(sql);
        // 解析出来的#{}中的内容
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);

        return boundSql;
    }
}
