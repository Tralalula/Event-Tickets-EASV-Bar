package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.util.Result;

import java.util.Optional;

public interface UserAuthDAO {
    Result<Optional<User>> getUserByUsernameOrMail(String usernameOrMail);
    Result<Boolean> resetPassword(String usernameOrMail);
}
