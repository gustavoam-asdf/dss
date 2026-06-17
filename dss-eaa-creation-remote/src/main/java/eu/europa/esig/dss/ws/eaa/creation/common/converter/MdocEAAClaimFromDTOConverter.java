package eu.europa.esig.dss.ws.eaa.creation.common.converter;

import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.ClaimDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.ClaimValueDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Converts a {@code ClaimDTO} into an {@code MdocEAAClaim}
 *
 */
public class MdocEAAClaimFromDTOConverter extends AbstractEAAClaimFromDTOConverter<MdocEAAClaim> {

    /**
     * Default constructor
     */
    public MdocEAAClaimFromDTOConverter() {
        super();
    }

    @Override
    public MdocEAAClaim apply(ClaimDTO claimDTO) {
        Objects.requireNonNull(claimDTO, "ClaimDTO cannot be null!");
        verifyClaimValueDTO(claimDTO.getValue());

        return MdocEAAClaim.create(claimDTO.getNamespace(), claimDTO.getDigestId(), claimDTO.getName(),
                getValue(claimDTO.getValue()), claimDTO.getSalt());
    }

    /**
     * Gets a plain value of the {@code claimValueDTO}
     *
     * @param claimValueDTO {@link ClaimValueDTO}
     * @return {@link Object}
     */
    protected Object getValue(ClaimValueDTO claimValueDTO) {
        if (claimValueDTO.getStringValue() != null) {
            return claimValueDTO.getStringValue();
        } else if (claimValueDTO.getNumberValue() != null) {
            return claimValueDTO.getNumberValue();
        } else if (claimValueDTO.getBooleanValue() != null) {
            return claimValueDTO.getBooleanValue();
        } else if (claimValueDTO.getBinaryValue() != null) {
            return claimValueDTO.getBinaryValue();
        } else if (claimValueDTO.getDateValue() != null) {
            return claimValueDTO.getDateValue();
        } else if (claimValueDTO.getArrayValue() != null) {
            List<Object> result = new ArrayList<>();
            claimValueDTO.getArrayValue().forEach(i -> result.add(apply(i).getValue()));
            return result;
        } else if (claimValueDTO.getObjectValue() != null) {
            Map<String, Object> result = new HashMap<>();
            claimValueDTO.getObjectValue().forEach(i -> result.put(i.getName(), apply(i).getValue()));
            return result;
        }
        return null;
    }

}
