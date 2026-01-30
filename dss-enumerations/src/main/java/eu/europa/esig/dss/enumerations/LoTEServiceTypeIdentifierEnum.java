package eu.europa.esig.dss.enumerations;

public enum LoTEServiceTypeIdentifierEnum implements LoTEServiceTypeIdentifier {

    PID_ISSUANCE("http://uri.etsi.org/19602/SvcType/PID/Issuance", "PID Issuance"),

    PID_REVOCATION("http://uri.etsi.org/19602/SvcType/PID/Revocation", "PID Revocation"),

    WALLET_ISSUANCE("http://uri.etsi.org/19602/SvcType/WalletSolution/Issuance" , "Wallet Solution Issuance"),

    WALLET_REVOCATION("http://uri.etsi.org/19602/SvcType/WalletSolution/Revocation" , "Wallet Solution Revocation"),

    WRPAC_ISSUANCE("http://uri.etsi.org/19602/SvcType/WRPAC/Issuance", "WRPAC Issuance"),

    WRPAC_REVOCATION("http://uri.etsi.org/19602/SvcType/WRPAC/Revocation", "WRPAC Revocation"),

    WRPRC_ISSUANCE("http://uri.etsi.org/19602/SvcType/WRPRC/Issuance", "WRPRC Issuance"),

    WRPRC_REVOCATION("http://uri.etsi.org/19602/SvcType/WRPRC/Revocation" , "WRPRC Revocation"),

    PUB_EAA_ISSUANCE("http://uri.etsi.org/19602/SvcType/PubEAA/Issuance", "Pub-EAA Issuance"),

    PUB_EAA_REVOCATION("http://uri.etsi.org/19602/SvcType/PubEAA/Revocation", "Pub-EAA Revocation"),

    REGISTER("http://uri.etsi.org/19602/SvcType/Register", "Register");

    /** Service Type Identifier URI */
    private final String stiUri;

    /** User-friendly label defining the certificate usage type */
    private final String label;

    /**
     * Default constructor
     *
     * @param stiUri {@link String}
     * @param label {@link String}
     */
    LoTEServiceTypeIdentifierEnum(final String stiUri, final String label) {
        this.stiUri = stiUri;
        this.label = label;
    }

    @Override
    public String getUri() {
        return stiUri;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
