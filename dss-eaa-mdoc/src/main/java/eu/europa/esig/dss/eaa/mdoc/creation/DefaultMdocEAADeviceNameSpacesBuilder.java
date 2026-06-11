package eu.europa.esig.dss.eaa.mdoc.creation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORObjectFactory;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;

/**
 * Default implementation of {@link MdocEAADeviceNameSpacesBuilder}
 */
public class DefaultMdocEAADeviceNameSpacesBuilder implements MdocEAADeviceNameSpacesBuilder {

    @Override
    public CBORByteString buildDeviceNameSpacesBytes(final MdocEAADeviceSignedParameters mdocEAADeviceSignedParameters) {
        Map<String, List<MdocEAAClaim>> groupedClaims = mdocEAADeviceSignedParameters.getDeviceSignedDataElements()
                .stream()
                .collect(Collectors.groupingBy(MdocEAAClaim::getNamespace, LinkedHashMap::new, Collectors.toList()));

        CBORMap deviceNameSpaces = new CBORMap();

        for (Map.Entry<String, List<MdocEAAClaim>> entry : groupedClaims.entrySet()) {
            CBORMap map = new CBORMap();
            for (MdocEAAClaim claim : entry.getValue()) {
                CBORObject object = CBORObjectFactory.toCBORObject(claim.getValue());
                map.put(claim.getName(), object);
            }

            deviceNameSpaces.put(entry.getKey(), map);
        }

        return CBORUtils.toCborBtsrWrappedTagged(deviceNameSpaces);
    }
}
