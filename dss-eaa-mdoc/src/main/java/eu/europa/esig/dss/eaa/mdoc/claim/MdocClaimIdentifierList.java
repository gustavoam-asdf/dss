package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Represents an IdentifierListInfo object as defined in ETSI TS 119 472-1 (currently in IA draft only)
 * 
 */
public class MdocClaimIdentifierList extends MdocClaimMap implements ClaimStatusList {

    private static final long serialVersionUID = -8431629611618058461L;

    /**
     * Constructor to initialize MdocClaimIdentifierList from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimIdentifierList(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimNumber getIndex() {
        return getAsNumber(MdocConstants.IDENTIFIER_ID);
    }

    @Override
    public ClaimString getUri() {
        return getAsString(MdocConstants.IDENTIFIER_URI);
    }

    @Override
    public ClaimByteString getCertificate() {
        return getAsByteString(MdocConstants.IDENTIFIER_CERTIFICATE);
    }

}
