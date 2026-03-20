package eu.europa.esig.dss.spi.validation.analyzer.eaa;

import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.validation.analyzer.DocumentAnalyzer;

import java.util.List;

/**
 * Interface to perform validation of a presentation of Electronic Attestation of Attributes (EAA)
 *
 */
public interface EAAPresentationAnalyzer extends DocumentAnalyzer {

    /**
     * Gets extracted list of Electronic Attestation of Attributes (EAA) presentations
     *
     * @return a list of {@link EAAPresentation}s
     */
    List<EAAPresentation> getEAAPresentations();

}
