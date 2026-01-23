package eu.europa.esig.dss.model.lote.identifier;

import eu.europa.esig.dss.model.lote.ListInfo;

/**
 * Identifier for a List of Trusted Entities
 *
 */
public class LoTEIdentifier extends AbstractListInfoIdentifier {

    private static final long serialVersionUID = 9104809797778596906L;

    /**
     * Default constructor
     *
     * @param listInfo {@link ListInfo} of the target List
     */
    public LoTEIdentifier(ListInfo listInfo) {
        super("LoTE-", listInfo);
    }

}
