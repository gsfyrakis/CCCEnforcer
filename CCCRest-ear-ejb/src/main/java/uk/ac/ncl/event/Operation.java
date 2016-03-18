package uk.ac.ncl.event;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Operation {
    @Enumerated(EnumType.STRING)
    private OperationName name;

    private String object;

    private String type;

    public Operation(OperationName name, String type, String object) {
        this.name = name;
        this.type = type;
        this.object = object;
    }

    public Operation() {

    }

    public enum OperationName {
        register, upload, remove, addDependency, update, download,
    }

    public OperationName getName() {
        return name;
    }

    public String getObject() {
        return object;
    }

    public String getType() {
        return type;
    }

    public void setName(OperationName name) {
        this.name = name;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "uk.ac.ncl.event.Operation{" +
                "name=" + name +
                ", object='" + object + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Operation))
            return false;
        Operation operation = (Operation) obj;
        return (name == null ? operation.name == null : name
                .equals(operation.name))
                && (object == null ? operation.object == null : object
                .equals(operation.object))
                && (type == null ? operation.type == null : type
                .equals(operation.type));

    }

    @Override
    public int hashCode() {
        int hc = 17;
        hc = 37 * hc + (name == null ? 0 : name.hashCode());
        hc = 37 * hc + (object == null ? 0 : object.hashCode());
        return 37 * hc + (type == null ? 0 : type.hashCode());

    }

    public boolean contain(Object obj) {
        if (this.equals(obj))
            return true;
        else if (!(obj instanceof Operation))
            return false;
        else {
            Operation operation = (Operation) obj;
            return (name == null ? operation.name == null : name
                    .equals(operation.name))
                    && (type == null ? operation.type == null : type
                    .equals(operation.type))
                    && (object == null);
        }
    }
}
