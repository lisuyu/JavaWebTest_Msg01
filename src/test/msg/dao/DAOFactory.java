package test.msg.dao;

public class DAOFactory {
    public static IUserDao getUserDao(){
        return new UserDao();
    }

    public static IMessageDao getMessageDao(){
        return new MessageDao();
    }

    public static ICommentDao getCommentDao(){
        return new CommentDao();
    }
}
