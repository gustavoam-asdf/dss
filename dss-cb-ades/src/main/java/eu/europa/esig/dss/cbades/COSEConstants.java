package eu.europa.esig.dss.cbades;

/**
 * Utils class containing RFC 9052 and ETSI TS 119 152 constants
 *
 */
public final class COSEConstants {

    /* RFC 9052 COSE structure */

    /** COSE_Sign. CBOR Tag '98' */
    public static final long COSE_SIGN_TAG = 98;

    /** COSE_Sign1. CBOR Tag '18' */
    public static final long COSE_SIGN1_TAG = 18;

    /* RFC 9052 Header parameters */

    /** Cryptographic algorithm used for signature creation. CBOR Tag '1' */
    public static final long ALG = 1;

    /** Critical header parameters to be understood. CBOR Tag '2' */
    public static final long CRIT = 2;

    /** Content Type of the payload. CBOR Tag '3' */
    public static final long CONTENT_TYPE = 3;

    /** Key identifier. CBOR Tag '4' */
    public static final long KID = 4;

    /** Initialization Vector. CBOR Tag '5' */
    public static final long IV = 5;

    /** Partial Initialization Vector. CBOR Tag '6' */
    public static final long PARTIAL_IV = 6;

    /* RFC 8152 Header parameters */

    /** Counter signature. CBOR Tag '7' */
    public static final long COUNTER_SIGNATURE = 7;

    /** Counter signature0. CBOR Tag '9' */
    public static final long COUNTER_SIGNATURE0 = 9;

    /* RFC 9338 Header parameters */

    /** COSE_Countersignature. CBOR Tag '19' */
    public static final long COSE_COUNTERSIGNATURE_TAG = 19;

    /** Counter signature V2. CBOR Tag '11' */
    public static final long COUNTER_SIGNATURE_V2 = 11;

    /** Counter signature0 V2. CBOR Tag '12' */
    public static final long COUNTER_SIGNATURE0_V2 = 12;

    /* RFC 9597 Header Parameters */

    /** CWT Claims. CBOR Tag '15' */
    public static final long CWT_CLAIMS = 15;

    /* RFC 8392 Header Parameters */

    /** CWT Claims iat (issued at). CBOR Tag '6' */
    public static final long CWT_CLAIMS_IAT = 6;

    /* RFC 9360 Header parameters */

    /** An unordered bag of X.509 certificates. CBOR Tag '32' */
    public static final long X5BAG = 32;

    /** An ordered chain of X.509 certificates. CBOR Tag '33' */
    public static final long X5CHAIN = 33;

    /** Hash of an X.509 certificate. CBOR Tag '34' */
    public static final long X5T = 34;

    /** URI pointing to an X.509 certificate. CBOR Tag '35' */
    public static final long X5U = 35;

    /* ETSI TS 119 152 Header parameters */

    /** Reference to signing certificate / certs in cert path. CBOR Tag '261' */
    public static final long X5TS = 261;

    /** Signer commitments. CBOR Tag '262' */
    public static final long SR_CMS = 262;

    /** Signature production place. CBOR Tag '263' */
    public static final long SIG_PL = 263;

    /** Signer attributes. CBOR Tag '264' */
    public static final long SR_ATS = 264;

    /** COSE payload time-stamp. CBOR Tag '265' */
    public static final long ADO_TST = 265;

    /** Signature Policy Identifier. CBOR Tag '266' */
    public static final long SIG_PID = 266;

    /** Detached COSE Payload reference data. CBOR Tag '267' */
    public static final long SIG_D = 267;

    /* ETSI TS 119 152 uHeaders parameters */

    /** uHeaders header parameter contains a list of unsigned properties qualifying the CB-AdES signature. CBOR Tag '268' */
    public static final long U_HEADERS = 268;

    /** Signature time-stamp. CBOR Tag '1' */
    public static final long SIG_TST = 1;

    /** Validation data. CBOR Tag '2' */
    public static final long VAL_DATA = 2;

    /** Archive time-stamp. CBOR Tag '3' */
    public static final long ARC_TST = 3;

    /** Validation data references. CBOR Tag '4' */
    public static final long REFS = 4;

