package com.lagou.session;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author hanbing
 * @date 2020-04-24 16:58
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        Excutor excutor = new SimpleExcutor();
        MappedStatement mappedStatement = configuration.getMap().get(statementId);
        List<Object> query = excutor.query(configuration, mappedStatement, params);

        return (List<E>) query;

    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或返回结果过多");
        }

    }

    @Override
    public void updateOne(String statementId, Object... params) throws Exception {
        Excutor excutor = new SimpleExcutor();
        MappedStatement mappedStatement = configuration.getMap().get(statementId);
        excutor.update(configuration, mappedStatement, params);

    }



    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用JDK动态代理来为Dao接口生成代理对象，并返回

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层都还是去执行JDBC代码 //根据不同情况，来调用selctList或者selectOne
                // 准备参数 1：statmentid :sql语句的唯一标识：namespace.id= 接口全限定名.方法名
                // 方法名：findAll findOne addUser removeUser modifyUser
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();

                String statementId = className+"."+methodName;

                //通过方法名判断执行哪个方法
                if (methodName.contains("add")){
                    updateOne(statementId,args);
                    return null;
                }

                if (methodName.contains("modify")){
                    updateOne(statementId,args);
                    return null;
                }

                if (methodName.contains("remove")){
                    updateOne(statementId,args);
                    return null;
                }

                // 准备参数2：params:args
                // 获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                // 判断是否进行了 泛型类型参数化
                if(genericReturnType instanceof ParameterizedType){
                    List<Object> objects = selectList(statementId, args);
                    return objects;
                }

                return selectOne(statementId,args);

            }
        });

        return (T) proxyInstance;
    }
}
