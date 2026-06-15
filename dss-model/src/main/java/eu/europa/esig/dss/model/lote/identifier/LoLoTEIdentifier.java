package eu.europa.esig.dss.model.lote.identifier;

import eu.europa.esig.dss.model.lote.ListInfo;

/**
 * Identifier for a List of Lists of Trusted Entities
 *
 */
public class LoLoTEIdentifier extends AbstractListInfoIdentifier {

    private static final long serialVersionUID = -8109242606546775442L;

    /**
     * Default constructor
     *
     * @param listInfo {@link ListInfo} of the target List
     */
    public LoLoTEIdentifier(ListInfo listInfo) {
        super("LoLoTE-", listInfo);
    }

}
