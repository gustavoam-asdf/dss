package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default MdocEAAClaimsBuilderFactory loading one of the supported implementations.
 */
public class DefaultMdocEAAClaimsBuilderFactory implements MdocEAAClaimsBuilderFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMdocEAAClaimsBuilderFactory.class);

    /**
     * Default constructor
     */
    public DefaultMdocEAAClaimsBuilderFactory() {
        // empty
    }

    @Override
    public MdocEAAClaimsBuilder create(MdocEAAPayloadParameters payloadParameters) {
        String docType = payloadParameters.getDocType();
        if (MdocConstants.ISO18013_5_MDL_DOC_TYPE.equals(docType)) {
            return DefaultMdocEAAClaimsBuilder.ISO180135MDLEAAClaimsBuilder.getInstance();
        } else if (MdocConstants.ISO23220_1_MID_DOC_TYPE.equals(docType)) {
            return DefaultMdocEAAClaimsBuilder.ISO232201MIDEAAClaimsBuilder.getInstance();
        } else if (MdocConstants.EUDI_PID_DOC_TYPE.equals(docType)) {
            return DefaultMdocEAAClaimsBuilder.EUDIPIDEAAClaimsBuilder.getInstance();
        }
        LOG.warn("The docType '{}' is not supported by the implementation! Default implementation of MdocClaimProvider is selected." +
                "Should you need a specific configuration, please configure a custom MdocClaimProvider.", docType);
        return DefaultMdocEAAClaimsBuilder.ISO232201MIDEAAClaimsBuilder.getInstance(); // default
    }

}
