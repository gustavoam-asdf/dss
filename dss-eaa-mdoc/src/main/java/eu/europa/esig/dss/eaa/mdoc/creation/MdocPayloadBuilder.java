package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.cose.COSEKeyFactory;
import eu.europa.esig.dss.cbades.cose.DefaultCOSEKeyFactory;
import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadBuilder;
import eu.europa.esig.dss.eaa.common.creation.EAARevocationList;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class provides access to a configuration to build a payload for an ISO/IEC 18013-5 mdoc EAA.
 *
 */
public class MdocPayloadBuilder extends AbstractEAAPayloadBuilder<MdocEAAPayloadParameters, MdocEAAClaim, MdocEAADisclosure> {

    private static final Logger LOG = LoggerFactory.getLogger(MdocPayloadBuilder.class);

    /**
     * The factory is used to build a representation of a COSE_Key from a {@code java.security.PublicKey}
     * Default : {@code DefaultCOSEKeyFactory}
     */
    private COSEKeyFactory coseKeyFactory = new DefaultCOSEKeyFactory();

    /**
     * Builds disclosures
     */
    private MdocDisclosureBuilder disclosureBuilder = new DefaultMdocDisclosureBuilder();

    /**
     * Provides MdocEAAClaimsBuilder to build claims
     */
    private MdocEAAClaimsBuilderFactory mdocEAAClaimsBuilderFactory;

    /**
     * Empty constructor
     */
    public MdocPayloadBuilder() {
        // empty
    }

    /**
     * (Optional) allows modifying the default behavior for a COSE_Key computation from a {@code java.security.PublicKey}.
     * Default : an instance of {@code COSEKeyFactory} is used, relying on JDK 8 and BouncyCastle utility methods.
     *
     * @param coseKeyFactory {@link COSEKeyFactory}
     */
    public void setCoseKeyFactory(COSEKeyFactory coseKeyFactory) {
        Objects.requireNonNull(coseKeyFactory, "COSEKeyFactory cannot be null!");
        this.coseKeyFactory = coseKeyFactory;
    }

    /**
     * Sets a disclosure builder.
     * Default : {@code eu.europa.esig.dss.eaa.mdoc.creation.DefaultMdocDisclosureBuilder}
     *
     * @param disclosureBuilder {@link MdocDisclosureBuilder}
     */
    public void setDisclosureBuilder(MdocDisclosureBuilder disclosureBuilder) {
        Objects.requireNonNull(disclosureBuilder, "Disclosure builder cannot be null!");
        this.disclosureBuilder = disclosureBuilder;
    }

    /**
     * Gets the MdocEAAClaimsBuilderFactory
     * Default : {@code DefaultMdocEAAClaimsBuilderFactory}
     *
     * @return {@link MdocEAAClaimsBuilderFactory}
     */
    protected MdocEAAClaimsBuilderFactory getMdocClaimProviderFactory() {
        if (mdocEAAClaimsBuilderFactory == null) {
            mdocEAAClaimsBuilderFactory = new DefaultMdocEAAClaimsBuilderFactory();
        }
        return mdocEAAClaimsBuilderFactory;
    }

    /**
     * Sets MdocEAAClaimsBuilderFactory, providing definition of the claims for the given document type.
     * Default : {@code DefaultMdocEAAClaimsBuilderFactory}. Supported docType's are:
     *           "org.iso.18013.5.1.mDL", "org.iso.23220.1.mID", "eu.europa.ec.eudi.pid.1".
     *           For other types it is recommended to provide a custom implementation.
     *
     * @param mdocEAAClaimsBuilderFactory {@link MdocEAAClaimsBuilderFactory}
     */
    public void setMdocClaimProviderFactory(MdocEAAClaimsBuilderFactory mdocEAAClaimsBuilderFactory) {
        this.mdocEAAClaimsBuilderFactory = mdocEAAClaimsBuilderFactory;
    }

    @Override
    public DSSDocument buildPayload(MdocEAAPayloadParameters payloadParameters) {
        Objects.requireNonNull(payloadParameters, "MdocEAAPayloadParameters cannot be null!");
        CBORMap mso = buildMobileSecurityObject(payloadParameters);
        CBORByteString msoBytes = CBORUtils.toCborBtsrWrappedTagged(mso);
        return new InMemoryDocument(CBORUtils.serializeCborObject(msoBytes));
    }

