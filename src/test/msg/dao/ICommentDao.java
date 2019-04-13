package test.msg.dao;

import test.msg.model.Comment;
import test.msg.model.Pager;

public interface ICommentDao {
    public void add(Comment comment,int userId,int msgId);
    public void delete(int id);
    public Comment load(int id);
    public Pager<Comment> list(int msgId);
}
