package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.validation.AbstractEAAPresentationTestIssuance;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.MimeType;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;

public abstract class AbstractSDJWTEAAPresentationTestIssuance extends AbstractEAAPresentationTestIssuance<
        JAdESSignatureParameters, SDJWTEAAPayloadParameters, SDJWTEAAClaim, SDJWTEAADisclosure> {

    @Override
    protected SDJWTEAAService getService() {
        return new SDJWTEAAService(getOfflineCertificateVerifier());
    }

    @Override
    protected MimeType getExpectedMime() {
        return MimeTypeEnum.JSON;
    }

    @Override
    protected EAAType getEAAType() {
        return EAAType.SD_JWT_VC;
    }

}
