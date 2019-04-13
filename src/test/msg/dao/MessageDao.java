package test.msg.dao;

import test.msg.model.Message;
import test.msg.model.MsgException;
import test.msg.model.Pager;
import test.msg.model.SystemContext;
import test.msg.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDao implements IMessageDao{
    private IUserDao userDao;
    public MessageDao(){
        userDao = DAOFactory.getUserDao();
    }

    @Override
    public void add(Message msg, int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        if (userDao.load(userId)==null) throw new MsgException("添加留言的用户不存在");
        try {
            connection = DBUtil.getConnection();
            String sql = "insert into t_msg values (null,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,msg.getTitle());
            preparedStatement.setTimestamp(3,new Timestamp(new Date().getTime()));
            preparedStatement.setString(2,msg.getContent());
            preparedStatement.setInt(4,msg.getUserId());
            System.out.println(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
    }

    @Override
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtil.getConnection();
            /*
            delete comments before deleting msgs
             */
            String sql = "delete from t_comment where msg_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            sql = "delete from t_msg where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
    }

    @Override
    public void update(Message msg) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "update t_msg set title=?,coutent=? where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,msg.getTitle());
            preparedStatement.setString(2,msg.getContent());
            preparedStatement.setInt(3,msg.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
    }

    @Override
    public Message load(int id) {
        Message message = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from t_msg where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                message = new Message();
                message.setId(resultSet.getInt("id"));
                message.setTitle(resultSet.getString("title"));
                message.setPostDate(new Date(resultSet.getTimestamp("post_date").getTime()));
                message.setContent(resultSet.getString("content"));
                message.setUserId(resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet);
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
        return message;
    }

    @Override
    public Pager<Message> list() {
        Pager<Message> pages = new Pager<Message>();
        List<Message> lists = new ArrayList<Message>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int pageOffset = SystemContext.getPageOffset();
        int pageSize = SystemContext.getPageSize();
        pages.setPageSize(pageSize);
        pages.setPageOffset(pageOffset);
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from t_msg order by post_date desc limit ?,?";
            String sqlCount = "select count(*) from t_msg";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,pageOffset);
            preparedStatement.setInt(2,pageSize);
            resultSet = preparedStatement.executeQuery();
            Message message = null;
            while (resultSet.next()){
                message = new Message();
                message.setId(resultSet.getInt("id"));
                message.setTitle(resultSet.getString("title"));
                message.setPostDate(new Date(resultSet.getTimestamp("post_date").getTime()));
                message.setContent(resultSet.getString("content"));
                message.setUserId(resultSet.getInt("user_id"));
                lists.add(message);
            }
            preparedStatement = connection.prepareStatement(sqlCount);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int pageTotalRecords = resultSet.getInt(1);
                int pageTotalPages = (pageTotalRecords-1)/pageSize+1;
                pages.setTotalRecords(pageTotalRecords);
                pages.setTotalPages(pageTotalPages);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet);
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
        pages.setDates(lists);
        return pages;
    }
}
