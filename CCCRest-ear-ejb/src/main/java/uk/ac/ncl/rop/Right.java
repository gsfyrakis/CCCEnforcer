package uk.ac.ncl.rop;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import uk.ac.ncl.event.Operation;
import uk.ac.ncl.state.RopState.RightState;

@Entity
@Table(name = "UserRight")
public class Right extends RopEntity<RightState> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6031505480282101097L;

    @Column(name = "FulfilledTimes")
    private int counter = 0;

    public Right() {
        super();
    }

    public Right(String name, Set<Operation> operation, RightState state) {
        super(name, operation, state);
    }

    public Right(String name, Set<Operation> operation, RightState state, Date deadline) {
        super(name, operation, state);
        this.deadline = deadline;
    }

    public Right(String name, Set<Operation> operation) {
        super(name, operation);
        this.state = RightState.granted;
    }

    @Override
    public String toString() {
        return "uk.ac.ncl.rop.Right{" +
                ", counter= " + counter + "state=" + state +
                " deadline= " + deadline +
                '}';
    }

    public Right(String name, Set<Operation> operation, Date deadline) {
        super(name, operation);
        this.state = RightState.granted;
        this.deadline = deadline;
    }

    public Right(Operation operation, RightState state, Date deadline) {
        super(operation, state);
        this.deadline = deadline;
    }

    public Right(Operation operation, RightState state) {
        super(operation, state);
    }

    public Right(Operation operation) {
        super(operation);
        this.state = RightState.granted;
    }

    public Right(Operation operation, Date deadline) {
        super(operation);
        this.state = RightState.granted;
        this.deadline = deadline;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void fulfil() {
        this.state = RightState.granted;
        counter++;
    }

}