    /**
     * Builds a Mobile Security Object (MSO) as defined in "9.1.2.4 Signing method and structure for MSO"
     * {@code
     *   MobileSecurityObject = {
     *     "version" : tstr,                       ; Version of the MobileSecurityObject
     *     "digestAlgorithm" : tstr,               ; Message digest algorithm used
     *     "valueDigests" : ValueDigests,          ; Digests of all data elements per namespace
     *     "deviceKeyInfo" : DeviceKeyInfo,
     *     "docType" : tstr,                       ; docType as used in Documents
     *     "validityInfo" : ValidityInfo
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildMobileSecurityObject(MdocEAAPayloadParameters payloadParameters) {
        Objects.requireNonNull(payloadParameters.getVersion(), "Version cannot be null!");
        Objects.requireNonNull(payloadParameters.getDigestAlgorithm(), "DigestAlgorithm cannot be null!");
        Objects.requireNonNull(payloadParameters.getDocType(), "DocType cannot be null!");

        final CBORMap mso = new CBORMap();
        mso.put(MdocConstants.VERSION, payloadParameters.getVersion());
        mso.put(MdocConstants.DIGEST_ALGORITHM, payloadParameters.getDigestAlgorithm().getMSOId());
        mso.put(MdocConstants.VALUE_DIGEST, buildValueDigests(payloadParameters));
        mso.put(MdocConstants.DEVICE_KEY_INFO, buildDeviceKeyInfo(payloadParameters));
        mso.put(MdocConstants.DOC_TYPE, payloadParameters.getDocType());
        mso.put(MdocConstants.VALIDITY_INFO, buildValidityInfo(payloadParameters));
        CBORMap status = buildStatus(payloadParameters.getIdentifierList(), payloadParameters.getStatusList());
        if (status != null) {
            mso.put(MdocConstants.STATUS, status);
        }
        return mso;
    }

    /**
     * Builds a ValueDigests based on the set claims.
     * {@code
     *   ValueDigests = {
     *     + NameSpace => DigestIDs
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildValueDigests(MdocEAAPayloadParameters payloadParameters) {
        final CBORMap valueDigests = new CBORMap();
        SecureRandom secureRandom = secureRandom(payloadParameters);
        for (Map.Entry<String, List<MdocEAAClaim>> claimsEntry : getRootPayloadClaims(payloadParameters).entrySet()) {
            String namespace = claimsEntry.getKey();
            List<MdocEAAClaim> claims = claimsEntry.getValue();
            valueDigests.put(namespace, buildDigestIDs(claims, payloadParameters.getDigestAlgorithm(), secureRandom));
        }
        return valueDigests;
    }

    /**
     * Builds a map of elements for each namespace
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return a map between namespaces and corresponding list of claims
     */
    protected Map<String, List<MdocEAAClaim>> getRootPayloadClaims(MdocEAAPayloadParameters payloadParameters) {
        MdocEAAClaimsBuilder mdocEAAClaimsBuilder = getMdocClaimProviderFactory().create(payloadParameters);
        List<MdocEAAClaim> claims = mdocEAAClaimsBuilder.build(payloadParameters);
        return claims.stream().collect(Collectors.groupingBy(
                MdocEAAClaim::getNamespace, LinkedHashMap::new, Collectors.toList()));
    }

    /**
     * Builds a DigestIDs structure.
     * {@code
     *   DigestIDs = {
     *     + DigestID => Digest
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildDigestIDs(List<MdocEAAClaim> claims, DigestAlgorithm digestAlgorithm, SecureRandom secureRandom) {
        if (Utils.isCollectionEmpty(claims)) {
            throw new IllegalStateException("The list of claims is empty!");
        }

        final CBORMap digestIDs = new CBORMap();
        List<MdocEAADisclosure> disclosures = buildDisclosures(claims, secureRandom);
        disclosures.forEach(d -> {
            Digest digest = d.getDigest(digestAlgorithm);
            digestIDs.put(d.getDigestId(), digest.getValue());
        });
        return digestIDs;
    }

    /**
     * Builds a DeviceKeyInfo structure.
     * {@code
     *   DeviceKeyInfo = {
     *     "deviceKey" : DeviceKey
     *     ? "keyAuthorizations" : KeyAuthorizations,
     *     ? "keyInfo" : KeyInfo
     *   }
     *   DeviceKey = COSE_Key
     * }
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return {@link CBORMap}
     */
    protected CBORMap buildDeviceKeyInfo(MdocEAAPayloadParameters payloadParameters) {
        Objects.requireNonNull(payloadParameters.getDeviceKey(), "DeviceKey shall be provided for an mdoc payload building!");
        final CBORMap deviceKeyInfo = new CBORMap();
        deviceKeyInfo.put(MdocConstants.DEVICE_KEY, coseKeyFactory.create(payloadParameters.getDeviceKey()));
        CBORMap keyAuthorizations = buildKeyAuthorizations(payloadParameters.getKeyAuthorizationsMap());
        if (keyAuthorizations != null && !keyAuthorizations.isEmpty()) {
            deviceKeyInfo.put(MdocConstants.KEY_AUTHORIZATIONS, keyAuthorizations);
        }
        CBORMap keyInfo = buildKeyInfo(payloadParameters.getKeyInfoMap());
        if (keyInfo != null && !keyInfo.isEmpty()) {
            deviceKeyInfo.put(MdocConstants.KEY_INFO, keyInfo);
        }
        return deviceKeyInfo;
    }

