package eu.europa.esig.dss.model.lote.identifier;

import eu.europa.esig.dss.model.identifier.MultipleDigestIdentifier;
import eu.europa.esig.dss.model.lote.LoTEInfo;

/**
 * Abstract class for LoTE generic identifier
 *
 */
public class AbstractLoTEIdentifier extends MultipleDigestIdentifier {

    private static final long serialVersionUID = 4820142572993996604L;

    /**
     * Default constructor
     *
     * @param prefix {@link String} identifier prefix (e.g. 'LoTE-')
     * @param listInfo {@link LoTEInfo} of the target List
     */
    protected AbstractLoTEIdentifier(final String prefix, LoTEInfo listInfo) {
        super(prefix, listInfo.getUrl().getBytes());
    }

}
