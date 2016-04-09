package uk.ac.ncl.event;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The Class BusinessEvent provides an entity for eventhistory database table.
 */
@Entity
@Table(name = "Eventhistory")
public class Event implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4281936788602102625L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "OPERATION")),
            @AttributeOverride(name = "type", column = @Column(name = "TYPE")),
            @AttributeOverride(name = "object", column = @Column(name = "OBJECT"))
    })
    private Operation operation;

    @Column(name = "TIMESTAMP", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private EventStatus status = EventStatus.unChecked;


    public Event(String username, Operation operation) {
        super();
        this.username = username;
        this.operation = operation;
    }


    public Event(String username, Operation operation, EventStatus status) {
        super();
        this.username = username;
        this.operation = operation;
        this.status = status;
    }


    public Event(String username, Operation operation, EventStatus status, Date timestamp) {
        super();
        this.username = username;
        this.operation = operation;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "uk.ac.ncl.event.Event{" +
                "serialVersionUID=" + serialVersionUID +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", operation=" + operation +
                ", timestamp=" + timestamp +
                ", status=" + status +
                '}';
    }
}
