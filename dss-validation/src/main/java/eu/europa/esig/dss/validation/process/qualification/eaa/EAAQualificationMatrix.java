package eu.europa.esig.dss.validation.process.qualification.eaa;

import eu.europa.esig.dss.enumerations.CertificateApprovalStatus;
import eu.europa.esig.dss.enumerations.CertificateApprovalStatusEnum;
import eu.europa.esig.dss.enumerations.EAAQualification;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SignatureQualification;

/**
 * Builds an EAA qualification result based on the given parameters
 *
 */
public final class EAAQualificationMatrix {

    /**
     * Empty constructor
     */
    private EAAQualificationMatrix() {
        // empty
    }

    /** EAA passed validation process */
    private static final int PASSED_EAA = 0;

    /** EAA with an indeterminate result of validation process */
    private static final int INDETERMINATE_EAA = 1;

    /** EAA failed validation process */
    private static final int FAILED_EAA = 2;

    /** Qualified EAA */
    private static final int QEAA = 0;

    /** Public Sector Body EAA */
    private static final int PUBEAA = 1;

    /** Non-qualified EAA */
    private static final int EAA = 2;

    /** Unknown EAA */
    private static final int UNKNOWN_EAA = 3;

    /** Not EAA */
    private static final int NOT_EAA = 4;

    /** Qualified electronic signature or seal */
    private static final int QUAL_SIG_SEAL = 0;

    /** Indeterminate qualified electronic signature or seal */
    private static final int INDETERMINATE_QUAL_SIG_SEAL = 1;

    /** Non-qualified electronic signature or seal */
    private static final int NOT_QUAL_SIG_SEAL = 2;

    /** Not applicable validation status of electronic signature or seal */
    private static final int NA = 3;

    /** Certificate is a PID Provider */
    private static final int CERT_USAGE_PID = 0;

    /** Certificate is not a PID Provider */
    private static final int CERT_USAGE_OTHER = 1;

    /** Not applicable validation of a PID Provider */
    private static final int CERT_USAGE_NA = 2;

    /**
     * Array containing the relationship between qualification parameters and the final EAA qualification
     */
    private static final EAAQualification[][][] QUALIFS = new EAAQualification[3][5][4];

    /**
     * Array containing the relationship between qualification parameters and the final PID qualification
     */
    private static final EAAQualification[][] PID_QUALIFS = new EAAQualification[3][3];