    /**
     * Builds a KeyAuthorizations structure.
     * {@code
     *   KeyAuthorizations = {
     *     ? "nameSpaces" : AuthorizedNameSpaces
     *     ? "dataElements" : AuthorizedDataElements
     *   }
     *   AuthorizedNameSpaces = [+ NameSpace]
     *   AuthorizedDataElements = {+ NameSpace => DataElementsArray}
     *   DataElementsArray = [+ DataElementIdentifier]
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildKeyAuthorizations(Map<String, List<String>> keyAuthorizationsMap) {
        if (Utils.isMapEmpty(keyAuthorizationsMap)) {
            return null;
        }
        final CBORMap keyAuthorizations = new CBORMap();
        keyAuthorizations.put(MdocConstants.NAMESPACES, new CBORArray(keyAuthorizationsMap.keySet()));
        CBORMap authorizedDataElements = new CBORMap();
        keyAuthorizationsMap.forEach((k, v) -> authorizedDataElements.put(k, new CBORArray(v)));
        keyAuthorizations.put(MdocConstants.DATA_ELEMENTS, authorizedDataElements);
        return keyAuthorizations;
    }

    /**
     * Builds a KeyInfo structure.
     * {@code
     *   KeyInfo = { * int => any}   ; Positive integers are RFU, negative integers may be used for
     *   proprietary use
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildKeyInfo(Map<Integer, Object> keyInfoMap) {
        if (Utils.isMapEmpty(keyInfoMap)) {
            return null;
        }
        final CBORMap keyInfo = new CBORMap();
        keyInfoMap.forEach(keyInfo::put);
        return keyInfo;
    }

    /**
     * Builds a ValidityInfo structure.
     * {@code
     *   ValidityInfo = {
     *     "signed" : tdate,
     *     "validFrom" : tdate,
     *     "validUntil" : tdate,
     *     ? "expectedUpdate" : tdate
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildValidityInfo(MdocEAAPayloadParameters payloadParameters) {
        Objects.requireNonNull(payloadParameters.getSigned(), "signed date cannot be null!");
        Objects.requireNonNull(payloadParameters.getValidFrom(), "validFrom date cannot be null!");
        Objects.requireNonNull(payloadParameters.getValidUntil(), "validUntil date cannot be null!");

        final CBORMap validityInfo = new CBORMap();
        validityInfo.put(MdocConstants.SIGNED, DSSUtils.getTimeValueInSeconds(payloadParameters.getSigned().getTime()));
        validityInfo.put(MdocConstants.VALID_FROM, DSSUtils.getTimeValueInSeconds(payloadParameters.getValidFrom().getTime()));
        validityInfo.put(MdocConstants.VALID_UNTIL, DSSUtils.getTimeValueInSeconds(payloadParameters.getValidUntil().getTime()));
        if (payloadParameters.getExpectedUpdate() != null) {
            validityInfo.put(MdocConstants.EXPECTED_UPDATE, DSSUtils.getTimeValueInSeconds(payloadParameters.getExpectedUpdate().getTime()));
        }
        return validityInfo;
    }

    /**
     * Builds a Status structure.
     * NOTE: The "status" is not defined in ISO/IEC 18013-5:2021,
     * but referenced in the draft of the amendments to the EU Implementing Acts.
     * {@code
     *   Status = {
     *     ? "identifier_list”: IdentifierListInfo,
     *     ? "status_list”: StatusListInfo,
     *     * tstr => RFU
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildStatus(EAARevocationList identifierList, EAARevocationList statusList) {
        // TODO : review with the new revision of ISO/IEC 18013-5
        if (identifierList == null && statusList == null) {
            return null;
        }
        final CBORMap status = new CBORMap();
        CBORMap identifierListInfo = buildIdentifierListInfo(identifierList);
        if (identifierListInfo != null) {
            status.put(MdocConstants.IDENTIFIER_LIST, identifierListInfo);
        }
        CBORMap statusListInfo = buildStatusListInfo(statusList);
        if (statusListInfo != null) {
            status.put(MdocConstants.STATUS_LIST, statusListInfo);
        }
        return status;
    }

    /**
     * Builds an IdentifierListInfo  structure.
     * {@code
     *   IdentifierListInfo = {
     *     "id": Identifier ,
     *     "uri": URI,
     *     ? "certificate": Certificate
     *     * tstr => RFU
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildIdentifierListInfo(EAARevocationList identifierList) {
        if (identifierList == null) {
            return null;
        }
        final CBORMap identifierListInfo = new CBORMap();
        identifierListInfo.put(MdocConstants.IDENTIFIER_ID, identifierList.getIndex());
        identifierListInfo.put(MdocConstants.IDENTIFIER_URI, identifierList.getUri());
        if (identifierList.getCertificate() != null) {
            identifierListInfo.put(MdocConstants.IDENTIFIER_CERTIFICATE, identifierList.getCertificate().getEncoded());
        }
        return identifierListInfo;
    }

    /**
     * Builds an StatusListInfo structure.
     * {@code
     *   StatusListInfo = {
     *     "idx": Identifier,
     *     "uri": URI,
     *     ? "certificate": Certificate
     *     * tstr => RFU
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildStatusListInfo(EAARevocationList statusList) {
        if (statusList == null) {
            return null;
        }
        final CBORMap statusListInfo = new CBORMap();
        statusListInfo.put(MdocConstants.STATUS_IDX, statusList.getIndex());
        statusListInfo.put(MdocConstants.STATUS_URI, statusList.getUri());
        if (statusList.getCertificate() != null) {
            statusListInfo.put(MdocConstants.STATUS_CERTIFICATE, statusList.getCertificate().getEncoded());
        }
        return statusListInfo;
    }

    @Override
    public List<MdocEAADisclosure> buildDisclosures(MdocEAAPayloadParameters payloadParameters) {
        Objects.requireNonNull(payloadParameters, "Payload parameters cannot be null!");
        Objects.requireNonNull(payloadParameters.getDigestAlgorithm(), "Digest algorithm cannot be null!");

        final List<MdocEAADisclosure> result = new ArrayList<>();
        SecureRandom secureRandom = secureRandom(payloadParameters);
        getRootPayloadClaims(payloadParameters).values().forEach(c -> result.addAll(buildDisclosures(c, secureRandom)));
        return result;
    }

    /**
     * Builds disclosures for the given list of {@code claims} using the provided {@code secureRandom}
     *
     * @param claims a list of {@link MdocEAAClaim}s to build disclosures for
     * @param secureRandom {@link SecureRandom} to be used for salt generation, where applicable
     * @return a list of {@link MdocEAADisclosure}s
     */
    protected List<MdocEAADisclosure> buildDisclosures(List<MdocEAAClaim> claims, SecureRandom secureRandom) {
        if (Utils.isCollectionEmpty(claims)) {
            throw new IllegalStateException("The list of claims is empty!");
        }
        final List<MdocEAADisclosure> result = new ArrayList<>();

        for (int i = 0; i < claims.size(); i++) {
            MdocEAAClaim claim = claims.get(i);
            ensureDigestId(claim, i + 1, claims);
            ensureSalt(claim, secureRandom);

            result.add(disclosureBuilder.build(claim));
        }
        return result;
    }

