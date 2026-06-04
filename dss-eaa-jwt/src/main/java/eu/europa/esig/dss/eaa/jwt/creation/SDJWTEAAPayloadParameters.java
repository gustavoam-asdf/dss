package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadParameters;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides configuration for the SD-JWT VC payload creation
 *
 */
public class SDJWTEAAPayloadParameters extends AbstractEAAPayloadParameters {

    /** EAA issuer subject */
    private String issuer;

    /** EAA subject */
    private String subject;

    /** Contains the type of the key used for the device authentication. */
    private String deviceKeyType;

    /** Contains the certificate chain used for the device authentication. */
    private List<CertificateToken> deviceX509CertificateChain;

    /** Contains the digest of the certificate used for the device authentication. */
    private Digest deviceX509CertificateThumbprint;

    /** Contains the location where the certificate used for device authentication can be accessed from. */
    private String deviceX509CertificateUrl;

    /** Catalogue of parameters to be made selectively disclosable */
    private final SDJWTClaimParameters selectivelyDisclosableParameters = new SDJWTClaimParameters();

    /** Catalogue of parameters to be made non-selectively disclosable */
    private final SDJWTClaimParameters nonSelectivelyDisclosableParameters = new SDJWTClaimParameters();

    /**
     * Default constructor to instantiate SD-JWT VC Payload parameters
     */
    public SDJWTEAAPayloadParameters() {
        // empty
    }

    /**
     * Gets the EAA issuer subject
     *
     * @return {@link String}
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the EAA issue subject
     *
     * @param issuer {@link String}
     */
    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    /**
     * Gets the EAA subject
     *
     * @return {@link String}
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the EAA subject
     *
     * @param subject {@link String}
     */
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * Gets the type of the key used for the device authentication
     *
     * @return {@link String}
     */
    public String getDeviceKeyType() {
        return deviceKeyType;
    }

    /**
     * Sets the type of the key used for the device authentication.
     * To be used only when a representation of an EAA subject certificate is provided, but not a device public key.
     *
     * @param deviceKeyType {@link String}
     */
    public void setDeviceKeyType(String deviceKeyType) {
        this.deviceKeyType = deviceKeyType;
    }

    /**
     * Gets a certificate chain used for the device authentication
     *
     * @return a list of {@link CertificateToken}s
     */
    public List<CertificateToken> getDeviceX509CertificateChain() {
        return deviceX509CertificateChain;
    }

    /**
     * Sets a certificate used for the device authentication
     *
     * @param deviceX509Certificate {@link CertificateToken}
     */
    public void setDeviceX509Certificate(CertificateToken deviceX509Certificate) {
        if (deviceX509Certificate == null) {
            this.deviceX509CertificateChain = null;
        } else {
            this.deviceX509CertificateChain = Collections.singletonList(deviceX509Certificate);
        }
    }

    /**
     * Sets a certificate chain used for the device authentication
     *
     * @param deviceX509CertificateChain an array of {@link CertificateToken}s
     */
    public void setDeviceX509CertificateChain(CertificateToken... deviceX509CertificateChain) {
        if (Utils.isArrayEmpty(deviceX509CertificateChain)) {
            this.deviceX509CertificateChain = null;
        } else {
            this.deviceX509CertificateChain = Arrays.asList(deviceX509CertificateChain);
        }
    }

    /**
     * Sets a certificate chain used for the device authentication
     *
     * @param deviceX509CertificateChain a list of {@link CertificateToken}s
     */
    public void setDeviceX509CertificateChain(List<CertificateToken> deviceX509CertificateChain) {
        this.deviceX509CertificateChain = deviceX509CertificateChain;
    }

    /**
     * Gets digest of the certificate used for the device authentication
     *
     * @return {@link Digest}
     */
    public Digest getDeviceX509CertificateThumbprint() {
        return deviceX509CertificateThumbprint;
    }

    /**
     * Sets digest of the certificate used for the device authentication
     *
     * @param deviceX509CertificateThumbprint {@link Digest}
     */
    public void setDeviceX509CertificateThumbprint(Digest deviceX509CertificateThumbprint) {
        this.deviceX509CertificateThumbprint = deviceX509CertificateThumbprint;
    }

    /**
     * Gets location of the certificate used for the device authentication
     *
     * @return {@link String}
     */
    public String getDeviceX509CertificateUrl() {
        return deviceX509CertificateUrl;
    }

    /**
     * Sets location of the certificate used for the device authentication
     *
     * @param deviceX509CertificateUrl {@link String}
     */
    public void setDeviceX509CertificateUrl(String deviceX509CertificateUrl) {
        this.deviceX509CertificateUrl = deviceX509CertificateUrl;
    }

    /**
     * Gets a catalogue of claims to be made selectively disclosable within the produced SD-JWT VC EAA.
     * When parameters are defined within the object, the computed hashes will be computed and
     * incorporated within "_sd" header parameter of the EAA Payload.
     * To provide the plain values on presentation, the disclosures shall be generated.
     *
     * @return {@link SDJWTClaimParameters}
     */
    public SDJWTClaimParameters selectivelyDisclosable() {
        return selectivelyDisclosableParameters;
    }

    /**
     * Gets a catalogue of claims to be mase non-selectively disclosable and
     * thus to be included within the SD-JWT VC EAA Payload in the plain form.
     *
     * @return {@link SDJWTClaimParameters}
     */
    public SDJWTClaimParameters nonSelectivelyDisclosable() {
        return nonSelectivelyDisclosableParameters;
    }

    @Override
    public String toString() {
        return "SDJWTEAAPayloadParameters [" +
                "issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", selectivelyDisclosableParameters=" + selectivelyDisclosableParameters +
                ", nonSelectivelyDisclosableParameters=" + nonSelectivelyDisclosableParameters +
                "] " + super.toString();
    }

}
