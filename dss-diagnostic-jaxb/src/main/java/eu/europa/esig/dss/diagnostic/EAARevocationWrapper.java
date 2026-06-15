package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlEAARevocationStatus;
import eu.europa.esig.dss.enumerations.EAAStatus;

/**
 * Contains information about the validity of an EAA
 *
 */
public class EAARevocationWrapper extends EAARevocationTokenWrapper {

    /** Wrapped {@code XmlEAARevocationStatus} */
    private final XmlEAARevocationStatus xmlEAARevocationStatus;

    /**
     * Default constructor
     *
     * @param xmlEAARevocationStatus {@link XmlEAARevocationStatus}
     */
    public EAARevocationWrapper(XmlEAARevocationStatus xmlEAARevocationStatus) {
        super(xmlEAARevocationStatus.getEAARevocationToken());
        this.xmlEAARevocationStatus = xmlEAARevocationStatus;
    }

    /**
     * Returns the status of the concerned EAA
     *
     * @return {@link EAAStatus}
     */
    public EAAStatus getStatus() {
        return xmlEAARevocationStatus.getStatus();
    }

}
