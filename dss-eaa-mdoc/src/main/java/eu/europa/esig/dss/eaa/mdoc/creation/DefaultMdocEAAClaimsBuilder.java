package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.mdoc.ETSI194721Headers;
import eu.europa.esig.dss.eaa.mdoc.EUDIPIDHeaders;
import eu.europa.esig.dss.eaa.mdoc.ISO180135Headers;
import eu.europa.esig.dss.eaa.mdoc.ISO232202Headers;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDrivingPrivilege;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class is used to provide a proper MdocEAAClaim implementation based on the document type.
 * The class defines default values for some properties, which may not be present on a specific implementation.
 *
 */
public abstract class DefaultMdocEAAClaimsBuilder implements MdocEAAClaimsBuilder {

    /**
     * Default constructor
     */
    protected DefaultMdocEAAClaimsBuilder() {
        // empty
    }

    /**
     * Creates claims for the payload parameters
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return a list of {@link MdocEAAClaim}s
     */
    public List<MdocEAAClaim> build(MdocEAAPayloadParameters payloadParameters) {
        final List<MdocEAAClaim> result = new ArrayList<>();

        /* ETSI technical claims */
        addClaim(result, getIssuanceDate(payloadParameters));
        addClaim(result, getOneTime(payloadParameters));
        addClaim(result, getShortLived(payloadParameters));
        addClaim(result, getCategory(payloadParameters));

        /* Other selectively disclosable claims */

        MdocSelectivelyDisclosableParameters selectivelyDisclosable = payloadParameters.selectivelyDisclosable();
        addClaim(result, getFirstName(selectivelyDisclosable));
        addClaim(result, getLastName(selectivelyDisclosable));
        addClaim(result, getEmail(selectivelyDisclosable));
        addClaim(result, getGender(selectivelyDisclosable));
        addClaim(result, getBirthdate(selectivelyDisclosable));
        addClaim(result, getPhoneNumber(selectivelyDisclosable));
        addClaim(result, getPlaceOfBirth(selectivelyDisclosable));
        addClaim(result, getNationality(selectivelyDisclosable));
        addClaim(result, getNationalities(selectivelyDisclosable));
        addClaim(result, getBirthFirstName(selectivelyDisclosable));
        addClaim(result, getBirthLastName(selectivelyDisclosable));
        addClaim(result, getTitle(selectivelyDisclosable));
        addClaim(result, getMobilePhoneNumber(selectivelyDisclosable));
        addClaim(result, getPseudonym(selectivelyDisclosable));
        addClaim(result, getIssuingCountry(selectivelyDisclosable));
        addClaim(result, getIssuingAuthority(selectivelyDisclosable));
        addClaim(result, getDocumentNumber(selectivelyDisclosable));
        addClaim(result, getPortrait(selectivelyDisclosable));
        addClaim(result, getDrivingPrivileges(selectivelyDisclosable));
        addClaim(result, getUNDistinguishingSign(selectivelyDisclosable));
        addClaim(result, getAdministrativeNumber(selectivelyDisclosable));
        addClaim(result, getHeight(selectivelyDisclosable));
        addClaim(result, getWeight(selectivelyDisclosable));
        addClaim(result, getEyeColor(selectivelyDisclosable));
        addClaim(result, getHairColor(selectivelyDisclosable));
        addClaim(result, getResidentAddress(selectivelyDisclosable));
        addClaim(result, getPortraitCaptureDate(selectivelyDisclosable));
        addClaim(result, getAgeInYears(selectivelyDisclosable));
        addClaim(result, getAgeBirthYear(selectivelyDisclosable));
        addClaims(result, getAgeOverNN(selectivelyDisclosable));
        addClaim(result, getIssuingJurisdiction(selectivelyDisclosable));
        addClaim(result, getResidentCity(selectivelyDisclosable));
        addClaim(result, getResidentState(selectivelyDisclosable));
        addClaim(result, getResidentPostalCode(selectivelyDisclosable));
        addClaim(result, getResidentCountry(selectivelyDisclosable));
        addClaims(result, getBiometricTemplate(selectivelyDisclosable));
        addClaim(result, getBiometricTemplateFace(selectivelyDisclosable));
        addClaim(result, getSignatureUsualMark(selectivelyDisclosable));
        addClaim(result, getFingerprint(selectivelyDisclosable));
        addClaim(result, getBusinessName(selectivelyDisclosable));
        addClaim(result, getOrganizationName(selectivelyDisclosable));
        addClaim(result, getBirthFullName(selectivelyDisclosable));
        addClaim(result, getProfession(selectivelyDisclosable));
        addClaim(result, getRelationshipFather(selectivelyDisclosable));
        addClaim(result, getRelationshipMother(selectivelyDisclosable));
        addClaim(result, getRelationshipParent(selectivelyDisclosable));
        addClaim(result, getRelationshipSon(selectivelyDisclosable));
        addClaim(result, getRelationshipDaughter(selectivelyDisclosable));
        addClaim(result, getRelationshipBrother(selectivelyDisclosable));
        addClaim(result, getRelationshipSister(selectivelyDisclosable));
        addClaim(result, getRelationshipSibling(selectivelyDisclosable));
        addClaim(result, getRelationshipSpouse(selectivelyDisclosable));
        addClaim(result, getRelationshipFatherInLaw(selectivelyDisclosable));
        addClaim(result, getRelationshipMotherInLaw(selectivelyDisclosable));
        addClaim(result, getRelationshipParentInLaw(selectivelyDisclosable));
        addClaim(result, getRelationshipSonInLaw(selectivelyDisclosable));
        addClaim(result, getRelationshipDaughterInLaw(selectivelyDisclosable));
        addClaim(result, getRelationshipChildInLaw(selectivelyDisclosable));
        addClaim(result, getRelationshipParentalAuthority(selectivelyDisclosable));
        addClaim(result, getRelationshipLegalRepresentative(selectivelyDisclosable));
        addClaim(result, getRelationshipAgent(selectivelyDisclosable));
        addClaim(result, getDocumentType(selectivelyDisclosable));
        addClaim(result, getAdministrativeIssuanceDate(selectivelyDisclosable));
        addClaim(result, getAdministrativeExpirationDate(selectivelyDisclosable));
        addClaim(result, getResidentStreet(selectivelyDisclosable));
        addClaim(result, getResidentHouseNumber(selectivelyDisclosable));
        addClaim(result, getTrustAnchor(selectivelyDisclosable));
        addClaim(result, getIssuingAuthorityRegistrationIdentifier(selectivelyDisclosable));
        addClaim(result, getAttestedAttributesSubject(selectivelyDisclosable));

        result.addAll(selectivelyDisclosable.getOtherClaims());

        return result;
    }

