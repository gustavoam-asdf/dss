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
    // TODO : Tags have to be registered in IANA.
    // TODO : For the moment, we use TBD00X or TBD0XY (TBD: To Be Defined) values, using unassigned identifiers, starting from 100.

    /** Reference to signing certificate / certs in cert path. CBOR Tag 'TBD001' */
    public static final long X5TS = 101;

    /** Claimed signing time. CBOR Tag 'TBD002' */
    public static final long SIG_T = 102;

    /** Signer commitments. CBOR Tag 'TBD003' */
    public static final long SR_CMS = 103;

    /** Signature production place. CBOR Tag 'TBD004' */
    public static final long SIG_PL = 104;

    /** Signer attributes. CBOR Tag 'TBD005' */
    public static final long SR_ATS = 105;

    /** COSE payload time-stamp. CBOR Tag 'TBD006' */
    public static final long ADO_TST = 106;

    /** Signature Policy Identifier. CBOR Tag 'TBD007' */
    public static final long SIG_PID = 107;

    /** URL where a copy of the signature policy document can be obtained. CBOR Tag 'TBD008' */
    public static final long SP_URI = 108;

    /** Info displayed when signature is validated. CBOR Tag 'TBD009' */
    public static final long SP_USER_NOTICE = 109;

    /** Identifier of the technical specification that defines the syntax used for producing the signature policy document. CBOR Tag 'TBD010' */
    public static final long SP_D_SPEC = 110;

    /** Detached COSE Payload reference data. CBOR Tag 'TBD011' */
    public static final long SIG_D = 111;

    /* ETSI TS 119 152 uHeaders parameters */
    // TODO : Tags have to be registered in IANA.
    // TODO : For the moment, we use TBD00X or TBD0XY (TBD: To Be Defined) values, using unassigned identifiers, starting from 200.

    /** uHeaders header parameter contains a list of unsigned properties qualifying the CB-AdES signature. CBOR Tag 'TBD111' */
    public static final long U_HEADERS = 211;

    /** Signature policy document. CBOR Tag 'TBD112' */
    public static final long SIG_PST = 212;

    /** Signature time-stamp. CBOR Tag 'TBD113' */
    public static final long SIG_TST = 213;

    /** Validation data. CBOR Tag 'TBD114' */
    public static final long VAL_DATA = 214;

    /** Archive time-stamp. CBOR Tag 'TBD115' */
    public static final long ARC_TST = 215;

    /** Validation data references. CBOR Tag 'TBD116' */
    public static final long REFS = 216;

    /** Signature and validation data references time-stamp. CBOR Tag 'TBD117' */
    public static final long SIG_R_TST = 217;

    /** Validation data references time-stamp. CBOR Tag 'TBD118' */
    public static final long RFS_TST = 218;

    /* Subtype keys */

    /** SrCm commId the commitment identifier: an oId data type. CBOR Tag '1' */
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

    /** spUserNotice noticeRef: User notice and references. CBOR Tag '1' */
    public static final long SP_USER_NOTICE_NOTICE_REF = 1;

    /** spUserNotice explText: notice text to be displayed. CBOR Tag '2' */
    public static final long SP_USER_NOTICE_EXPL_TEXT = 2;

    /** noticeRef org: the name of the organization. CBOR Tag '1' */
    public static final long NOTICE_REF_ORG = 1;

    /** noticeRef notNumbres: the notice numbers identifying textual statements. CBOR Tag '2' */
    public static final long NOTICE_REF_NOT_NUMBERS = 2;

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

    /** valData xVals: DER-encoded encapsulated X.509 certificates or other encapsulated certificates. CBOR Tag '1 */
    public static final long VAL_DATA_X_VALS = 1;

    /** valData rVals: validation material. CBOR Tag '2 */
    public static final long VAL_DATA_R_VALS = 2;

    /**
     * Singleton
     */
    private COSEConstants() {
        // empty
    }

}
