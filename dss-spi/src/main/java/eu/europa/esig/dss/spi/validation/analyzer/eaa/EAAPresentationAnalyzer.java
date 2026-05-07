package eu.europa.esig.dss.spi.validation.analyzer.eaa;

import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.validation.analyzer.DocumentAnalyzer;

/**
 * Interface to perform validation of a presentation of Electronic Attestation of Attributes (EAA)
 *
 */
public interface EAAPresentationAnalyzer extends DocumentAnalyzer {

    /**
     * Gets extracted Electronic Attestation of Attributes (EAA) presentation
     *
     * @return {@link EAAPresentation}
     */
    EAAPresentation getEAAPresentation();

}
