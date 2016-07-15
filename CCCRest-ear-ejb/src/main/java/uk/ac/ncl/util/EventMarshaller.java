package uk.ac.ncl.util;

import uk.ac.ncl.event.Event;
import uk.ac.ncl.response.CCCResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * marshall event to xml files
 */
public class EventMarshaller {

    /* helper method for marshaling event object */
    private static int eventCounter = 0;

    public static void marshalEvent(Event event) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Event.class);
        // Marshal to System.out
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(event, System.out);
        File file = new File("event-" + eventCounter + ".xml");
        marshaller.marshal(event, file);
        eventCounter++;
    }

    public static void marshalCCCResponse(CCCResponse cccResponse) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(CCCResponse.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(cccResponse, System.out);
        File file = new File("event-" + eventCounter + ".xml");
        marshaller.marshal(cccResponse, file);
        eventCounter++;
    }
}