    /**
     * Gets mdoc claim generated for the issuanceDate parameter
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getIssuanceDate(MdocEAAPayloadParameters payloadParameters) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getIssuanceDate(payloadParameters);
    }

    /**
     * Gets mdoc claim generated for the shortLived parameter
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getShortLived(MdocEAAPayloadParameters payloadParameters) {
        return ETSI194721EAAClaimsBuilder.getInstance().getShortLived(payloadParameters);
    }

    /**
     * Gets mdoc claim generated for the oneTime parameter
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getOneTime(MdocEAAPayloadParameters payloadParameters) {
        return ETSI194721EAAClaimsBuilder.getInstance().getOneTime(payloadParameters);
    }

    /**
     * Gets mdoc claim generated for the category parameter
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getCategory(MdocEAAPayloadParameters payloadParameters) {
        return ETSI194721EAAClaimsBuilder.getInstance().getCategory(payloadParameters);
    }

    /**
     * Gets mdoc claim generated for the first name parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getFirstName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getFirstName(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the last name parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getLastName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getLastName(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the email parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getEmail(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getEmail(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the gender parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getGender(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getGender(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the birthdate parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getBirthdate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getBirthdate(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the phone number parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getPhoneNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getPhoneNumber(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the place of birth parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getPlaceOfBirth(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getPlaceOfBirth(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the nationality parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getNationality(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getNationality(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the nationalities parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getNationalities(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return EUDIPIDEAAClaimsBuilder.getInstance().getNationalities(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the birth first name parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getBirthFirstName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return EUDIPIDEAAClaimsBuilder.getInstance().getBirthFirstName(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the birth last name parameter
     *v
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getBirthLastName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return EUDIPIDEAAClaimsBuilder.getInstance().getBirthLastName(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the title parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getTitle(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getTitle(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the mobile phone number parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getMobilePhoneNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return EUDIPIDEAAClaimsBuilder.getInstance().getMobilePhoneNumber(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the pseudonym parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getPseudonym(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ETSI194721EAAClaimsBuilder.getInstance().getPseudonym(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the issuing country parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getIssuingCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getIssuingCountry(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the issuing authority parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getIssuingAuthority(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getIssuingAuthority(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the document number parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getDocumentNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getDocumentNumber(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the portrait parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getPortrait(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getPortrait(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the driving privileges parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getDrivingPrivileges(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getDrivingPrivileges(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the UN distinguishing sign parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getUNDistinguishingSign(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getUNDistinguishingSign(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the administrative number parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getAdministrativeNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getAdministrativeNumber(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the height parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getHeight(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getHeight(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the weight parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getWeight(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getWeight(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the eye color parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getEyeColor(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getEyeColor(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the hair color parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getHairColor(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getHairColor(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the resident address parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getResidentAddress(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getResidentAddress(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the portrait capture date parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getPortraitCaptureDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getPortraitCaptureDate(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the age in years parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getAgeInYears(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getAgeInYears(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the age birth year parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getAgeBirthYear(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getAgeBirthYear(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claims generated for the age over NN parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return a list of {@link MdocEAAClaim}s
     */
    protected List<MdocEAAClaim> getAgeOverNN(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getAgeOverNN(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the issuing jurisdiction parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getIssuingJurisdiction(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getIssuingJurisdiction(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the resident city parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getResidentCity(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getResidentCity(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the resident state parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getResidentState(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getResidentState(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the resident postal code parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getResidentPostalCode(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getResidentPostalCode(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the resident country parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getResidentCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getResidentCountry(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claims generated for the biometric template parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return a list of {@link MdocEAAClaim}s
     */
    protected List<MdocEAAClaim> getBiometricTemplate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getBiometricTemplate(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the biometric template face
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim
     */
    protected MdocEAAClaim getBiometricTemplateFace(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getBiometricTemplateFace(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the signature usual mark parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getSignatureUsualMark(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO180135MDLEAAClaimsBuilder.getInstance().getSignatureUsualMark(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the fingerprint parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getFingerprint(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getFingerprint(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the business name parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getBusinessName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getBusinessName(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the organization name parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getOrganizationName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getOrganizationName(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the birth full name parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getBirthFullName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getBirthFullName(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the profession parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getProfession(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getProfession(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship father parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipFather(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipFather(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship mother parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipMother(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipMother(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship parent parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipParent(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipParent(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship son parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipSon(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipSon(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship daughter parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipDaughter(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipDaughter(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship brother parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipBrother(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipBrother(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship sister parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipSister(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipSister(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship sibling parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipSibling(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipSibling(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship spouse parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipSpouse(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipSpouse(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship father in law parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipFatherInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipFatherInLaw(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship mother in law parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipMotherInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipMotherInLaw(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship parent in law parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipParentInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipParentInLaw(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship son in law parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipSonInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipSonInLaw(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship daughter in law parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipDaughterInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipDaughterInLaw(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship child in law parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipChildInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipChildInLaw(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship parental authority parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipParentalAuthority(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipParentalAuthority(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship legal representative parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipLegalRepresentative(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipLegalRepresentative(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the relationship agent parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getRelationshipAgent(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getRelationshipAgent(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the document type parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getDocumentType(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getDocumentType(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the administrative issuance date parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getAdministrativeIssuanceDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getAdministrativeIssuanceDate(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the administrative expiration date parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getAdministrativeExpirationDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getAdministrativeExpirationDate(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the resident street parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getResidentStreet(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ISO232201MIDEAAClaimsBuilder.getInstance().getResidentStreet(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the resident house number parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getResidentHouseNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return EUDIPIDEAAClaimsBuilder.getInstance().getResidentHouseNumber(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the trust anchor parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getTrustAnchor(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return EUDIPIDEAAClaimsBuilder.getInstance().getTrustAnchor(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the issuing authority registration identifier parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getIssuingAuthorityRegistrationIdentifier(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ETSI194721EAAClaimsBuilder.getInstance().getIssuingAuthorityRegistrationIdentifier(selectivelyDisclosable);
    }

    /**
     * Gets mdoc claim generated for the attested attributes subject family name parameter
     *
     * @param selectivelyDisclosable {@link MdocSelectivelyDisclosableParameters}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim getAttestedAttributesSubject(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
        return ETSI194721EAAClaimsBuilder.getInstance().getAttestedAttributesSubject(selectivelyDisclosable);
    }

    /**
     * Gets the namespace for the given claims category
     *
     * @return {@link String}
     */
    protected abstract String getNamespace();

    /**
     * Creates a new MdocEAAClaim using the name, value and the applicable namespace
     *
     * @param name {@link String}
     * @param value {@link Object}
     * @return {@link MdocEAAClaim}
     */
    protected MdocEAAClaim create(String name, Object value) {
        return MdocEAAClaim.create(getNamespace(), name, value);
    }

    /**
     * Adds the {@code claim} to the {@code result} list if not null
     *
     * @param result a list of {@link MdocEAAClaim}s
     * @param claim {@link MdocEAAClaim} to be added
     */
    protected void addClaim(final List<MdocEAAClaim> result, MdocEAAClaim claim) {
        if (claim != null) {
            result.add(claim);
        }
    }

    /**
     * Adds the {@code claim} to the {@code result} list if not null
     *
     * @param result a list of {@link MdocEAAClaim}s
     * @param claims a list of {@link MdocEAAClaim}s to be added
     */
    protected void addClaims(final List<MdocEAAClaim> result, List<MdocEAAClaim> claims) {
        if (Utils.isCollectionNotEmpty(claims)) {
            claims.forEach(c -> addClaim(result, c));
        }
    }

    /**
     * Provides claim definitions for the document conformant to ISO/IEC 18013-5 MDL mdoc.
     */
    protected static final class ISO180135MDLEAAClaimsBuilder extends DefaultMdocEAAClaimsBuilder {

        /** Singleton */
        private static ISO180135MDLEAAClaimsBuilder instance;

        /**
         * Default constructor
         */
        private ISO180135MDLEAAClaimsBuilder() {
            // empty
        }

        /**
         * Gets current instance
         *
         * @return {@link ISO180135MDLEAAClaimsBuilder}
         */
        public static ISO180135MDLEAAClaimsBuilder getInstance() {
            if (instance == null) {
                instance = new ISO180135MDLEAAClaimsBuilder();
            }
            return instance;
        }

        @Override
        protected String getNamespace() {
            return MdocConstants.ISO18013_5_NAMESPACE;
        }

        @Override
        protected MdocEAAClaim getIssuanceDate(MdocEAAPayloadParameters payloadParameters) {
            if (payloadParameters.getIssuanceDate() != null) {
                return create(ISO180135Headers.ISSUE_DATE, payloadParameters.getIssuanceDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getFirstName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getFirstName() != null) {
                return create(ISO180135Headers.GIVEN_NAME, selectivelyDisclosable.getFirstName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getLastName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getLastName() != null) {
                return create(ISO180135Headers.FAMILY_NAME, selectivelyDisclosable.getLastName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getGender(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getGender() != null) {
                return create(ISO180135Headers.SEX, selectivelyDisclosable.getGender());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBirthdate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBirthdate() != null) {
                return create(ISO180135Headers.BIRTH_DATE, CBORUtils.toFullDate(selectivelyDisclosable.getBirthdate()));
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPlaceOfBirth(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPlaceOfBirth() != null) {
                return create(ISO180135Headers.BIRTH_PLACE, selectivelyDisclosable.getPlaceOfBirth());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getNationality(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getNationality() != null) {
                return create(ISO180135Headers.NATIONALITY, selectivelyDisclosable.getNationality());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingCountry() != null) {
                return create(ISO180135Headers.ISSUING_COUNTRY, selectivelyDisclosable.getIssuingCountry());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingAuthority(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingAuthority() != null) {
                return create(ISO180135Headers.ISSUING_AUTHORITY, selectivelyDisclosable.getIssuingAuthority());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getDocumentNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getDocumentNumber() != null) {
                return create(ISO180135Headers.LICENCE_NUMBER, selectivelyDisclosable.getDocumentNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPortrait(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPortrait() != null) {
                return create(ISO180135Headers.PORTRAIT, selectivelyDisclosable.getPortrait());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getDrivingPrivileges(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (Utils.isCollectionNotEmpty(selectivelyDisclosable.getDrivingPrivileges())) {
                final CBORArray drivingPrivileges = new CBORArray();
                for (MdocDrivingPrivilege mdocDrivingPrivilege : selectivelyDisclosable.getDrivingPrivileges()) {
                    CBORMap drivingPrivilege = new CBORMap();
                    drivingPrivilege.put(ISO180135Headers.DRIVING_PRIVILEGES_VEHICLE_CATEGORY_CODE, mdocDrivingPrivilege.getVehicleCategoryCode());
                    if (mdocDrivingPrivilege.getIssueDate() != null) {
                        drivingPrivilege.put(ISO180135Headers.DRIVING_PRIVILEGES_ISSUE_DATE, CBORUtils.toFullDate(mdocDrivingPrivilege.getIssueDate()));
                    }
                    if (mdocDrivingPrivilege.getExpiryDate() != null) {
                        drivingPrivilege.put(ISO180135Headers.DRIVING_PRIVILEGES_EXPIRY_DATE, CBORUtils.toFullDate(mdocDrivingPrivilege.getExpiryDate()));
                    }
                    if (Utils.isCollectionNotEmpty(mdocDrivingPrivilege.getCodes())) {
                        CBORArray codes = new CBORArray();
                        for (MdocDrivingPrivilege.Code mdocCode : mdocDrivingPrivilege.getCodes()) {
                            CBORMap code = new CBORMap();
                            code.put(ISO180135Headers.DRIVING_PRIVILEGES_CODE_CODE, mdocCode.getCode());
                            if (mdocCode.getSign() != null) {
                                code.put(ISO180135Headers.DRIVING_PRIVILEGES_CODE_SIGN, mdocCode.getSign());
                            }
                            if (mdocCode.getValue() != null) {
                                code.put(ISO180135Headers.DRIVING_PRIVILEGES_CODE_VALUE, mdocCode.getValue());
                            }
                            codes.add(code);
                        }
                    }
                    drivingPrivileges.add(drivingPrivilege);
                }
                return create(ISO180135Headers.DRIVING_PRIVILEGES, drivingPrivileges);
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getUNDistinguishingSign(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getUNDistinguishingSign() != null) {
                return create(ISO180135Headers.UN_DISTINGUISHING_SIGN, selectivelyDisclosable.getUNDistinguishingSign());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeNumber() != null) {
                return create(ISO180135Headers.ADMINISTRATIVE_NUMBER, selectivelyDisclosable.getAdministrativeNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getHeight(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getHeight() != null) {
                return create(ISO180135Headers.HEIGHT, selectivelyDisclosable.getHeight());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getWeight(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getWeight() != null) {
                return create(ISO180135Headers.WEIGHT, selectivelyDisclosable.getWeight());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getEyeColor(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getEyeColor() != null) {
                return create(ISO180135Headers.EYE_COLOR, selectivelyDisclosable.getEyeColor());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getHairColor(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getHairColor() != null) {
                return create(ISO180135Headers.HAIR_COLOR, selectivelyDisclosable.getHairColor());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentAddress(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentAddress() != null) {
                return create(ISO180135Headers.RESIDENT_ADDRESS, selectivelyDisclosable.getResidentAddress());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPortraitCaptureDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPortraitCaptureDate() != null) {
                return create(ISO180135Headers.PORTRAIT_CAPTURE_DATE, selectivelyDisclosable.getPortraitCaptureDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAgeInYears(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAgeInYears() != null) {
                return create(ISO180135Headers.AGE_IN_YEARS, selectivelyDisclosable.getAgeInYears());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAgeBirthYear(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAgeBirthYear() != null) {
                return create(ISO180135Headers.AGE_BIRTH_YEAR, selectivelyDisclosable.getAgeBirthYear());
            }
            return null;
        }

        @Override
        protected List<MdocEAAClaim> getAgeOverNN(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (Utils.isMapNotEmpty(selectivelyDisclosable.getAgeOverNN())) {
                final List<MdocEAAClaim> result = new ArrayList<>();
                for (Map.Entry<Integer, Boolean> entry : selectivelyDisclosable.getAgeOverNN().entrySet()) {
                    addClaim(result, create(ISO180135Headers.AGE_OVER_NN + entry.getKey(), entry.getValue()));
                }
                return result;
            }
            return Collections.emptyList();
        }

        @Override
        protected MdocEAAClaim getIssuingJurisdiction(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingJurisdiction() != null) {
                return create(ISO180135Headers.ISSUING_JURISDICTION, selectivelyDisclosable.getIssuingJurisdiction());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentCity(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentCity() != null) {
                return create(ISO180135Headers.RESIDENT_CITY, selectivelyDisclosable.getResidentCity());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentState(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentState() != null) {
                return create(ISO180135Headers.RESIDENT_STATE, selectivelyDisclosable.getResidentState());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentPostalCode(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentPostalCode() != null) {
                return create(ISO180135Headers.RESIDENT_POSTAL_CODE, selectivelyDisclosable.getResidentPostalCode());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentPostalCode() != null) {
                return create(ISO180135Headers.RESIDENT_COUNTRY, selectivelyDisclosable.getResidentPostalCode());
            }
            return null;
        }

        @Override
        protected List<MdocEAAClaim> getBiometricTemplate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (Utils.isMapNotEmpty(selectivelyDisclosable.getBiometricTemplate())) {
                final List<MdocEAAClaim> result = new ArrayList<>();
                for (Map.Entry<String, byte[]> entry : selectivelyDisclosable.getBiometricTemplate().entrySet()) {
                    addClaim(result, create(ISO180135Headers.BIOMETRIC_TEMPLATE_XX + entry.getKey(), entry.getValue()));
                }
                return result;
            }
            return Collections.emptyList();
        }

        @Override
        protected MdocEAAClaim getBiometricTemplateFace(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBiometricTemplateFace() != null) {
                return create(ISO180135Headers.BIOMETRIC_TEMPLATE_FACE, selectivelyDisclosable.getBiometricTemplateFace());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getSignatureUsualMark(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getSignatureUsualMark() != null) {
                return create(ISO180135Headers.SIGNATURE, selectivelyDisclosable.getSignatureUsualMark());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeIssuanceDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeIssuanceDate() != null) {
                return create(ISO180135Headers.ISSUE_DATE, selectivelyDisclosable.getAdministrativeIssuanceDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeExpirationDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeExpirationDate() != null) {
                return create(ISO180135Headers.EXPIRY_DATE, selectivelyDisclosable.getAdministrativeExpirationDate());
            }
            return null;
        }

    }

    /**
     * Provides claim definitions for the document conformant to ISO/IEC 23220-1 MID mdoc.
     */
    protected static final class ISO232201MIDEAAClaimsBuilder extends DefaultMdocEAAClaimsBuilder {

        /** Singleton */
        private static ISO232201MIDEAAClaimsBuilder instance;

        /**
         * Default constructor
         */
        private ISO232201MIDEAAClaimsBuilder() {
            // empty
        }

        /**
         * Gets current instance
         *
         * @return {@link ISO232201MIDEAAClaimsBuilder}
         */
        public static ISO232201MIDEAAClaimsBuilder getInstance() {
            if (instance == null) {
                instance = new ISO232201MIDEAAClaimsBuilder();
            }
            return instance;
        }

        @Override
        public String getNamespace() {
            return MdocConstants.ISO23220_1_NAMESPACE;
        }

        @Override
        protected MdocEAAClaim getIssuanceDate(MdocEAAPayloadParameters payloadParameters) {
            if (payloadParameters.getIssuanceDate() != null) {
                return create(ISO232202Headers.ISSUE_DATE, payloadParameters.getIssuanceDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getFirstName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getFirstName() != null) {
                return create(ISO232202Headers.GIVEN_NAME_UNICODE, selectivelyDisclosable.getFirstName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getLastName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getLastName() != null) {
                return create(ISO232202Headers.FAMILY_NAME_UNICODE, selectivelyDisclosable.getLastName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getEmail(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getEmail() != null) {
                return create(ISO232202Headers.EMAIL_ADDRESS, selectivelyDisclosable.getEmail());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getGender(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getGender() != null) {
                return create(ISO232202Headers.SEX, selectivelyDisclosable.getGender());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBirthdate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBirthdate() != null) {
                return create(ISO232202Headers.BIRTH_DATE, CBORUtils.toFullDate(selectivelyDisclosable.getBirthdate()));
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPhoneNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPhoneNumber() != null) {
                return create(ISO232202Headers.TELEPHONE_NUMBER, selectivelyDisclosable.getPhoneNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPlaceOfBirth(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPlaceOfBirth() != null) {
                return create(ISO232202Headers.BIRTHPLACE, selectivelyDisclosable.getPlaceOfBirth());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getNationality(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getNationality() != null) {
                return create(ISO232202Headers.NATIONALITY, selectivelyDisclosable.getNationality());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getTitle(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getTitle() != null) {
                return create(ISO232202Headers.TITLE, selectivelyDisclosable.getTitle());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingCountry() != null) {
                return create(ISO232202Headers.ISSUING_COUNTRY, selectivelyDisclosable.getIssuingCountry());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingAuthority(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingAuthority() != null) {
                // TODO : review in ISO 23220-2:2026
                return create(ISO232202Headers.ISSUING_AUTHORITY_UNICODE, selectivelyDisclosable.getIssuingAuthority());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getDocumentNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getDocumentNumber() != null) {
                return create(ISO232202Headers.DOCUMENT_NUMBER, selectivelyDisclosable.getDocumentNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPortrait(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPortrait() != null) {
                return create(ISO232202Headers.PORTRAIT, selectivelyDisclosable.getPortrait());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getHeight(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getHeight() != null) {
                return create(ISO232202Headers.HEIGHT, selectivelyDisclosable.getHeight());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getWeight(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getWeight() != null) {
                return create(ISO232202Headers.WEIGHT, selectivelyDisclosable.getWeight());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentAddress(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentAddress() != null) {
                return create(ISO232202Headers.RESIDENT_ADDRESS_UNICODE, selectivelyDisclosable.getResidentAddress());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPortraitCaptureDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPortraitCaptureDate() != null) {
                return create(ISO232202Headers.PORTRAIT_CAPTURE_DATE, selectivelyDisclosable.getPortraitCaptureDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAgeInYears(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAgeInYears() != null) {
                return create(ISO232202Headers.AGE_IN_YEARS, selectivelyDisclosable.getAgeInYears());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAgeBirthYear(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAgeBirthYear() != null) {
                return create(ISO232202Headers.AGE_BIRTH_YEAR, selectivelyDisclosable.getAgeBirthYear());
            }
            return null;
        }

        @Override
        protected List<MdocEAAClaim> getAgeOverNN(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (Utils.isMapNotEmpty(selectivelyDisclosable.getAgeOverNN())) {
                final List<MdocEAAClaim> result = new ArrayList<>();
                for (Map.Entry<Integer, Boolean> entry : selectivelyDisclosable.getAgeOverNN().entrySet()) {
                    addClaim(result, create(ISO232202Headers.AGE_OVER_NN + entry.getKey(), entry.getValue()));
                }
                return result;
            }
            return Collections.emptyList();
        }

        @Override
        protected MdocEAAClaim getIssuingJurisdiction(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingJurisdiction() != null) {
                return create(ISO232202Headers.ISSUING_SUBDIVISION, selectivelyDisclosable.getIssuingJurisdiction());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentCity(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentCity() != null) {
                return create(ISO232202Headers.RESIDENT_CITY_UNICODE, selectivelyDisclosable.getResidentCity());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentPostalCode(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentPostalCode() != null) {
                return create(ISO232202Headers.RESIDENT_POSTAL_CODE, selectivelyDisclosable.getResidentPostalCode());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentPostalCode() != null) {
                return create(ISO232202Headers.RESIDENT_COUNTRY, selectivelyDisclosable.getResidentPostalCode());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBiometricTemplateFace(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBiometricTemplateFace() != null) {
                return create(ISO232202Headers.BIOMETRIC_TEMPLATE_FACE, selectivelyDisclosable.getBiometricTemplateFace());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getFingerprint(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getSignatureUsualMark() != null) {
                return create(ISO232202Headers.FINGERPRINT, selectivelyDisclosable.getSignatureUsualMark());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBusinessName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBusinessName() != null) {
                return create(ISO232202Headers.BUSINESS_NAME_UNICODE, selectivelyDisclosable.getBusinessName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getOrganizationName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getOrganizationName() != null) {
                return create(ISO232202Headers.ORGANIZATION_NAME_UNICODE, selectivelyDisclosable.getOrganizationName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBirthFullName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBirthFullName() != null) {
                return create(ISO232202Headers.NAME_AT_BIRTH, selectivelyDisclosable.getBirthFullName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getProfession(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getProfession() != null) {
                return create(ISO232202Headers.PROFESSION, selectivelyDisclosable.getProfession());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipFather(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipFather() != null) {
                return create(ISO232202Headers.RELATIONSHIP_FATHER, selectivelyDisclosable.getRelationshipFather());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipMother(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipMother() != null) {
                return create(ISO232202Headers.RELATIONSHIP_MOTHER, selectivelyDisclosable.getRelationshipMother());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipParent(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipParent() != null) {
                return create(ISO232202Headers.RELATIONSHIP_PARENT, selectivelyDisclosable.getRelationshipParent());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipSon(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipSon() != null) {
                return create(ISO232202Headers.RELATIONSHIP_SON, selectivelyDisclosable.getRelationshipSon());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipDaughter(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipDaughter() != null) {
                return create(ISO232202Headers.RELATIONSHIP_DAUGHTER, selectivelyDisclosable.getRelationshipDaughter());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipBrother(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipBrother() != null) {
                return create(ISO232202Headers.RELATIONSHIP_BROTHER, selectivelyDisclosable.getRelationshipBrother());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipSister(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipSister() != null) {
                return create(ISO232202Headers.RELATIONSHIP_SISTER, selectivelyDisclosable.getRelationshipSister());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipSibling(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipSibling() != null) {
                return create(ISO232202Headers.RELATIONSHIP_SIBLING, selectivelyDisclosable.getRelationshipSibling());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipSpouse(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipSpouse() != null) {
                return create(ISO232202Headers.RELATIONSHIP_SPOUSE, selectivelyDisclosable.getRelationshipSpouse());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipFatherInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipFatherInLaw() != null) {
                return create(ISO232202Headers.RELATIONSHIP_FATHER_IN_LAW, selectivelyDisclosable.getRelationshipFatherInLaw());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipMotherInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipMotherInLaw() != null) {
                return create(ISO232202Headers.RELATIONSHIP_MOTHER_IN_LAW, selectivelyDisclosable.getRelationshipMotherInLaw());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipParentInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipParentInLaw() != null) {
                return create(ISO232202Headers.RELATIONSHIP_PARENT_IN_LAW, selectivelyDisclosable.getRelationshipParentInLaw());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipSonInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipSonInLaw() != null) {
                return create(ISO232202Headers.RELATIONSHIP_SON_IN_LAW, selectivelyDisclosable.getRelationshipSonInLaw());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipDaughterInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipDaughterInLaw() != null) {
                return create(ISO232202Headers.RELATIONSHIP_DAUGHTER_IN_LAW, selectivelyDisclosable.getRelationshipDaughterInLaw());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipChildInLaw(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipChildInLaw() != null) {
                return create(ISO232202Headers.RELATIONSHIP_CHILD_IN_LAW, selectivelyDisclosable.getRelationshipChildInLaw());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipParentalAuthority(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipParentalAuthority() != null) {
                return create(ISO232202Headers.RELATIONSHIP_PARENTAL_AUTHORITY, selectivelyDisclosable.getRelationshipParentalAuthority());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipLegalRepresentative(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipLegalRepresentative() != null) {
                return create(ISO232202Headers.RELATIONSHIP_LEGAL_REPRESENTATIVE, selectivelyDisclosable.getRelationshipLegalRepresentative());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getRelationshipAgent(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getRelationshipAgent() != null) {
                return create(ISO232202Headers.RELATIONSHIP_AGENT, selectivelyDisclosable.getRelationshipAgent());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getDocumentType(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getDocumentType() != null) {
                return create(ISO232202Headers.DOCUMENT_TYPE, selectivelyDisclosable.getDocumentType());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeIssuanceDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeIssuanceDate() != null) {
                return create(ISO232202Headers.ISSUE_DATE, selectivelyDisclosable.getAdministrativeIssuanceDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeExpirationDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeExpirationDate() != null) {
                return create(ISO232202Headers.EXPIRY_DATE, selectivelyDisclosable.getAdministrativeExpirationDate());
            }
            return null;
        }

    }

    /**
     * Provides claim definitions for the document conformant to PID Rulebook specification.
     */
    protected static final class EUDIPIDEAAClaimsBuilder extends DefaultMdocEAAClaimsBuilder {

        /** Singleton */
        private static EUDIPIDEAAClaimsBuilder instance;

        /**
         * Default constructor
         */
        private EUDIPIDEAAClaimsBuilder() {
            // empty
        }

        /**
         * Gets current instance
         *
         * @return {@link EUDIPIDEAAClaimsBuilder}
         */
        public static EUDIPIDEAAClaimsBuilder getInstance() {
            if (instance == null) {
                instance = new EUDIPIDEAAClaimsBuilder();
            }
            return instance;
        }

        @Override
        public String getNamespace() {
            return MdocConstants.EUDI_PID_NAMESPACE;
        }

        @Override
        protected MdocEAAClaim getFirstName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getFirstName() != null) {
                return create(EUDIPIDHeaders.GIVEN_NAME, selectivelyDisclosable.getFirstName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getLastName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getLastName() != null) {
                return create(EUDIPIDHeaders.FAMILY_NAME, selectivelyDisclosable.getLastName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getEmail(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getEmail() != null) {
                return create(EUDIPIDHeaders.EMAIL_ADDRESS, selectivelyDisclosable.getEmail());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getGender(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getGender() != null) {
                return create(EUDIPIDHeaders.SEX, selectivelyDisclosable.getGender());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBirthdate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBirthdate() != null) {
                return create(EUDIPIDHeaders.BIRTH_DATE, CBORUtils.toFullDate(selectivelyDisclosable.getBirthdate()));
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPlaceOfBirth(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPlaceOfBirthCountry() != null ||
                    selectivelyDisclosable.getPlaceOfBirthRegion() != null ||
                    selectivelyDisclosable.getPlaceOfBirthLocality() != null) {
                final CBORMap placeOfBirth = new CBORMap();
                if (selectivelyDisclosable.getPlaceOfBirthCountry() != null) {
                    placeOfBirth.put(EUDIPIDHeaders.PLACE_OF_BIRTH_COUNTRY, selectivelyDisclosable.getPlaceOfBirthCountry());
                }
                if (selectivelyDisclosable.getPlaceOfBirthRegion() != null) {
                    placeOfBirth.put(EUDIPIDHeaders.PLACE_OF_BIRTH_REGION, selectivelyDisclosable.getPlaceOfBirthRegion());
                }
                if (selectivelyDisclosable.getPlaceOfBirthLocality() != null) {
                    placeOfBirth.put(EUDIPIDHeaders.PLACE_OF_BIRTH_LOCALITY, selectivelyDisclosable.getPlaceOfBirthLocality());
                }
                return create(EUDIPIDHeaders.PLACE_OF_BIRTH, placeOfBirth);
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getNationalities(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (Utils.isCollectionNotEmpty(selectivelyDisclosable.getNationalities())) {
                return create(EUDIPIDHeaders.NATIONALITY, selectivelyDisclosable.getNationalities());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBirthFirstName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBirthFirstName() != null) {
                return create(EUDIPIDHeaders.GIVEN_NAME_BIRTH, selectivelyDisclosable.getBirthFirstName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getBirthLastName(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getBirthLastName() != null) {
                return create(EUDIPIDHeaders.FAMILY_NAME_BIRTH, selectivelyDisclosable.getBirthLastName());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getMobilePhoneNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getMobilePhoneNumber() != null) {
                return create(EUDIPIDHeaders.MOBILE_PHONE_NUMBER, selectivelyDisclosable.getMobilePhoneNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingCountry() != null) {
                return create(EUDIPIDHeaders.ISSUING_COUNTRY, selectivelyDisclosable.getIssuingCountry());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingAuthority(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingAuthority() != null) {
                return create(EUDIPIDHeaders.ISSUING_AUTHORITY, selectivelyDisclosable.getIssuingAuthority());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getDocumentNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getDocumentNumber() != null) {
                return create(EUDIPIDHeaders.DOCUMENT_NUMBER, selectivelyDisclosable.getDocumentNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPortrait(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPortrait() != null) {
                return create(EUDIPIDHeaders.PORTRAIT, selectivelyDisclosable.getPortrait());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeNumber() != null) {
                return create(EUDIPIDHeaders.PERSONAL_ADMINISTRATIVE_NUMBER, selectivelyDisclosable.getAdministrativeNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentAddress(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentAddress() != null) {
                return create(EUDIPIDHeaders.RESIDENT_ADDRESS, selectivelyDisclosable.getResidentAddress());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingJurisdiction(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingJurisdiction() != null) {
                return create(EUDIPIDHeaders.ISSUING_JURISDICTION, selectivelyDisclosable.getIssuingJurisdiction());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentCity(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentCity() != null) {
                return create(EUDIPIDHeaders.RESIDENT_CITY, selectivelyDisclosable.getResidentCity());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentState(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentState() != null) {
                return create(EUDIPIDHeaders.RESIDENT_STATE, selectivelyDisclosable.getResidentState());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentPostalCode(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentPostalCode() != null) {
                return create(EUDIPIDHeaders.RESIDENT_POSTAL_CODE, selectivelyDisclosable.getResidentPostalCode());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentCountry(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentPostalCode() != null) {
                return create(EUDIPIDHeaders.RESIDENT_COUNTRY, selectivelyDisclosable.getResidentPostalCode());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeIssuanceDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeIssuanceDate() != null) {
                return create(EUDIPIDHeaders.ISSUANCE_DATE, selectivelyDisclosable.getAdministrativeIssuanceDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAdministrativeExpirationDate(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAdministrativeExpirationDate() != null) {
                return create(EUDIPIDHeaders.EXPIRY_DATE, selectivelyDisclosable.getAdministrativeExpirationDate());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentStreet(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentStreet() != null) {
                return create(EUDIPIDHeaders.RESIDENT_STREET, selectivelyDisclosable.getResidentStreet());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getResidentHouseNumber(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getResidentHouseNumber() != null) {
                return create(EUDIPIDHeaders.RESIDENT_HOUSE_NUMBER, selectivelyDisclosable.getResidentHouseNumber());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getTrustAnchor(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getTrustAnchor() != null) {
                return create(EUDIPIDHeaders.TRUST_ANCHOR, selectivelyDisclosable.getTrustAnchor());
            }
            return null;
        }

    }

    /**
     * Provides claim definitions for the document conformant to PID Rulebook specification.
     */
    protected static final class ETSI194721EAAClaimsBuilder extends DefaultMdocEAAClaimsBuilder {

        /** Singleton */
        private static ETSI194721EAAClaimsBuilder instance;

        /**
         * Default constructor
         */
        private ETSI194721EAAClaimsBuilder() {
            // empty
        }

        /**
         * Gets current instance
         *
         * @return {@link ETSI194721EAAClaimsBuilder}
         */
        public static ETSI194721EAAClaimsBuilder getInstance() {
            if (instance == null) {
                instance = new ETSI194721EAAClaimsBuilder();
            }
            return instance;
        }

        @Override
        public String getNamespace() {
            return MdocConstants.ETSI_19472_1_NAMESPACE;
        }

        @Override
        protected MdocEAAClaim getShortLived(MdocEAAPayloadParameters payloadParameters) {
            if (payloadParameters.isShortLived()) {
                return create(ETSI194721Headers.SHORT_LIVED, payloadParameters.isShortLived());
            }
            return null;
        }


        @Override
        protected MdocEAAClaim getOneTime(MdocEAAPayloadParameters payloadParameters) {
            if (payloadParameters.isOneTime()) {
                return create(ETSI194721Headers.SHORT_LIVED, payloadParameters.isOneTime());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getCategory(MdocEAAPayloadParameters payloadParameters) {
            if (payloadParameters.getCategory() != null) {
                return create(ETSI194721Headers.CATEGORY, payloadParameters.getCategory());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getPseudonym(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getPseudonym() != null) {
                return create(ETSI194721Headers.ALSO_KNOWN_AS, selectivelyDisclosable.getPseudonym());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getIssuingAuthorityRegistrationIdentifier(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getIssuingAuthorityRegistrationIdentifier() != null) {
                return create(ETSI194721Headers.ISSUING_REGISTRATION_IDENTIFIER, selectivelyDisclosable.getIssuingAuthorityRegistrationIdentifier());
            }
            return null;
        }

        @Override
        protected MdocEAAClaim getAttestedAttributesSubject(MdocSelectivelyDisclosableParameters selectivelyDisclosable) {
            if (selectivelyDisclosable.getAttestedAttributesSubjectFamilyName() != null &&
                    selectivelyDisclosable.getAttestedAttributesSubjectGivenName() != null &&
                    selectivelyDisclosable.getAttestedAttributesSubjectDocumentNumber() != null) {
                final CBORMap subAttr = new CBORMap();
                CBORMap subId = new CBORMap();
                subId.put(ETSI194721Headers.SUB_ATTRS_ID_FAMILY_NAME, selectivelyDisclosable.getAttestedAttributesSubjectFamilyName());
                subId.put(ETSI194721Headers.SUB_ATTRS_ID_GIVEN_NAME, selectivelyDisclosable.getAttestedAttributesSubjectGivenName());
                subId.put(ETSI194721Headers.SUB_ATTRS_ID_DOCUMENT_NUMBER, selectivelyDisclosable.getAttestedAttributesSubjectDocumentNumber());
                subAttr.put(ETSI194721Headers.SUB_ATTRS_ID, subId);
                return create(ETSI194721Headers.SUB_ATTRS, subAttr);

            } else if (selectivelyDisclosable.getAttestedAttributesSubjectPseudonym() != null) {
                final CBORMap subAttr = new CBORMap();
                subAttr.put(ETSI194721Headers.SUB_ATTRS_AKA, selectivelyDisclosable.getAttestedAttributesSubjectPseudonym());
                return create(ETSI194721Headers.SUB_ATTRS, subAttr);
            }
            return null;
        }

    }

}
