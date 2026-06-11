package eu.europa.esig.dss.eaa.mdoc.creation;

import java.util.List;

import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;

public interface MdocEAADeviceSignedParameters {

    List<MdocEAAClaim> getDeviceSignedDataElements();
}
