package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;

/**
 * Builds an IssuerSignedItemBytes structure representing a disclosure object to be used for a hash computation
 *
 */
public interface MdocDisclosureBuilder {

    /**
     * Builds a String for a selectively disclosable EAA claim to be used for Digest computation
     *
     * @param claim {@link MdocEAAClaim} to create a disclosure for
     * @return {@link CBORByteString} representing the IssuerSignedItemBytes structure
     */
    MdocEAADisclosure build(MdocEAAClaim claim);

}
