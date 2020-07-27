package blog;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    // 管理数据库连接
    // 1) 建立连接
    // 2) 断开连接
    // JDBC 中使用 DataSource 来管理连接.
    // DBUtil 相当于是对 DataSource 再稍微包装一层.
    // DataSource 每个应用程序只应该有一个实例~~ (单例)
    // DBUtil 本质上就是实现了一个单例模式, 管理了唯一的一个 DataSource 实例
    // 单例模式的实现, 有两种风格:
    // 1. 饿汉模式
    // 2. 懒汉模式.
    // 此处使用懒汉模式即可.

    private static volatile DataSource dataSource = null;
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/blogDemo?characterEncoding=utf-8&useSSL=true";
    private static final String USERNAME = "xz";
    private static final String PASSWORD = "1005";

    public static DataSource getDataSource() {
        // 懒汉模式有线程不安全问题，因此一些解决措施：
        // 加锁
        // 双重 if 判定
        // volatile

        if (dataSource == null) {
            synchronized (DBUtil.class) {
                if (dataSource == null) {
                    dataSource = new MysqlDataSource();
                    // 给 DataSource 设置一些属性
                    ((MysqlDataSource)dataSource).setURL(URL);
                    ((MysqlDataSource)dataSource).setUser(USERNAME);
                    ((MysqlDataSource)dataSource).setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection() {
        // 获取连接
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        // 断开连接
        // 后创建的先断开
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