    /** Signature and validation data references time-stamp. CBOR Tag '5' */
    public static final long SIG_R_TST = 5;

    /** Validation data references time-stamp. CBOR Tag '6' */
    public static final long RFS_TST = 6;

    /** Signature policy document. CBOR Tag '7' */
    public static final long SIG_PST = 7;

    /* Subtype keys */

    /** SrCm commId: the commitment identifier: an oId data type. CBOR Tag '1' */
    public static final long SR_CM_COMM_ID = 1;

    /** SrCm commQuals: qualifiers. CBOR Tag '2' */
    public static final long SR_CM_COMM_QUALS = 2;

    /** oId id: the URI reference that is the object identifier. CBOR Tag '1' */
    public static final long OID_ID = 1;

    /** oId desc: a textual description of the identified object. CBOR Tag '2' */
    public static final long OID_DESC = 2;

    /** oId docRefs: an array of URI references to documents specifying the identified object. CBOR Tag '3' */
    public static final long OID_DOC_REFS = 3;

    /** pkiOb val: CBOR byte string encapsulating the encoded PKI object. CBOR Tag '1' */
    public static final long PKI_OB_VAL = 1;

    /** pkiOb encoding: a URI reference identifying the encoding. CBOR Tag '2' */
    public static final long PKI_OB_ENCODING = 2;

    /** pkiOb specRef: a URI reference identifying the specification of the encapsulated PKI object. CBOR Tag '3' */
    public static final long PKI_OB_SPEC_REF = 3;

    /** sigPl addressCountry. CBOR Tag '1' */
    public static final long SIG_PL_ADDRESS_COUNTRY = 1;

    /** sigPl addressLocality. CBOR Tag '2' */
    public static final long SIG_PL_ADDRESS_LOCALITY = 2;

    /** sigPl addressRegion. CBOR Tag '3' */
    public static final long SIG_PL_ADDRESS_REGION = 3;

    /** sigPl postOfficeBoxNumber. CBOR Tag '4' */
    public static final long SIG_PL_POST_OFFICE_BOX_NUMBER = 4;

    /** sigPl postalCode. CBOR Tag '5' */
    public static final long SIG_PL_POSTAL_CODE = 5;

    /** sigPl streetAddress. CBOR Tag '6' */
    public static final long SIG_PL_STREET_ADDRESS = 6;

    /** srAts certified: Certified signer attributes. CBOR Tag '1' */
    public static final long SR_ATS_CERTIFIED_ATTRS = 1;

    /** srAts signedAssertions: Signed assertions for signer. CBOR Tag '2' */
    public static final long SR_ATS_SIGNED_ASSERTIONS = 2;

    /** srAts claimed: Claimed signer attributes. CBOR Tag '3' */
    public static final long SR_ATS_CLAIMED = 3;

    /** srAtms certified CertifiedAttr x509AttrCert: encapsulates a X.509 attribute certificate. CBOR Tag '1' */
    public static final long CERTIFIED_ATTR_X509_ATTR_CERT = 1;

    /** srAtms certified CertifiedAttr otherAttrCert: encapsulates another type of attribute certificate. CBOR Tag '2' */
    public static final long CERTIFIED_ATTR_OTHER_ATTR_CERT = 2;

    /** NotCertifiedItem mediaType: String identifying the type of claimed attributes or signed assertions. Array position '0' */
    public static final int NOT_CERTIFIED_ITEM_MEDIA_TYPE = 0;

    /** NotCertifiedItem encoding: String identifying the encoding of claimed attributes or signed assertions. Array position '1' */
    public static final int NOT_CERTIFIED_ITEM_ENCODING = 1;

        /** NotCertifiedItem qVals: Array with the claimed attributes or signed assertions. Array position '0' */
    public static final int NOT_CERTIFIED_ITEM_QVALS = 2;

    /** tstContainer tstTokens: CBOR array containing one or more time-stamp tokens. CBOR Tag '1' */
    public static final long TST_CONTAINER_TST_TOKENS = 1;

    /** tstContainer canonAlg: URI reference identifying the canonicalization algorithm. CBOR Tag '2' */
    public static final long TST_CONTAINER_CANON_ALG = 2;

