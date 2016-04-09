package uk.ac.ncl.rop;

import uk.ac.ncl.core.Deadline;
import uk.ac.ncl.event.Operation;
import uk.ac.ncl.state.RopState.ObligationState;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "UserObligation")
public class Obligation extends RopEntity<ObligationState> implements
        Serializable {

    private static final long serialVersionUID = -6075412688170777269L;
    @Transient
    private Deadline dl;

    public Obligation() {
        super();
    }


    public Obligation(String name, Set<Operation> operation,
                      ObligationState state, Date deadline) {
        super(name, operation, state);
        this.deadline = deadline;

    }


    public Obligation(String name, Set<Operation> operation, Date deadline) {
        super(name, operation);
        this.state = ObligationState.imposed;
        this.deadline = deadline;
    }

    public Obligation(String name, Set<Operation> operation, Deadline deadlineInt) {
        super(name, operation);
        this.state = ObligationState.imposed;
        this.dl = deadlineInt;
    }

    public Obligation(Operation operation, ObligationState state, Date deadline) {
        super(operation, state);
        this.deadline = deadline;
    }

    public Obligation(Operation operation, Date deadline) {
        super(operation);
        this.deadline = deadline;
        this.state = ObligationState.imposed;
    }

    public Obligation(String name, Operation operation, Date deadline) {
        super(name, operation);
        this.deadline = deadline;
    }


    @Override
    public String toString() {
        return "uk.ac.ncl.rop.Obligation{" +
                "serialVersionUID=" + serialVersionUID + "deadline: " + deadline + "state: " + state +
                '}';
    }
}
