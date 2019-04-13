package test.msg.dao;

import test.msg.model.Message;
import test.msg.model.Pager;

public interface IMessageDao {
    public void add(Message msg,int userId);
    public void delete(int id);
    public void update(Message msg);
    public Message load(int id);
    public Pager<Message> list();
}
