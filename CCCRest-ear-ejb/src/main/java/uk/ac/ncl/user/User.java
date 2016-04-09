package uk.ac.ncl.user;

import uk.ac.ncl.core.TimeKeeper;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.Prohibition;
import uk.ac.ncl.rop.Right;
import uk.ac.ncl.rop.RopEntity;
import uk.ac.ncl.state.RopState.ObligationState;
import uk.ac.ncl.state.RopState.ProhibitionState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

@Entity
@Table(name = "User")
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5455560211410987289L;

    @Transient
    private final static Logger log = Logger.getLogger(User.class
            .toString());

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<Right> rightSet;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<Obligation> obligationSet;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<Prohibition> prohibitionSet;

    @Transient
    private static TimeKeeper timeKeeper;

    public User(String name, String password, String role, Set<Right> rightSet,
                Set<Obligation> obligationSet, Set<Prohibition> prohibitionSet) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.rightSet = rightSet;
        this.obligationSet = obligationSet;
        this.prohibitionSet = prohibitionSet;
    }

    public User() {

    }

    public static void setTimeKeeper(TimeKeeper timeKeeper) {
        User.timeKeeper = timeKeeper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Right> getRightSet() {
        return rightSet;
    }

    public void setRightSet(Set<Right> rightSet) {
        this.rightSet = rightSet;
    }

    public Set<Obligation> getObligationSet() {
        return obligationSet;
    }

    public void setObligationSet(Set<Obligation> obligationSet) {
        this.obligationSet = obligationSet;
    }

    public Set<Prohibition> getProhibitionSet() {
        return prohibitionSet;
    }

    public void setProhibitionSet(Set<Prohibition> prohibitionSet) {
        this.prohibitionSet = prohibitionSet;
    }


    public void addObligation(RopEntity<?> entity) {

        log.info("add user obligation");

        Obligation obligation = (Obligation) entity;
        if (obligationSet.contains(obligation)) {

            log.info("obligation set contains obligation");

            for (Obligation result : obligationSet) {
                if (obligation.equals(result)) {
                    if (result.getState() == ObligationState.fulfilled) {
                        result.setState(ObligationState.imposed);
                        log.info("set Obligation state to imposed");
                    }
                }
            }
        } else {
            log.info("add  obligation to timekeeper");
            log.info("timekeeper: " + timeKeeper);
            if (timeKeeper != null)
                timeKeeper.addDeadline(obligation, obligation.getOperation(), this.getName(), entity.getDeadline());

            obligationSet.add(obligation);
        }

    }

    public void addRight(RopEntity<?> entity) {
        Right right = (Right) entity;
        rightSet.add(right);

    }

    public void addProhibition(RopEntity<?> entity) {
        Prohibition prohibition = (Prohibition) entity;
        if (prohibitionSet.contains(prohibition)) {
            for (Prohibition result : prohibitionSet) {
                if (prohibition.equals(result)) {
                    if (result.getState() == ProhibitionState.fulfilled) {
                        result.setState(ProhibitionState.imposed);
                    }
                }
            }

        } else
            prohibitionSet.add(prohibition);
    }


    public boolean removeRopEntity(RopEntity<?> entity) {
        if (entity instanceof Right) {
            Right right = (Right) entity;
            Iterator<Right> iter = rightSet.iterator();
            while (iter.hasNext()) {
                Right result = iter.next();
                if (right.equals(result)) {
                    iter.remove();
                    return true;
                }
            }
            return false;
        } else if (entity instanceof Obligation) {
            Obligation obligation = (Obligation) entity;
            Iterator<Obligation> iter = obligationSet.iterator();
            while (iter.hasNext()) {
                Obligation result = iter.next();
                if (obligation.equals(result)) {
                    iter.remove();
                    return true;
                }
            }
            return false;
        } else if (entity instanceof Prohibition) {
            Prohibition prohibition = (Prohibition) entity;
            Iterator<Prohibition> iter = prohibitionSet.iterator();
            while (iter.hasNext()) {
                Prohibition result = iter.next();
                if (prohibition.equals(result)) {
                    iter.remove();
                    return true;
                }
            }
            return false;
        } else
            return false;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", role=" + role + "]";
    }

}
