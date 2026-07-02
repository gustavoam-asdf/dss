package eu.europa.esig.dss.model.lote.identifier;

import eu.europa.esig.dss.model.lote.LoTEInfo;

/**
 * Identifier for a List of Lists of Trusted Entities
 *
 */
public class LoLoTEIdentifier extends AbstractLoTEIdentifier {

    private static final long serialVersionUID = -8109242606546775442L;

    /**
     * Default constructor
     *
     * @param listInfo {@link LoTEInfo} of the target List
     */
    public LoLoTEIdentifier(LoTEInfo listInfo) {
        super("LoLoTE-", listInfo);
    }

}
