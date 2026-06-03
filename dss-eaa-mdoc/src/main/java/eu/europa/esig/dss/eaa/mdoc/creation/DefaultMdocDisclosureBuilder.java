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
        issuerSignedItem.put(MdocHeaderParameter.DIGEST_ID.toString(), claim.getDigestId());
        issuerSignedItem.put(MdocHeaderParameter.RANDOM.toString(), claim.getSalt());
        issuerSignedItem.put(MdocHeaderParameter.ELEMENT_IDENTIFIER.toString(), claim.getName());
        issuerSignedItem.put(MdocHeaderParameter.ELEMENT_VALUE.toString(), claim.getValue());

        if (claim.isVoid()) {
            return new MdocEAADisclosure(claim.getDigestId(), CBORUtils.toCborBtsrWrappedTagged(issuerSignedItem));
        } else {
            return new MdocEAADisclosure(claim.getNamespace(), claim.getDigestId(), CBORUtils.toCborBtsrWrappedTagged(issuerSignedItem));
        }
    }

}
