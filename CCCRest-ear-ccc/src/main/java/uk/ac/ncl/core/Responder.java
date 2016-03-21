package uk.ac.ncl.core;

/**
 * The Class Responder.
 */

public class Responder {
    private Boolean isContractCompliant = false;

    private String message = "";

    public Responder() {
    }

    public Responder(boolean contractCompliant) {
        this.isContractCompliant = contractCompliant;
    }

    public void setContractCompliant(Boolean isContractCompliant) {
        this.isContractCompliant = isContractCompliant;
    }

    public Boolean getContractCompliant() {
        return this.isContractCompliant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
