package eu.europa.esig.dss.model.lote.identifier;

import eu.europa.esig.dss.model.lote.LoTEInfo;

/**
 * Identifier for a List of Trusted Entities
 *
 */
public class LoTEIdentifier extends AbstractLoTEIdentifier {

    private static final long serialVersionUID = 9104809797778596906L;

    /**
     * Default constructor
     *
     * @param listInfo {@link LoTEInfo} of the target List
     */
    public LoTEIdentifier(LoTEInfo listInfo) {
        super("LoTE-", listInfo);
    }

}
