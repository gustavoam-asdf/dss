package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.mdoc.MdocHeaderParameter;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;

import java.util.Objects;

/**
 * Default implementation of a {@code eu.europa.esig.dss.eaa.mdoc.creation.MdocDisclosureBuilder}
 * use to build a IssuerSignedItemBytes structure.
 * Example of a produced item:
 * {@code
 *   24(<< {"digestID": 1, "random": h'87A1148380494EF', "elementIdentifier": "given_name", "elementValue": "John"} >>)
 * }
 */
public class DefaultMdocDisclosureBuilder implements MdocDisclosureBuilder {

    /**
     * Default constructor
     */
    public DefaultMdocDisclosureBuilder() {
        // empty
    }

    @Override
    public MdocEAADisclosure build(MdocEAAClaim claim) {
        Objects.requireNonNull(claim, "MdocEAAClaim cannot be null!");

        final CBORMap issuerSignedItem = new CBORMap();
        issuerSignedItem.put(MdocHeaderParameter.DIGEST_ID, claim.getDigestId());
        issuerSignedItem.put(MdocHeaderParameter.RANDOM, claim.getSalt());
        issuerSignedItem.put(MdocHeaderParameter.ELEMENT_IDENTIFIER, claim.getName());
        issuerSignedItem.put(MdocHeaderParameter.ELEMENT_VALUE, claim.getValue());

        return new MdocEAADisclosure(claim.getNamespace(), CBORUtils.toCborBtsrWrappedTagged(issuerSignedItem));
    }

}
