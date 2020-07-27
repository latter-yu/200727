package blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    // 新增用户
    // 将数据插入到表中
    void add(User user) {
        // 获取到数据库连接
        Connection connection = DBUtil.getConnection();
        // 拼装 sql 语句
        String sql = "insert into user values (null, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            // 替换 sql 内的 ？
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 执行 sql 语句
        try {
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("插入新用户失败！");
                return;
            }
            System.out.println("插入新用户成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放数据库连接
            DBUtil.close(connection, statement, null);
        }
    }

    // 按照名字查找用户(登录)
    public User selectByName(String name) {
        // 获取到数据库连接
        Connection connection = DBUtil.getConnection();
        // 拼装 sql 语句
        String sql = "select * from user where name = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            // 替换 sql 内的 ？
            statement.setString(1, name);
            // 执行
            resultSet = statement.executeQuery();
            // 4. 遍历结果集. 预期 name 在数据库中不能重复.
            // 此处查找最多只能查出一条记录.
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        // 1. 先测试 add 方法
        /*User user = new User();
        user.setName("xz");
        user.setPassword("1234");
        userDao.add(user);*/
        // 2. 测试 selectByName
        User user = userDao.selectByName("xz");
        System.out.println(user);
    }
}



