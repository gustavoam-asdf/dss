package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.statuslist.EAAStatusSource;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.SignatureValidationContext;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Performs validation of EAA tokens. During validation, retrieved the corresponding information,
 * including the data required for a signature validation, and/or EAA status verification.
 */
public class EAAValidationContext extends SignatureValidationContext {

    private static final Logger LOG = LoggerFactory.getLogger(EAAValidationContext.class);

    /**
     * A set of EAAs to process
     */
    private final Set<EAA> processedEAAs = new LinkedHashSet<>();

    /**
     * A set of EAA Status tokens to process
     */
    private final Set<EAAStatusToken> processedEAAStatusTokens = new LinkedHashSet<>();

    /**
     * Source used to verify status of the EAA
     */
    private EAAStatusSource eaaStatusSource;

    /**
     * Default constructor instantiating object with null or empty values and current time
     */
    public EAAValidationContext() {
        this(new Date());
    }

    /**
     * Constructor instantiating object with null or empty values and provided time
     *
     * @param validationTime {@link Date} validation time to be used during the execution
     */
    public EAAValidationContext(Date validationTime) {
        super(validationTime);
    }

    /**
     * Sets the EAAStatusSource used for retrieving an information about a status of the EAAs
     *
     * @param eaaStatusSource {@link EAAStatusSource}
     */
    public void setEAAStatusSource(EAAStatusSource eaaStatusSource) {
        this.eaaStatusSource = eaaStatusSource;
    }

    /**
     * Adds an {@code EAA} to be verified
     *
     * @param eaa {@link EAA}
     */
    public void addEAAForVerification(final EAA eaa) {
        if (eaa == null) {
            return;
        }

        addEAACertificateSources(eaa);

        prepareSignatures(eaa);

        final boolean added = processedEAAs.add(eaa);
        if (LOG.isTraceEnabled()) {
            if (added) {
                LOG.trace("EAA added to processedEAAs: {} ", eaa.getId());
            } else {
                LOG.trace("EAA already present processedEAAs: {} ", eaa.getId());
            }
        }
    }

    private void prepareSignatures(EAA eaa) {
        List<AdvancedSignature> signatures = eaa.getSignatures();
        if (Utils.isCollectionNotEmpty(signatures)) {
            for (AdvancedSignature signature : signatures) {
                addSignatureForVerification(signature);
            }
        }
        AdvancedSignature keyBindingSignature = eaa.getKeyBindingSignature();
        if (keyBindingSignature != null) {
            addSignatureForVerification(keyBindingSignature);
        }
    }

    private void addEAACertificateSources(EAA eaa) {
        CertificateSource deviceKeyCertificateSource = eaa.getDeviceKeyCertificateSource();
        if (deviceKeyCertificateSource != null) {
            addDocumentCertificateSource(deviceKeyCertificateSource);
        }
    }

    /**
     * Adds an {@code EAAStatusToken} to be verified
     *
     * @param eaaStatusToken {@link EAAStatusToken}
     */
    public void addEAAStatusTokenForVerification(final EAAStatusToken eaaStatusToken) {
        if (eaaStatusToken == null) {
            return;
        }

        addSignatureForVerification(eaaStatusToken.getSignature());
        addDocumentCertificateSource(eaaStatusToken.getCertificateSource());

        final boolean added = processedEAAStatusTokens.add(eaaStatusToken);
        if (LOG.isTraceEnabled()) {
            if (added) {
                LOG.trace("EAA Status Token added to processedEAAStatusTokens: {} ", eaaStatusToken.getDSSIdAsString());
            } else {
                LOG.trace("EAA already present processedEAAStatusTokens: {} ", eaaStatusToken.getDSSIdAsString());
            }
        }
    }

    @Override
    public void validate() {
        for (EAA eaa : processedEAAs) {
            findEAAStatusData(eaa);
        }
        super.validate();
    }

    /**
     * Fetches the EAA status token for the {@code eaa}, when required
     *
     * @param eaa {@link EAA} to get status for
     */
    private void findEAAStatusData(EAA eaa) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Checking status data for : {}", eaa.getId());
        }

        if (isEAAStatusCheckRequired(eaa)) {
            if (eaaStatusSource == null) {
                LOG.info("No EAAStatusSource has been provided. EAA status check is skipped.");
                return;
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("EAA status check is in progress for EAA : {}", eaa.getId());
            }

            EAAStatusToken eaaStatusToken = eaaStatusSource.getEAAStatus(eaa);
            if (eaaStatusToken != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Obtained a new EAA Status token : {}, for EAA : {}",
                            eaaStatusToken.getDSSIdAsString(), eaa.getId());
                }
                addEAAStatusTokenForVerification(eaaStatusToken);
            }

        } else if (LOG.isDebugEnabled()) {
            LOG.debug("Status data is not required for EAA : {}", eaa.getId());
        }
    }

    /**
     * This method verifies whether the {@code eaa} requires the status verification
     *
     * @param eaa {@link EAA}
     * @return TRUE if the EAA status should be checked, FALSE otherwise
     */
    protected boolean isEAAStatusCheckRequired(EAA eaa) {
        if (eaa.getPayload() != null && eaa.getPayload().getShortLived() != null) {
            Boolean shortLived = eaa.getPayload().getShortLived().getBooleanValue();
            return shortLived != null && !Utils.isTrue(shortLived);
        }
        return true;
    }

}