    /** tstToken val: Encoded time-stamp token encapsulated in a CBOR byte string. CBOR Tag '1' */
    public static final long TST_TOKEN_VAL = 1;

    /** tstToken type: String identifying the type of time-stamp token. CBOR Tag '2' */
    public static final long TST_TOKEN_TYPE = 2;

    /** tstToken encoding: URI reference identifying the type of encoding. CBOR Tag '3' */
    public static final long TST_TOKEN_ENCODING = 3;

    /** tstToken specRef: a URI reference identifying the reference where the time-stamp token is defined. CBOR Tag '4' */
    public static final long TST_TOKEN_SPEC_REF = 4;

    /** sigPId id: instance of oId type identifying the signature policy. CBOR Tag '1' */
    public static final long SIG_P_ID_ID = 1;

    /** sigPId digAlgVal: digest algorithm and value of the signature policy document. CBOR Tag '2' */
    public static final long SIG_P_ID_DIG_ALG_VAL = 2;

    /** sigPId digPSp: indicates whether the digest has been computed according to some spec, default value: false. CBOR Tag '3' */
    public static final long SIG_P_ID_DIG_P_SP = 3;

    /** sigPId sigPQuals: signature policy qualifiers. CBOR Tag '4' */
    public static final long SIG_P_ID_SIG_P_QUALS = 4;

    /** SigPQual spURI: URL where a copy of the signature policy document can be obtained. CBOR Tag '1' */
    public static final long SIG_P_QUAL_SP_URI = 1;

    /** SigPQual spUserNotice: Info displayed when signature is validated. CBOR Tag '2' */
    public static final long SIG_P_QUAL_SP_USER_NOTICE = 2;

    /** SigPQual spDSpec: identifier of the technical specification that defines the syntax used for producing the signature policy document. CBOR Tag '3' */
    public static final long SIG_P_QUAL_SP_D_SPEC = 3;

    /** spUserNotice noticeRef: User notice and references. CBOR Tag '1' */
    public static final long SP_USER_NOTICE_NOTICE_REF = 1;

    /** spUserNotice explText: notice text to be displayed. CBOR Tag '2' */
    public static final long SP_USER_NOTICE_EXPL_TEXT = 2;

    /** noticeRef org: the name of the organization. CBOR Tag '1' */
    public static final long NOTICE_REF_ORG = 1;

    /** noticeRef noticeNumbers: the notice numbers identifying textual statements. CBOR Tag '2' */
    public static final long NOTICE_REF_NOTICE_NUMBERS = 2;

    /** sigD mId: URI identifying the mechanism used for referencing and processing each referenced data object. CBOR Tag '1' */
    public static final long SIG_D_MID = 1;

    /** sigD pars: References to data objects as per the mechanism identified by mId. CBOR Tag '2' */
    public static final long SIG_D_PARS = 2;

    /** sigD hashM: Digest algorithm identifier. CBOR Tag '3' */
    public static final long SIG_D_HASH_M = 3;

    /** sigD hashV: Digest values of referenced data objects as per algorithm identified by hashM. CBOR Tag '4' */
    public static final long SIG_D_HASH_V = 4;

    /** sigD ctys: Indication of the content type of each referenced object. CBOR Tag '5' */
    public static final long SIG_D_CTYS = 5;

    /** DigAlgVal hashAlg. Array position '0' */
    public static final int DIG_ALG_VAL_HASH_ALG = 0;

    /** DigAlgVal hashValue. Array position '1' */
    public static final int DIG_ALG_VAL_HASH_VALUE = 1;

    /** COSE_CertHash hashAlg. Array position '0' */
    public static final int COSE_CERT_HASH_ALG = 0;

    /** COSE_CertHash hashValue. Array position '0' */
    public static final int COSE_CERT_HASH_VALUE = 1;

    /** valData xVals: DER-encoded encapsulated X.509 certificates or other encapsulated certificates. CBOR Tag '1' */
    public static final long VAL_DATA_X_VALS = 1;

    /** valData rVals: validation material. CBOR Tag '2' */
    public static final long VAL_DATA_R_VALS = 2;

    /** X509OrOther x509Cert: DER-encoded X509 certificate encapsulated in an instance of pkiOb type. CBOR Tag '1' */
    public static final long X509_OR_OTHER_X509_CERT = 1;

