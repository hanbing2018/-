package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.session.SqlSession;
import com.lagou.session.SqlSessionFactory;
import com.lagou.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


/**
 * @author hanbing
 * @date 2020-04-24 14:15
 */
public class IPersistenceTest {

    @Test
    public void test() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        /*User user = new User();
        user.setId(1);
        user.setUsername("孙悟空");
        User user2 = sqlSession.selectOne("user.selectOne", user);
        System.out.println(user2);*/

        /*List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }


    }







    @Test
    public void testInsert() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User user = new User();
        user.setId(1);
        user.setUsername("孙悟空");

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        userDao.addUser(user);
    }

    @Test
    public void testUpdate() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User user = new User();
        user.setId(2);
        user.setUsername("猪八戒");

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        userDao.modifyUser(user);
    }

    @Test
    public void testDelete() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User user = new User();
        user.setId(1);

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        userDao.removeUser(user);
    }





































    @Test
    public void test2() throws Exception {
        //jdbc:mysql://主机名或IP抵制：端口号/数据库名?useUnicode=true&characterEncoding=UTF-8&useSSL=true
        String URL="jdbc:mysql:///zdy_mybatis?serverTimezone=GMT&useUnicode=true&characerEncoding=utf-8&useSSL=true";
//        String URL="jdbc:mysql:///zdy_mybatis";
        String USER="root";
        String PASSWORD="root";
        //1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获得数据库链接
        Connection conn= DriverManager.getConnection(URL, USER, PASSWORD);
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select * from user");
        //4.处理数据库的返回结果(使用ResultSet类)
        while(rs.next()) {
            System.out.println(rs.getString("userName"));
        }
        //关闭资源
        rs.close();
        st.close();
        conn.close();
    }

}
