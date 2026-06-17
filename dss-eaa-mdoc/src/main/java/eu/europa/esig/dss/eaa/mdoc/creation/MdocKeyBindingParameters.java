package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.eaa.common.creation.KeyBindingParameters;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.model.DSSDocument;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Default constructor
     */
    public MdocKeyBindingParameters() {
        // empty
    }

    /**
     * Gets SessionTranscript for generation of a detached paylaod of the mdoc deviceAuth signature
     *
     * @return {@link DSSDocument}
     */
    public DSSDocument getSessionTranscript() {
        return sessionTranscript;
    }

    /**
     * Sets SessionTranscript structure for generation of a detached paylaod of the mdoc deviceAuth signature
     *
     * @param sessionTranscript {@link DSSDocument}
     */
    public void setSessionTranscript(final DSSDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
    }

    /**
     * Gets the document type
     *
     * @return {@link String}
     */
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the document type. Shall be the same as the docType of the EAA.
     *
     * @param docType {@link String}
     */
    public void setDocType(final String docType) {
        this.docType = docType;
    }

    /**
     * Adds a data element to be incorporated within DeviceSigned.nameSpaces structure
     *
     * @param deviceSignedDataElement {@link MdocEAAClaim}
     */
    public void addDeviceSignedDataElement(final MdocEAAClaim deviceSignedDataElement) {
        deviceSignedDataElements.add(deviceSignedDataElement);
    }

    /**
     * Adds a data element to be incorporated within DeviceSigned.nameSpaces structure
     *
     * @param namespace {@link String}
     * @param name {@link String}
     * @param value {@link Object}
     */
    public void addDeviceSignedDataElement(final String namespace, final String name, final Object value) {
        deviceSignedDataElements.add(MdocEAAClaim.create(namespace, name, value));
    }

    @Override
    public List<MdocEAAClaim> getDeviceSignedDataElements() {
        return deviceSignedDataElements;
    }

}
