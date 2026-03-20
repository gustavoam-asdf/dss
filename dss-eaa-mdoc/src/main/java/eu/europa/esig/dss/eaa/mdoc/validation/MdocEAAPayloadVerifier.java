package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.common.validation.EAAPayloadVerifier;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.MdocUtils;
import eu.europa.esig.dss.eaa.mdoc.model.MdocIssuerSignedItem;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class verifies issuer signed items, when provided, and computes the combined version of
 * the EAA payload, which includes the MobileSecurityObject as defined in ISO 18013-5
 * "9.1.2.4 Signing method and structure for MSO" as well as issuer signed items.
 * This class requires execution of {@code #verify} method before accessing the validation results.
 *
 */
public class MdocEAAPayloadVerifier extends EAAPayloadVerifier {

    private static final Logger LOG = LoggerFactory.getLogger(MdocEAAPayloadVerifier.class);

    /**
     * Payload to be verified
     */
    private final CBORObject cborPayload;

    /**
     * Default constructor
     *
     * @param cborPayload {@link String} JSON payload to be verified
     */
    public MdocEAAPayloadVerifier(final CBORObject cborPayload) {
        Objects.requireNonNull(cborPayload, "Payload cannot be null!");
        this.cborPayload = cborPayload;
    }

    /**
     * This method performs the verification process for the provided payload and disclosures
     * NOTE: The process can be executed only once
     */
    @Override
    public void verify() {
        ClaimMap originalPayloadMap = parseCborPayload();
        this.disclosureValidations = new ArrayList<>();
        this.digestAlgorithm = getDigestAlgorithm(originalPayloadMap);
        ClaimMap verifiedPayloadMap = (ClaimMap) buildClaimWithDisclosures(originalPayloadMap);
        this.verifiedPayload = new MdocEAAPayload(verifiedPayloadMap);
    }

    /**
     * Parses the {@code cborPayload} to a {@code ClaimMap} object
     *
     * @return {@link ClaimMap}
     */
    protected ClaimMap parseCborPayload() {
        CBORMap mso = getMobileSecurityObject();
        return (ClaimMap) MdocUtils.createClaim(mso);
    }

    private CBORMap getMobileSecurityObject() {
        if (!cborPayload.isByteString()) {
            throw new IllegalInputException("COSE payload shall be encoded as a CBOR byte string!");
        }
        try {
            CBORByteString payloadByteString = (CBORByteString) cborPayload;
            CBORObject msoObject = CBORUtils.parseCbor(payloadByteString.getValueAsBytes());
            if (!msoObject.isByteString()) {
                throw new IllegalInputException("MobileSecurityObjectBytes shall be encoded as a CBOR byte string!");
            }
            return new CBORMap((CBORByteString) msoObject);

        } catch (Exception e) {
            throw new IllegalInputException(String.format(
                    "An error occurred on MobileSecurityObject processing : %s", e.getMessage()), e);
        }
    }

    private DigestAlgorithm getDigestAlgorithm(ClaimMap originalPayloadMap) {
        ClaimString digestAlgorithm = originalPayloadMap.getAsString(MdocConstants.DIGEST_ALGORITHM);
        if (digestAlgorithm != null) {
            String msoDigestAlgorithmId = digestAlgorithm.getValueAsString();
            try {
                return DigestAlgorithm.forMSO(msoDigestAlgorithmId);
            } catch (IllegalArgumentException e) {
                LOG.warn("Unable to find a corresponding DigestAlgorithm for the value extracted " +
                        "from a MobileSecurityObject '{}'!", msoDigestAlgorithmId);
            }
        }
        return null;
    }

    @Override
    protected boolean isSignedDisclosuresHeader(String headerName) {
        return MdocConstants.VALUE_DIGEST.equals(headerName);
    }

    @Override
    protected List<Claim> buildSelectivelyDisclosableClaimsFromClaim(Claim valueDigestsClaim) {
        if (!valueDigestsClaim.isMapValueType()) {
            LOG.warn("valueDigests header shall be of a CBOR Map type!");
            return Collections.emptyList();
        }

        final List<Claim> result = new ArrayList<>();

        Map<String, Claim> valueDigestsMap = valueDigestsClaim.getMapValue();
        for (Map.Entry<String, Claim> valueDigestsEntry : valueDigestsMap.entrySet()) {
            String namespace = valueDigestsEntry.getKey();
            Claim digestIDs = valueDigestsEntry.getValue();
            if (!digestIDs.isMapValueType()) {
                LOG.warn("DigestIDs object shall be of a CBOR Map type! The value is skipped.");
                continue;
            }

            for (Map.Entry<String, Claim> digestIDsEntry : digestIDs.getMapValue().entrySet()) {
                String digestId = digestIDsEntry.getKey();
                if (!Utils.isStringDigits(digestId)) {
                    LOG.warn("DigestID key shall be represented by an unsigned integer! The value is skipped.");
                    continue;
                }
                Claim digest = digestIDsEntry.getValue();

                List<Disclosure> disclosureCandidates = getDisclosureByNamespaceAndId(namespace, Long.parseLong(digestId));
                Claim claim = buildSelectivelyDisclosableClaim(digest, disclosureCandidates);
                if (claim != null) {
                    result.add(claim);
                }
            }

        }
        return result;
    }

    private List<Disclosure> getDisclosureByNamespaceAndId(String namespace, Long digestId) {
        return disclosures.stream()
                .filter(d -> namespace.equals(((MdocIssuerSignedItem) d).getNamespace()) && digestId.equals(((MdocIssuerSignedItem) d).getDigestId()))
                .collect(Collectors.toList());
    }

    @Override
    protected boolean isToSkipHeader(String headerName) {
        return MdocConstants.DIGEST_ALGORITHM.equals(headerName);
    }

    @Override
    protected Claim createClaim(String claimName, Claim parentClaim, Object claimValue, boolean isSelectivelyDisclosable) {
        return MdocUtils.createClaim(claimName, parentClaim, claimValue, isSelectivelyDisclosable);
    }

    @Override
    protected Claim getClaimHashItem(Claim claim) {
        // not applicable for mdoc
        return null;
    }

    @Override
    protected DisclosureValidation validateHashClaim(Claim hashClaim, List<Disclosure> disclosures) {
        DisclosureValidation disclosureValidation = super.validateHashClaim(hashClaim, disclosures);
        disclosureValidation.setId(hashClaim.getName());
        return disclosureValidation;
    }

    @Override
    protected byte[] getHashBytes(Claim hashClaim) {
        if (!hashClaim.isBinaryValueType()) {
            LOG.warn("Digest object shall be of a CBOR Byte String type! The value is skipped.");
            return null;
        }
        return hashClaim.getBinaryValue();
    }

}
