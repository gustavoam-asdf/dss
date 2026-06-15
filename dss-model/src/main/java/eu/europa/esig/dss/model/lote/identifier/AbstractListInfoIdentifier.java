package eu.europa.esig.dss.model.lote.identifier;

import eu.europa.esig.dss.model.identifier.MultipleDigestIdentifier;
import eu.europa.esig.dss.model.lote.ListInfo;

/**
 * Abstract class for LoTE generic identifier
 *
 */
public class AbstractListInfoIdentifier extends MultipleDigestIdentifier {

    private static final long serialVersionUID = 4820142572993996604L;

    /**
     * Default constructor
     *
     * @param prefix {@link String} identifier prefix (e.g. 'LoTE-')
     * @param listInfo {@link ListInfo} of the target List
     */
    protected AbstractListInfoIdentifier(final String prefix, ListInfo listInfo) {
        super(prefix, listInfo.getUrl().getBytes());
    }

}
