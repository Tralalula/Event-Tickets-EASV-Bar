package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.cryptographic.BCrypt;
import event.tickets.easv.bar.dal.dao.AuthDAO;

import java.io.IOException;

public class AuthHandler {
    private AuthDAO authDAO;

    public AuthHandler() throws IOException {
        authDAO = new AuthDAO();
    }

    public User loginUser(User user) throws Exception {
        User dbUser = authDAO.getUser(user);
        System.out.println("user pw:" + user.hashedPassword());
        System.out.println("dbUser pw:" + dbUser.hashedPassword());
        if (!BCrypt.checkpw(user.hashedPassword(), dbUser.hashedPassword()))
            throw new Exception("Incorrect login details");

        return dbUser;
    }

/*    public User createUser(User user) throws Exception {
        user.setHashedPassword(user.hashedPassword());

        return authDAO.createUser(user);
    }*/

    public boolean userExists(String username) throws Exception {
        return authDAO.userExists(username);
    }
}
