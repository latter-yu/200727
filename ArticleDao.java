package blog;

import blog.Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    // 新增文章（发布博客）
    void add(Article article) {
        // 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 拼装 sql 语句
        String sql = "insert into artical values(null, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            // 替换 sql 内的 ？
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getContent());
            statement.setInt(3, article.getUserId());
            // 执行 sql 语句
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("插入新文章失败!");
                return;
            }
            System.out.println("插入新文章成功!");
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放数据库的连接
            DBUtil.close(connection, statement, null);
        }
    }
    // 查看文章列表（显示除正文外的文章信息）
    public List<Article> SelectAll() {
        List<Article> list = new ArrayList<>();
        // 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 拼装 sql 语句(不显示正文)
        String sql = "select articleId, title, userId from article";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            // 执行 sql 语句
            resultSet = statement.executeQuery();
            // 遍历结果集
            while (resultSet.next()) {
                // 针对每个结果集，都构造一个对应的 Artical 对象
                Article article = new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setUserId(resultSet.getInt("userId"));
                list.add(article);
            }
            return list;
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放数据库的连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }
    // 查看指定文章详情（需要显示正文）
    public Article selecyById(int articleId) {
        // 1. 和数据库建立连接.
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL
        String sql = "select * from article where articleId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, articleId);
            // 3. 执行 SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集. 预期 articalId 在数据库中不能重复.
            // 此处查找最多只能查出一条记录来.
            if (resultSet.next()) {
                Article article = new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setUserId(resultSet.getInt("userId"));
                return article;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 删除指定文章（给定文章 id 删除）
    public void removeById(int articleId) {
        // 1. 和数据库建立连接.
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL
        String sql = "delect * from article where articleId = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, articleId);
            // 3. 执行 SQL
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("删除文章失败!");
                return;
            }
            System.out.println("删除文章成功!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            DBUtil.close(connection, statement, null);
        }
    }
}
