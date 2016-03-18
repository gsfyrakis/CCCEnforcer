package uk.ac.ncl.response;

import java.io.Serializable;

public class CCCResponse implements Serializable {

    private static final long serialVersionUID = 2989890107617783379L;

    private boolean isContractCompliant;

    public CCCResponse(boolean isContractCompliant) {
        this.setContractCompliant(isContractCompliant);
    }

    public CCCResponse() {

    }

    public boolean getContractCompliant() {
        return isContractCompliant;
    }

    public void setContractCompliant(boolean isContractCompliant) {
        this.isContractCompliant = isContractCompliant;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CCCResponse [isContractCompliant=")
                .append(isContractCompliant).append("]");
        return builder.toString();
    }

}