    /** X509OrOther otherCert: otherCert: Other type of certificate encoded and encapsulated in an instance of pkiOb type. CBOR Tag '2' */
    public static final long X509_OR_OTHER_OTHER_CERT = 2;

    /** rVals crlVals: array of CRLs encapsulated in an instance of pkiOb type. CBOR Tag '1' */
    public static final long R_VALS_CRL_VALS = 1;

    /** rVals ocspVals: array of DER encoded OCSPResponse encapsulated in an instance of pkiOb type. CBOR Tag '2' */
    public static final long R_VALS_OCSP_VALS = 2;

    /** rVals otherVals: other revocation values encoded and encapsulated in an instance of pkiOb type. CBOR Tag '3' */
    public static final long R_VALS_OTHER_VALS = 3;

    /** refs xRefs: references to certificates. CBOR Tag '1' */
    public static final long REFS_X_REFS = 1;

    /** refs rRefs: references to revocation data. CBOR Tag '2' */
    public static final long REFS_R_REFS = 2;

    /** CertId x5t: the digest algorithm identifier and value. CBOR Tag '1' */
    public static final long CERT_ID_X5T = 1;

    /** CertId kid: optional key identifier. CBOR Tag '2' */
    public static final long CERT_ID_KID = 2;

    /** CertId x5u: URI reference to X.509 certificate. CBOR Tag '3' */
    public static final long CERT_ID_X5U = 3;

    /** rRefs crlRefs: array of references to CRLs. CBOR Tag '1' */
    public static final long R_REFS_CRL_REF = 1;

    /** rRefs ocspRefs: array of references to OCSP responses. CBOR Tag '2' */
    public static final long R_REFS_OCSP_REF = 2;

    /** rRefs otherRefs: array of references to OCSP responses. CBOR Tag '3' */
    public static final long R_REFS_ANY = 3;

    /** CRLRef digAlgVal: digest algorithm and value of DER-encoded CRL. CBOR Tag '1' */
    public static final long CRL_REF_DIG_ALG_VAL = 1;

    /** CRLRef crlId: identifier of the CRL. CBOR Tag '2' */
    public static final long CRL_REF_CRL_ID = 2;

    /** CRLId issuer: the DER-encoded issuer of the CRL. CBOR Tag '1' */
    public static final long CRL_ID_ISSUER = 1;

    /** CRLId issueTime: the date and time of issuance. CBOR Tag '2' */
    public static final long CRL_ID_ISSUE_TIME = 2;

    /** CRLId number: the issuance number. CBOR Tag '3' */
    public static final long CRL_ID_NUMBER = 3;

    /** CRLId uri: an URI reference to the CRL (hint). CBOR Tag '4' */
    public static final long CRL_ID_URI = 4;

    /** OCSPRef digAlgVal: digest algorithm and value of the OCSP response. CBOR Tag '1' */
    public static final long OCSP_REF_DIG_ALG_VAL = 1;

    /** OCSPRef ocspId: identifier of the OCSP response. CBOR Tag '2' */
    public static final long OCSP_REF_OCSP_ID = 2;

    /** OCSPId responderChoice: a choice for identifying the responder. CBOR Tag '1' */
    public static final long OCSP_ID_RESPONDER_ID_CHOICE = 1;

    /** OCSPId producedAt: same time as the time indicated by the ProducedAt field of the referenced OCSP response. CBOR Tag '2' */
    public static final long OCSP_ID_PRODUCED_AT = 2;

    /** OCSPId uri: an URI reference to the OCSP response (hint). CBOR Tag '3' */
    public static final long OCSP_ID_URI = 3;

    /** ResponderIdChoice responderIdByName: the name of the responder wrapped in a CBOR byte string. CBOR Tag '1' */
    public static final long RESPONDER_ID_CHOICE_RESPONDER_ID_BY_NAME = 1;

    /** ResponderIdChoice responderIdByKey: the key of the responder wrapped in a CBOR byte string. CBOR Tag '2' */
    public static final long RESPONDER_ID_CHOICE_RESPONDER_ID_BY_KEY = 2;

    /**
     * Singleton
     */
    private COSEConstants() {
        // empty
    }

}
