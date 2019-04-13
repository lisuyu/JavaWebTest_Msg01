package test.msg.test;

import test.msg.dao.DAOFactory;
import test.msg.dao.IMessageDao;
import test.msg.dao.UserDao;
import test.msg.model.Message;


public class DaoTest {
    public static void main(String[] args) {
        UserDao ud = new UserDao();
//        List<User> users = ud.list();
//        System.out.println(users.size());
//        for (User user:users){
//            System.out.println(user);
//        }
        IMessageDao md = DAOFactory.getMessageDao();
        Message message = new Message();
        message.setContent("假装自己是一串很长很长的消息");
        message.setTitle("这是一个题目");
        message.setUserId(1);
        md.add(message,1);
    }
}
