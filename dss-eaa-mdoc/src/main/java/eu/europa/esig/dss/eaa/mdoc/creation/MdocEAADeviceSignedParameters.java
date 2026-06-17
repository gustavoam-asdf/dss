package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;

import java.util.List;

/**
 * Represents parameters configuration for filling DeviceSigned structure as defined
 * in ISO/IEC 18013-5 for key binding signature creation
 *
 */
public interface MdocEAADeviceSignedParameters {

    /**
     * Gets a list of DeviceSigned.nameSpaces structure elements
     *
     * @return a list of {@link MdocEAAClaim}s
     */
    List<MdocEAAClaim> getDeviceSignedDataElements();

}
