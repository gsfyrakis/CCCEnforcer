package uk.ac.ncl.rop;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import uk.ac.ncl.event.Operation;
import uk.ac.ncl.state.RopState.ObligationState;
import uk.ac.ncl.util.DateParser;

@Entity
@Table(name = "UserObligation")
public class Obligation extends RopEntity<ObligationState> implements
        Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6075412688170777269L;


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


    public Obligation(Operation operation, ObligationState state, Date deadline) {
        super(operation, state);
        this.deadline = deadline;
    }

    public Obligation(Operation operation, Date deadline) {
        super(operation);
        this.deadline = deadline;
        this.state = ObligationState.imposed;
    }

    @Override
    public String toString() {
        return "uk.ac.ncl.rop.Obligation{" +
                "serialVersionUID=" + serialVersionUID + "deadline: " + deadline + "state: " + state +
                '}';
    }
}
