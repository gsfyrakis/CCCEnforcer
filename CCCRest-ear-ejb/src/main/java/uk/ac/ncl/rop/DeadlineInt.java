package uk.ac.ncl.rop;

import java.util.Date;

/**
 * Created by alpac on 22/03/2016.
 */
public interface DeadlineInt {

    public void run();

    public Date getDeadline();

    public void setDeadline(Date deadline);

    /* Checks if is expiry.
       *
       * @return true, if is expiry
       */
    public boolean isExpiry();

}
