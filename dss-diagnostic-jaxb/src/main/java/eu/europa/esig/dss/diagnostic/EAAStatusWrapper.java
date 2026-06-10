package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatus;
import eu.europa.esig.dss.enumerations.EAAStatus;

/**
 * Contains information about the validity of an EAA
 *
 */
public class EAAStatusWrapper extends EAAStatusTokenWrapper {

    /** Wrapped {@code XmlEAAStatus} */
    private final XmlEAAStatus xmlEAAStatus;

    /**
     * Default constructor
     *
     * @param xmlEAAStatus {@link XmlEAAStatus}
     */
    public EAAStatusWrapper(XmlEAAStatus xmlEAAStatus) {
        super(xmlEAAStatus.getEAAStatusToken());
        this.xmlEAAStatus = xmlEAAStatus;
    }

    /**
     * Returns the status of the concerned EAA
     *
     * @return {@link EAAStatus}
     */
    public EAAStatus getStatus() {
        return xmlEAAStatus.getStatus();
    }

}
