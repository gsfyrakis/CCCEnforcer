package uk.ac.ncl.rop;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import uk.ac.ncl.event.Operation;
import uk.ac.ncl.state.RopState.ProhibitionState;
import uk.ac.ncl.util.DateParser;

@Entity
@Table(name = "UserProhibition")
public class Prohibition extends RopEntity<ProhibitionState> implements
        Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2821744163127245357L;

    public Prohibition() {
        super();
    }

    public Prohibition(Operation operation, ProhibitionState state) {
        super(operation, state);
    }

    public Prohibition(Operation operation, ProhibitionState state, Date deadline) {
        super(operation, state);
        this.deadline = deadline;
    }


    public Prohibition(Operation operation) {
        super(operation);
        this.state = ProhibitionState.imposed;
    }

    public Prohibition(Operation operation, Date deadline) {
        super(operation);
        this.deadline = deadline;
        this.state = ProhibitionState.imposed;
    }


    @Override
    public void setOperationSet(Set<Operation> operationSet) {
        if (operationSet.size() == 1)
            this.operationSet = operationSet;
        else
            throw new IllegalArgumentException("The size of operation set for prohibtion must be 1 !");
    }

    @Override
    public String toString() {
        return "uk.ac.ncl.rop.Prohibition{" +
                "serialVersionUID=" + serialVersionUID + "deadline: " + deadline + "state: " + state +
                '}';
    }
}
