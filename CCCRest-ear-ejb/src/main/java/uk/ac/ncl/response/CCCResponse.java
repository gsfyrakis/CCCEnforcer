package uk.ac.ncl.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class CCCResponse implements Serializable {

    private static final long serialVersionUID = 2989890107617783379L;

    private boolean isContractCompliant;


    private String message = "";

    public CCCResponse(boolean isContractCompliant) {
        this.setContractCompliant(isContractCompliant);
    }

    public CCCResponse() {

    }

    @XmlElement
    public boolean getContractCompliant() {
        return isContractCompliant;
    }

    public void setContractCompliant(boolean isContractCompliant) {
        this.isContractCompliant = isContractCompliant;
    }

    @Override
    public String toString() {
        return "<result>\n" +
                "<contractcompliant>" + isContractCompliant + "</contractcompliant>\n" +
                "<message>" + message + "</message>\n" +
                " </result>";
    }

    @XmlElement
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