    static {

        // Passed

        QUALIFS[PASSED_EAA][QEAA][QUAL_SIG_SEAL] = EAAQualification.QEAA;
        QUALIFS[PASSED_EAA][QEAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_QEAA;
        QUALIFS[PASSED_EAA][QEAA][NOT_QUAL_SIG_SEAL] = EAAQualification.EAA;
        QUALIFS[PASSED_EAA][QEAA][NA] = EAAQualification.NA;

        QUALIFS[PASSED_EAA][PUBEAA][QUAL_SIG_SEAL] = EAAQualification.PUBEAA;
        QUALIFS[PASSED_EAA][PUBEAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_PUBEAA;
        QUALIFS[PASSED_EAA][PUBEAA][NOT_QUAL_SIG_SEAL] = EAAQualification.EAA;
        QUALIFS[PASSED_EAA][PUBEAA][NA] = EAAQualification.NA;

        QUALIFS[PASSED_EAA][EAA][QUAL_SIG_SEAL] = EAAQualification.EAA;
        QUALIFS[PASSED_EAA][EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_EAA;
        QUALIFS[PASSED_EAA][EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.EAA;
        QUALIFS[PASSED_EAA][EAA][NA] = EAAQualification.NA;

        QUALIFS[PASSED_EAA][UNKNOWN_EAA][QUAL_SIG_SEAL] = EAAQualification.UNKNOWN;
        QUALIFS[PASSED_EAA][UNKNOWN_EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_UNKNOWN;
        QUALIFS[PASSED_EAA][UNKNOWN_EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.UNKNOWN;
        QUALIFS[PASSED_EAA][UNKNOWN_EAA][NA] = EAAQualification.NA;

        // Indeterminate EAA

        QUALIFS[INDETERMINATE_EAA][QEAA][QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_QEAA;
        QUALIFS[INDETERMINATE_EAA][QEAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_QEAA;
        QUALIFS[INDETERMINATE_EAA][QEAA][NOT_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_EAA;
        QUALIFS[INDETERMINATE_EAA][QEAA][NA] = EAAQualification.NA;

        QUALIFS[INDETERMINATE_EAA][PUBEAA][QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_PUBEAA;
        QUALIFS[INDETERMINATE_EAA][PUBEAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_PUBEAA;
        QUALIFS[INDETERMINATE_EAA][PUBEAA][NOT_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_EAA;
        QUALIFS[INDETERMINATE_EAA][PUBEAA][NA] = EAAQualification.NA;

        QUALIFS[INDETERMINATE_EAA][EAA][QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_EAA;
        QUALIFS[INDETERMINATE_EAA][EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_EAA;
        QUALIFS[INDETERMINATE_EAA][EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_EAA;
        QUALIFS[INDETERMINATE_EAA][EAA][NA] = EAAQualification.NA;

        QUALIFS[INDETERMINATE_EAA][UNKNOWN_EAA][QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_UNKNOWN;
        QUALIFS[INDETERMINATE_EAA][UNKNOWN_EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_UNKNOWN;
        QUALIFS[INDETERMINATE_EAA][UNKNOWN_EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.INDETERMINATE_UNKNOWN;
        QUALIFS[INDETERMINATE_EAA][UNKNOWN_EAA][NA] = EAAQualification.NA;

        QUALIFS[INDETERMINATE_EAA][NOT_EAA][QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[INDETERMINATE_EAA][NOT_EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[INDETERMINATE_EAA][NOT_EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[INDETERMINATE_EAA][NOT_EAA][NA] = EAAQualification.NA;

        // Not EAA

        QUALIFS[FAILED_EAA][QEAA][QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][QEAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][QEAA][NOT_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][QEAA][NA] = EAAQualification.NA;

        QUALIFS[FAILED_EAA][PUBEAA][QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][PUBEAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][PUBEAA][NOT_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][PUBEAA][NA] = EAAQualification.NA;

        QUALIFS[FAILED_EAA][EAA][QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][EAA][NA] = EAAQualification.NA;

        QUALIFS[FAILED_EAA][UNKNOWN_EAA][QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][UNKNOWN_EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][UNKNOWN_EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][UNKNOWN_EAA][NA] = EAAQualification.NA;

        QUALIFS[FAILED_EAA][NOT_EAA][QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][NOT_EAA][INDETERMINATE_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][NOT_EAA][NOT_QUAL_SIG_SEAL] = EAAQualification.NOT_EAA;
        QUALIFS[FAILED_EAA][NOT_EAA][NA] = EAAQualification.NA;

        // PID

        PID_QUALIFS[PASSED_EAA][CERT_USAGE_PID] = EAAQualification.PID;
        PID_QUALIFS[PASSED_EAA][CERT_USAGE_OTHER] = EAAQualification.UNKNOWN;
        PID_QUALIFS[PASSED_EAA][CERT_USAGE_NA] = EAAQualification.NA;

        PID_QUALIFS[INDETERMINATE_EAA][CERT_USAGE_PID] = EAAQualification.INDETERMINATE_PID;
        PID_QUALIFS[INDETERMINATE_EAA][CERT_USAGE_OTHER] = EAAQualification.INDETERMINATE_UNKNOWN;
        PID_QUALIFS[INDETERMINATE_EAA][CERT_USAGE_NA] = EAAQualification.NA;

        PID_QUALIFS[FAILED_EAA][CERT_USAGE_PID] = EAAQualification.NOT_EAA;
        PID_QUALIFS[FAILED_EAA][CERT_USAGE_OTHER] = EAAQualification.NOT_EAA;
        PID_QUALIFS[FAILED_EAA][CERT_USAGE_NA] = EAAQualification.NA;

    }

    /**
     * Gets EAA qualification based on the given parameters
     *
     * @param indication {@link Indication} representing the final result of validation process for EAA presentation
     * @param claimedQualification {@link EAAQualification} claimed qualification extracted from the EAA signed payload
     * @param signatureQualification {@link SignatureQualification} of the signature used to create the EAA
     * @return {@link EAAQualification}
     */
    public static EAAQualification getEAAQualification(Indication indication, EAAQualification claimedQualification,
                                                       SignatureQualification signatureQualification) {
        return QUALIFS[getInt(indication)][getInt(claimedQualification)][getInt(signatureQualification)];
    }

    /**
     * Gets PID qualification based on the given parameters
     *
     * @param indication {@link Indication} representing the final result of validation process for EAA presentation
     * @param certificateApprovalStatus {@link CertificateApprovalStatus} determined certificate approval status
     * @return {@link EAAQualification}
     */
    public static EAAQualification getPIDQualification(Indication indication, CertificateApprovalStatus certificateApprovalStatus) {
        return PID_QUALIFS[getInt(indication)][getInt(certificateApprovalStatus)];
    }

    private static int getInt(Indication indication) {
        switch (indication) {
            case FAILED:
            case TOTAL_FAILED:
                return FAILED_EAA;
            case PASSED:
            case TOTAL_PASSED:
                return PASSED_EAA;
            case INDETERMINATE:
                return INDETERMINATE_EAA;
            default:
                throw new IllegalStateException("Unsupported indication " + indication);
        }
    }

    private static int getInt(EAAQualification eaaQualification) {
        switch (eaaQualification) {
            case QEAA:
                return QEAA;
            case PUBEAA:
                return PUBEAA;
            case EAA:
                return EAA;
            case UNKNOWN:
                return UNKNOWN_EAA;
            case NOT_EAA:
                return NOT_EAA;
            default:
                throw new IllegalStateException("Unsupported EAA qualification " + eaaQualification);
        }
    }

    private static int getInt(SignatureQualification signatureQualification) {
        switch (signatureQualification) {
            case QESIG:
            case QESEAL:
                return QUAL_SIG_SEAL;
            case INDETERMINATE_QESIG:
            case INDETERMINATE_QESEAL:
                return INDETERMINATE_QUAL_SIG_SEAL;
            case NA:
                return NA;
            default:
                return NOT_QUAL_SIG_SEAL;
        }
    }

    private static int getInt(CertificateApprovalStatus certificateApprovalStatus) {
        if (CertificateApprovalStatusEnum.PID_PROVIDER == certificateApprovalStatus) {
            return CERT_USAGE_PID;
        } else if (CertificateApprovalStatusEnum.NA == certificateApprovalStatus) {
            return CERT_USAGE_NA;
        } else {
            return CERT_USAGE_OTHER;
        }
    }

}
