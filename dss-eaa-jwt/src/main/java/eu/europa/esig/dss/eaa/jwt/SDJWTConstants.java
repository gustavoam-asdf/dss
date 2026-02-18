package eu.europa.esig.dss.eaa.jwt;

/**
 * Contains a list of SD-JWT constants requiring for processing the token
 *
 */
public class SDJWTConstants {

    /**
     * Utils class
     */
    private SDJWTConstants() {
        // singleton
    }

    // SD-JWT unprotected header parameters

    /** SD-JWT unprotected header "disclosures" header */
    public static final String DISCLOSURES = "disclosures";

    /** SD-JWT unprotected header "kb_jwt" (key binding JWT) header */
    public static final String KB_JWT = "kb_jwt";

    // SD-JWT payload parameters

    // RFC 9901 payload header parameters

    /** SD-JWT payload header used to define a hash value of a selectively disclosable array element */
    public static final String HASH = "...";

    /** SD-JWT payload "_sd" header */
    public static final String _SD = "_sd";

    /** SD-JWT payload "_sd_alg" header */
    public static final String _SD_ALG = "_sd_alg";

    // RFC 7519 claims

    /** 4.1.1. "iss" (Issuer) Claim */
    public static final String ISSUER = "iss";

    /** 4.1.2. "sub" (Subject) Claim */
    public static final String SUBJECT = "sub";

    /** 4.1.3. "aud" (Audience) Claim */
    public static final String AUDIENCE = "aud";

    /** 4.1.4. "exp" (Expiration Time) Claim */
    public static final String EXPIRATION_TIME = "exp";

    /** 4.1.5. "nbf" (Not Before) Claim */
    public static final String NOT_BEFORE = "nbf";

    /** 4.1.6. "iat" (Issued At) Claim */
    public static final String ISSUED_AT = "iat";

    /** 4.1.7. "jti" (JWT ID) Claim */
    public static final String JWT_ID = "jti";

    // draft-ietf-oauth-sd-jwt-vc-13

    /** The type of the Verifiable Credential */
    public static final String VERIFIABLE_CREDENTIALS_TYPE = "vct";

    /** The hash of the Type Metadata document to provide integrity */
    public static final String VERIFIABLE_CREDENTIALS_INTEGRITY = "vct#integrity";

    // draft-ietf-oauth-status-list-13

    /** Specifies a JSON Object that contains at least one reference to a status mechanism */
    public static final String STATUS = "status";

    /** Specifies a JSON Object that contains a reference to a Status List Token */
    public static final String STATUS_LIST = "status_list";

    /** A non-negative Integer that represents the index to check for status information for the current Token */
    public static final String STATUS_INDEX = "idx";

    /** String value that identifies the Status List Token containing the status information for the Token */
    public static final String STATUS_URI = "uri";

    // RFC 9449 Nonce

    /** Value used to associate a Client session with an ID Token */
    public static final String NONCE = "nonce";

    // OpenID Connect Core 1.0 (User information claims)

    /** End-User's full name */
    public static final String USER_NAME = "name";

    /** Given name(s) or first name(s) of the End-User */
    public static final String USER_GIVEN_NAME = "given_name";

    /** Surname(s) or last name(s) of the End-User */
    public static final String USER_FAMILY_NAME = "family_name";

    /** Middle name(s) of the End-User */
    public static final String USER_MIDDLE_NAME = "middle_name";

    /** Casual name of the End-User */
    public static final String USER_NICKNAME = "nickname";

    /** Shorthand name by which the End-User wishes to be referred */
    public static final String USER_PREFERRED_NICKNAME = "preferred_username";

    /** URL of the End-User's profile page */
    public static final String USER_PROFILE = "profile";

    /** URL of the End-User's profile picture */
    public static final String USER_PICTURE = "picture";

    /** URL of the End-User's Web page or blog */
    public static final String USER_WEBSITE = "website";

    /** End-User's preferred e-mail address */
    public static final String USER_EMAIL = "email";

    /** End-User's preferred e-mail address */
    public static final String USER_EMAIL_VERIFIED = "email_verified";

    /** End-User's gender */
    public static final String USER_GENDER = "gender";

    /** End-User's birthday */
    public static final String USER_BIRTHDATE = "birthdate";

    /** End-User's time zone */
    public static final String USER_ZONEINFO = "zoneinfo";

    /** End-User's locale */
    public static final String USER_LOCALE = "locale";

    /** End-User's preferred telephone number */
    public static final String USER_PHONE_NUMBER = "phone_number";

    /** If the End-User's phone number has been verified */
    public static final String USER_PHONE_NUMBER_VERIFIED = "phone_number_verified";

    /** End-User's preferred postal address */
    public static final String USER_ADDRESS = "address";

    /** End-User's full mailing address */
    public static final String USER_ADDRESS_FORMATTED = "formatted";

    /** End-User's full street address component */
    public static final String USER_ADDRESS_STREET_ADDRESS = "street_address";

    /** End-User's city or locality component */
    public static final String USER_ADDRESS_LOCALITY = "locality";

    /** End-User's state, province, prefecture, or region component */
    public static final String USER_ADDRESS_REGION = "region";

    /** End-User's zip code or postal code component */
    public static final String USER_ADDRESS_POSTAL_CODE = "postal_code";

    /** End-User's country name component */
    public static final String USER_ADDRESS_COUNTRY = "country";

    /** Time the End-User's information was last updated */
    public static final String UPDATED_AT = "updated_at";

    // OpenID Connect for Identity Assurance Claims Registration 1.0

    /** End-user's place of birth */
    public static final String USER_PLACE_OF_BIRTH = "place_of_birth";

    /** String representing country in [ISO 3166-1] Alpha-2 or [ISO 3166-3] syntax */
    public static final String USER_PLACE_OF_BIRTH_COUNTRY = "country";

    /** String representing state, province, prefecture, or region component */
    public static final String USER_PLACE_OF_BIRTH_REGION = "region";

    /** String representing city or locality component */
    public static final String USER_PLACE_OF_BIRTH_LOCALITY = "locality";

    /** End-user's nationalities using ICAO 3-letter codes, 2-letter ICAO codes may be used */
    public static final String USER_NATIONALITIES = "nationalities";

    /** End-user's family name(s) when they were born */
    public static final String USER_BIRTH_FAMILY_NAME = "birth_family_name";

    /** End-user's given name(s) when they were born */
    public static final String USER_BIRTH_GIVEN_NAME = "birth_given_name";

    /** End-user's middle name(s) when they were born */
    public static final String USER_BIRTH_MIDDLE_NAME = "birth_middle_name";

    /** End-user's salutation */
    public static final String USER_SALUTATION = "salutation";

    /** End-user's title */
    public static final String USER_TITLE = "title";

    /** End-user's mobile phone number formatted according to ITU-T recommendation */
    public static final String USER_MOBILE_PHONE_NUMBER = "msisdn";

    /** Stage name, religious name or any other type of alias/pseudonym */
    public static final String USER_PSEUDONYM = "also_known_as";

    // ETSI TS 119 472-1 qualified claims

    /** SD-JWT payload "category" header */
    public static final String CATEGORY = "category";

}