    /**
     * This method ensures the digestId value within the claim for a disclosure building, if not defined
     *
     * @param claim {@link MdocEAAClaim}
     * @param index current index of the claim in the list
     * @param claims a list of all {@link MdocEAAClaim}s within the current namespace
     */
    protected void ensureDigestId(MdocEAAClaim claim, int index, List<MdocEAAClaim> claims) {
        if (claim.getDigestId() == null) {
            while (isDigestIdUsed(index, claims)) {
                ++index;
            }
            claim.setDigestId(index);
            if (LOG.isTraceEnabled()) {
                LOG.trace("DigestId has been added for a claim");
            }
        }
    }

    private boolean isDigestIdUsed(int digestId, List<MdocEAAClaim> claims) {
        return claims.stream().anyMatch(c -> c.getDigestId() != null && digestId == c.getDigestId());
    }

    /**
     * This method ensures the salt value within the claim for a disclosure building, if not defined.
     * This method uses a {@code secureRandomProvider} for the deterministic salt generation
     *
     * @param claim {@link MdocEAAClaim}
     * @param secureRandom {@link SecureRandom}
     */
    protected void ensureSalt(MdocEAAClaim claim, SecureRandom secureRandom) {
        if (claim.getSalt() == null) {
            claim.setSalt(nextRandomSalt(secureRandom));
            if (LOG.isTraceEnabled()) {
                LOG.trace("Salt has been added for a claim");
            }
        }
    }

}
