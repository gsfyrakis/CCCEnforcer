package uk.ac.ncl.userBo;

import uk.ac.ncl.user.User;

public interface UserBo {
    User create(String username, String password, String role);

    void delete(String username) throws Exception;

    User get(String username, String password) throws Exception;
}
