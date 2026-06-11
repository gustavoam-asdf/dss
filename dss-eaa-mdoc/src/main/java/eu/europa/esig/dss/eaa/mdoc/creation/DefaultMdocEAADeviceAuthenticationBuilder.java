package eu.europa.esig.dss.eaa.mdoc.creation;

import java.util.Objects;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
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
            deviceAuthentication.add(getDeviceNameSpacesBuilder().buildDeviceNameSpacesBytes(keyBindingParameters));
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

    protected MdocEAADeviceNameSpacesBuilder getDeviceNameSpacesBuilder(){
        return new DefaultMdocEAADeviceNameSpacesBuilder();
    }
}
