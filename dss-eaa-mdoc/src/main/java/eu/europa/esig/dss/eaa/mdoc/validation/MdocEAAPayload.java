package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.eaa.mdoc.claim.MdocClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimCredentialSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimIntegrity;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAAPayload;

import java.util.List;

public class MdocEAAPayload extends MdocClaimMap implements EAAPayload {

    public MdocEAAPayload(final ClaimMap verifiedPayloadMap) {
        super(verifiedPayloadMap.getMapValue());
    }

    @Override
    public ClaimString getIdentifier() {
        return null;
    }

    @Override
    public ClaimString getIssuer() {
        return null;
    }

    @Override
    public ClaimString getSubject() {
        return null;
    }

    @Override
    public ClaimArray getAudience() {
        return null;
    }

    @Override
    public ClaimDate getExpirationTime() {
        return null;
    }

    @Override
    public ClaimDate getNotBeforeTime() {
        return null;
    }

    @Override
    public ClaimDate getIssuedAtTime() {
        return null;
    }

    @Override
    public ClaimDate getUpdatedAtTime() {
        return null;
    }

    @Override
    public ClaimString getCategory() {
        return null;
    }

    @Override
    public ClaimString getMetadataType() {
        return null;
    }

    @Override
    public ClaimIntegrity getMetadataIntegrity() {
        return null;
    }

    @Override
    public ClaimStatus getStatus() {
        return null;
    }

    @Override
    public ClaimString getNonce() {
        return null;
    }

    @Override
    public ClaimString getFullName() {
        return null;
    }

    @Override
    public ClaimString getFirstName() {
        return null;
    }

    @Override
    public ClaimString getLastName() {
        return null;
    }

    @Override
    public ClaimString getMiddleName() {
        return null;
    }

    @Override
    public ClaimString getNickname() {
        return null;
    }

    @Override
    public ClaimString getShortName() {
        return null;
    }

    @Override
    public ClaimString getProfileUrl() {
        return null;
    }

    @Override
    public ClaimString getPictureUrl() {
        return null;
    }

    @Override
    public ClaimString getWebsiteUrl() {
        return null;
    }

    @Override
    public ClaimString getEmail() {
        return null;
    }

    @Override
    public ClaimBoolean getEmailVerified() {
        return null;
    }

    @Override
    public ClaimString getGender() {
        return null;
    }

    @Override
    public ClaimDate getBirthdate() {
        return null;
    }

    @Override
    public ClaimString getTimezone() {
        return null;
    }

    @Override
    public ClaimString getLocale() {
        return null;
    }

    @Override
    public ClaimAddress getAddress() {
        return null;
    }

    @Override
    public ClaimString getPhoneNumber() {
        return null;
    }

    @Override
    public ClaimBoolean getPhoneNumberVerified() {
        return null;
    }

    @Override
    public ClaimPlaceOfBirth getPlaceOfBirth() {
        return null;
    }

    @Override
    public ClaimArray getNationalities() {
        return null;
    }

    @Override
    public ClaimString getBirthFirstName() {
        return null;
    }

    @Override
    public ClaimString getBirthLastName() {
        return null;
    }

    @Override
    public ClaimString getBirthMiddleName() {
        return null;
    }

    @Override
    public ClaimString getSalutation() {
        return null;
    }

    @Override
    public ClaimString getTitle() {
        return null;
    }

    @Override
    public ClaimString getMobilePhoneNumber() {
        return null;
    }

    @Override
    public ClaimString getPseudonym() {
        return null;
    }

    @Override
    public List<ClaimCredentialSubject> getCredentialSubjects() {
        return null;
    }
}
