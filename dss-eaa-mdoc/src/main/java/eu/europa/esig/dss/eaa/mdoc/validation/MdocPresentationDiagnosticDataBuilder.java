package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.eaa.common.validation.EAAPresentationDiagnosticDataBuilder;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;

/**
 * Builds a diagnostic data for the mdoc EAA presentation
 *
 */
public class MdocPresentationDiagnosticDataBuilder extends EAAPresentationDiagnosticDataBuilder {

    /**
     * Default constructor
     */
    public MdocPresentationDiagnosticDataBuilder() {
        // empty
    }

    @Override
    protected XmlEAAPresentation buildDetachedXmlEAAPresentation(EAAPresentation eaaPresentation) {
        XmlEAAPresentation xmlEAAPresentation = super.buildDetachedXmlEAAPresentation(eaaPresentation);
        MdocEAAPresentation mdocEAAPresentation = (MdocEAAPresentation) eaaPresentation;
        xmlEAAPresentation.setDocumentType(mdocEAAPresentation.getDocumentType());
        return xmlEAAPresentation;
    }

}
