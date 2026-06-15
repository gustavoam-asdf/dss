package eu.europa.esig.dss.cbades.cwt;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.spi.WebTokenPayload;

import java.util.Date;

public class CWTPayload implements WebTokenPayload {

    /** Map representing a CWT payload */
    protected final CBORMap payload;

    /**
     * Default constructor
     *
     * @param payload {@link CBORMap}
     */
    public CWTPayload(final CBORMap payload) {
        this.payload = payload;
    }

    @Override
    public String getIssuer() {
        return payload.getAsString(CWTClaims.ISS.cbor());
    }

    @Override
    public String getSubject() {
        return payload.getAsString(CWTClaims.SUB.cbor());
    }

    @Override
    public String getAudience() {
        return payload.getAsString(CWTClaims.AUD.cbor());
    }

    @Override
    public Date getExpirationTime() {
        return CBORUtils.fromNumericDate(payload.getHeader(CWTClaims.EXP.cbor()));
    }

    @Override
    public Date getNotBefore() {
        return CBORUtils.fromNumericDate(payload.getHeader(CWTClaims.NBF.cbor()));
    }

    @Override
    public Date getIssuedAt() {
        return CBORUtils.fromNumericDate(payload.getHeader(CWTClaims.IAT.cbor()));
    }

    @Override
    public String getTokenId() {
        return payload.getAsString(CWTClaims.CTI.cbor());
    }

}
