package uk.ac.ncl.dao;

import uk.ac.ncl.event.Operation;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.Prohibition;
import uk.ac.ncl.rop.Right;
import uk.ac.ncl.user.User;

public interface UserManager {
    void store(User user);

    User query(String name);

    void remove(User user);

    Right getRight(User user, Operation operation);

    Obligation getObligation(User user, Operation operation);

    Prohibition getProhibition(User user, Operation operation);
}
