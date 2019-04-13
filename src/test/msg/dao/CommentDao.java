package test.msg.dao;

import test.msg.model.Comment;
import test.msg.model.MsgException;
import test.msg.model.Pager;
import test.msg.model.SystemContext;
import test.msg.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDao implements ICommentDao {
    private IUserDao userDao;
    private IMessageDao messageDao;

    public CommentDao(){
        userDao = DAOFactory.getUserDao();
        messageDao = DAOFactory.getMessageDao();
    }

    @Override
    public void add(Comment comment, int userId, int msgId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        if (messageDao.load(msgId)==null) throw new MsgException("添加评论的留言不存在");
        if (userDao.load(userId)==null) throw new MsgException("添加评论的用户不存在");
        try {
            connection = DBUtil.getConnection();
            String sql = "insert into t_comment (post_date,content,user_id,msg_id) values (null,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1,new Timestamp(new Date().getTime()));
            preparedStatement.setString(2,comment.getContent());
            preparedStatement.setInt(3,comment.getUserId());
            preparedStatement.setInt(4,comment.getMsgId());
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
            String sql = "delete t_comment where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
    }

    @Override
    public Comment load(int id) {
        Comment comment = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from t_comment where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                comment = new Comment();
                comment.setId(resultSet.getInt("id"));
                comment.setPostDate(new Date(resultSet.getTimestamp("post_date").getTime()));
                comment.setContent(resultSet.getString("content"));
                comment.setUserId(resultSet.getInt("user_id"));
                comment.setMsgId(resultSet.getInt("msg_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet);
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
        return comment;
    }

    @Override
    public Pager<Comment> list(int msgId) {
        Comment comment = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Pager<Comment> pager = new Pager<Comment>();
        List<Comment> list = new ArrayList<Comment>();
        int pageSize = SystemContext.getPageSize();
        int pageOffset = SystemContext.getPageOffset();
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from t_comment where msg_id=? order post_date asc limit ?,?";
            String sqlCount = "select count(*) from t_comment where msg_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,msgId);
            preparedStatement.setInt(2,pageOffset);
            preparedStatement.setInt(3,pageSize);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                comment = new Comment();
                comment.setId(resultSet.getInt("id"));
                comment.setPostDate(new Date(resultSet.getTimestamp("past_date").getTime()));
                comment.setContent(resultSet.getString("content"));
                comment.setUserId(resultSet.getInt("user_id"));
                comment.setMsgId(resultSet.getInt("msg_id"));
                list.add(comment);
            }
            preparedStatement = connection.prepareStatement(sqlCount);
            preparedStatement.setInt(1,msgId);
            while (resultSet.next()){
                int pageTotalRecords = resultSet.getInt(1);
                int pageTotalPages = (pageTotalRecords-1)/ pageSize-1;
                pager.setTotalRecords(pageTotalRecords);
                pager.setTotalPages(pageTotalPages);
                pager.setPageSize(pageSize);
                pager.setPageOffset(pageOffset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet);
            DBUtil.close(preparedStatement);
            DBUtil.close(connection);
        }
        pager.setDates(list);
        return pager;
    }
}
