package uk.ac.ncl.rop;

import uk.ac.ncl.event.Operation;
import uk.ac.ncl.state.RopState;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RopEntity<ropState extends RopState> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8495681371100274412L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Column(name = "NAME")
    String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "OPERATION")),
            @AttributeOverride(name = "type", column = @Column(name = "TYPE")),
            @AttributeOverride(name = "object", column = @Column(name = "OBJECT"))})
    Set<Operation> operationSet;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    ropState state;

    @Column(name = "Deadline")
    Date deadline;

    public RopEntity() {

    }

    public RopEntity(String name, Set<Operation> operationSet, ropState state) {
        this.name = name;
        this.operationSet = operationSet;
        this.state = state;
    }

    public RopEntity(String name, Set<Operation> operationSet) {
        this.name = name;
        this.operationSet = operationSet;
    }

    public RopEntity(Operation operation, ropState state) {
        this.name = operation.toString();
        Set<Operation> operationSet = new HashSet<Operation>();
        operationSet.add(operation);
        this.operationSet = operationSet;
        this.state = state;
    }

    public RopEntity(Operation operation) {
        this.name = operation.toString();
        Set<Operation> operationSet = new HashSet<Operation>();
        operationSet.add(operation);
        this.operationSet = operationSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RopState getState() {
        return state;
    }

    public void setState(ropState state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Operation> getOperationSet() {
        return operationSet;
    }

    public void setOperationSet(Set<Operation> operationSet) {
        this.operationSet = operationSet;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + operationSet.toString() + " " + state;
    }

    public boolean hasOperation(Operation operation) {
        if (operation == null) {
            return false;
        } else {
            for (Operation result : operationSet) {
                if (operation.equals(result))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RopEntity<?>))
            return false;
        RopEntity<?> rop = (RopEntity<?>) obj;
        return (name == null ? rop.name == null : name.equals(rop.name))
                && (operationSet == null ? rop.operationSet == null : operationSet.equals(rop.operationSet));
    }

    @Override
    public int hashCode() {
        int hc = 17;
        hc = 37 * hc + (name == null ? 0 : name.hashCode());
        return 37 * hc + (operationSet == null ? 0 : operationSet.hashCode());
    }
}
