package uk.ac.ncl.userBo;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.ncl.dao.UserManager;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.Prohibition;
import uk.ac.ncl.rop.Right;
import uk.ac.ncl.user.User;

public class UserBoImpl implements UserBo {
    @Autowired
    private UserManager userManager;

    @Override
    public User create(String username, String password, String role) {
        User user = new User(username, password, role, new HashSet<Right>(), new HashSet<Obligation>(), new HashSet<Prohibition>());
        userManager.store(user);
        return user;
    }

    @Override
    public void delete(String username) throws Exception {
        User user = userManager.query(username);
        if (user != null) {
            userManager.remove(user);
        } else {
            throw new Exception("The user does not exsit!");
        }

    }

    @Override
    public User get(String username, String password) throws Exception {
        User user = userManager.query(username);
        if (user != null) {
            if (user.getPassword().equals(password))
                return user;
            else
                return null;
        } else {
            throw new Exception("The user does not exsit!");
        }
    }

}
