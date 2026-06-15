package eu.europa.esig.dss.eaa.mdoc.creation;

import java.util.ArrayList;
import java.util.List;

import eu.europa.esig.dss.eaa.common.creation.KeyBindingParameters;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.model.DSSDocument;

/**
 * Implementation of {@link KeyBindingParameters} for ISO/IEC 18013-5 mdoc EAA.
 */
public class MdocKeyBindingParameters implements KeyBindingParameters, MdocEAADeviceSignedParameters {

    /** The session transcript to use for the creation of the key binding signature */
    private DSSDocument sessionTranscript;

    /** Doc type to use for the key binding signature, the value should be the same as the one in {@link MdocEAAPayloadParameters} */
    private String docType;

    /** The list of device signed data elements */
    private final List<MdocEAAClaim> deviceSignedDataElements = new ArrayList<>();

    public DSSDocument getSessionTranscript() {
        return sessionTranscript;
    }

    public void setSessionTranscript(final DSSDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(final String docType) {
        this.docType = docType;
    }

    public void addDeviceSignedDataElement(final MdocEAAClaim deviceSignedDataElement) {
        deviceSignedDataElements.add(deviceSignedDataElement);
    }

    public void addDeviceSignedDataElement(final String namespace, final String name, final Object value) {
        deviceSignedDataElements.add(MdocEAAClaim.create(namespace, name, value));
    }

    @Override
    public List<MdocEAAClaim> getDeviceSignedDataElements() {
        return deviceSignedDataElements;
    }
}
