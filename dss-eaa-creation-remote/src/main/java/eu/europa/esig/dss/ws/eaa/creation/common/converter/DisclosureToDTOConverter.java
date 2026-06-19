package eu.europa.esig.dss.ws.eaa.creation.common.converter;

import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.common.creation.EAADisclosure;
import eu.europa.esig.dss.eaa.jwt.creation.SDJWTEAADisclosure;
import eu.europa.esig.dss.eaa.mdoc.creation.MdocEAADisclosure;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.DisclosureDTO;

import java.util.function.Function;

/**
 * Converts a {@code EAADisclosure} into {@code DisclosureDTO} of a corresponding format
 *
 */
public class DisclosureToDTOConverter implements Function<EAADisclosure, DisclosureDTO> {

    /**
     * Default constructor
     */
    public DisclosureToDTOConverter() {
        super();
    }

    @Override
    public DisclosureDTO apply(EAADisclosure disclosureDTO) {
        if (disclosureDTO instanceof SDJWTEAADisclosure) {
            SDJWTEAADisclosure sdjwteaaDisclosure = (SDJWTEAADisclosure) disclosureDTO;
            return new DisclosureDTO(sdjwteaaDisclosure.getDisclosure());
        } else if (disclosureDTO instanceof MdocEAADisclosure) {
            MdocEAADisclosure mdocEAADisclosure = (MdocEAADisclosure) disclosureDTO;
            String disclosureValueB64 = Utils.toBase64(CBORUtils.serializeCborObject(mdocEAADisclosure.getIssuerSignedItemBytes()));
            return new DisclosureDTO(mdocEAADisclosure.getNamespace(), mdocEAADisclosure.getDigestId(), disclosureValueB64);
        } else {
            throw new UnsupportedOperationException(String.format(
                    "The EAA Disclosure Type '%s' is not supported!", disclosureDTO.getClass().getSimpleName()));
        }
    }

}
