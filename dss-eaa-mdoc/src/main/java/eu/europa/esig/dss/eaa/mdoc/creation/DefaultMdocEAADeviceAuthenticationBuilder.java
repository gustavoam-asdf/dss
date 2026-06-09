package eu.europa.esig.dss.eaa.mdoc.creation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORObjectFactory;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;

/**
 * Default implementation of {@link MdocEAADeviceAuthenticationBuilder}
 */
public class DefaultMdocEAADeviceAuthenticationBuilder implements MdocEAADeviceAuthenticationBuilder {

    @Override
    public DSSDocument build(final MdocKeyBindingParameters keyBindingParameters) {
        ensureKeyBindingParameters(keyBindingParameters);

        CBORArray deviceAuthentication = new CBORArray();
        try {
            deviceAuthentication.add(MdocConstants.DEVICE_AUTHENTICATION);
            deviceAuthentication.add(CBORUtils.parseCbor(keyBindingParameters.getSessionTranscript()));
            deviceAuthentication.add(keyBindingParameters.getDocType());
            deviceAuthentication.add(getDeviceNameSpacesBytes(keyBindingParameters));
        } catch (Exception e) {
            throw new DSSException(String.format("Unable to build DeviceAuthentication. Reason : %s", e.getMessage()), e);
        }

        CBORByteString deviceAuthenticationBytes = CBORUtils.toCborBtsrWrappedTagged(deviceAuthentication);
        return new InMemoryDocument(CBORUtils.serializeCborObject(deviceAuthenticationBytes));
    }

    protected void ensureKeyBindingParameters(final MdocKeyBindingParameters keyBindingParameters) {
        Objects.requireNonNull(keyBindingParameters, "keyBindingParameters must not be null");
        Objects.requireNonNull(keyBindingParameters.getSessionTranscript(), "SessionTranscript() must not be null");
        Objects.requireNonNull(keyBindingParameters.getDocType(), "DocType must not be null");

        if (!CBORUtils.isCbor(keyBindingParameters.getSessionTranscript())) {
            throw new DSSException("Session transcript must be a CBOR object");
        }
    }

    protected CBORByteString getDeviceNameSpacesBytes(final MdocKeyBindingParameters keyBindingParameters) {
        Map<String, List<MdocEAAClaim>> groupedClaims = keyBindingParameters.getDeviceSignedDataElements()
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
