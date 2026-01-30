package eu.europa.esig.dss.enumerations;

public enum CertificateUsageEnum implements CertificateUsage {

    CERT_FOR_PID_ISSUANCE("Certificate for PID Issuance", LoTETypeEnum.EUPIDProvidersList, LoTEServiceTypeIdentifierEnum.PID_ISSUANCE, null),

    CERT_FOR_PID_REVOCATION("Certificate for PID Revocation", LoTETypeEnum.EUPIDProvidersList, LoTEServiceTypeIdentifierEnum.PID_REVOCATION, null),

    CERT_FOR_WALLET_ISSUANCE("Certificate for Wallet Solution Issuance", LoTETypeEnum.EUWalletProvidersList, LoTEServiceTypeIdentifierEnum.WALLET_ISSUANCE, null),

    CERT_FOR_WALLET_REVOCATION("Certificate for Wallet Solution Revocation", LoTETypeEnum.EUWalletProvidersList, LoTEServiceTypeIdentifierEnum.WALLET_REVOCATION, null),

    CERT_FOR_WRPAC_ISSUANCE("Certificate for WRPAC Issuance", LoTETypeEnum.EUWRPACProvidersList, LoTEServiceTypeIdentifierEnum.WRPAC_ISSUANCE, null),

    CERT_FOR_WRPAC_REVOCATION("Certificate for WRPAC Revocation", LoTETypeEnum.EUWRPACProvidersList, LoTEServiceTypeIdentifierEnum.WRPAC_REVOCATION, null),

    CERT_FOR_WRPRC_ISSUANCE("Certificate for WRPRC Issuance", LoTETypeEnum.EUWRPRCProvidersList, LoTEServiceTypeIdentifierEnum.WRPRC_ISSUANCE, null),

    CERT_FOR_WRPRC_REVOCATION("Certificate for WRPRC Revocation", LoTETypeEnum.EUWRPRCProvidersList, LoTEServiceTypeIdentifierEnum.WRPRC_REVOCATION, null),

    NOTIFIED_CERT_FOR_PUB_EAA_ISSUANCE("Notified Certificate for Pub-EAA Issuance", LoTETypeEnum.EUPubEAAProvidersList, LoTEServiceTypeIdentifierEnum.PUB_EAA_ISSUANCE, LoTEServiceStatusEnum.PUB_EAA_PROVIDER_NOTIFIED),

    NOTIFIED_CERT_FOR_PUB_EAA_REVOCATION("Notified Certificate for Pub-EAA Revocation", LoTETypeEnum.EUPubEAAProvidersList, LoTEServiceTypeIdentifierEnum.PUB_EAA_REVOCATION, LoTEServiceStatusEnum.PUB_EAA_PROVIDER_NOTIFIED),

    WITHDRAWN_CERT_FOR_PUB_EAA_ISSUANCE("Notified Certificate for Pub-EAA Issuance", LoTETypeEnum.EUPubEAAProvidersList, LoTEServiceTypeIdentifierEnum.PUB_EAA_ISSUANCE, LoTEServiceStatusEnum.PUB_EAA_PROVIDER_WITHDRAWN),

    WITHDRAWN_CERT_FOR_PUB_EAA_REVOCATION("Notified Certificate for Pub-EAA Revocation", LoTETypeEnum.EUPubEAAProvidersList, LoTEServiceTypeIdentifierEnum.PUB_EAA_REVOCATION, LoTEServiceStatusEnum.PUB_EAA_PROVIDER_WITHDRAWN),

    CERT_FOR_REGISTER("Certificate for Register", LoTETypeEnum.EURegistrarsAndRegistersList, LoTEServiceTypeIdentifierEnum.REGISTER, null),

    CERT_FOR_UNKNOWN("Certificate for Unknown usage", null, null, null);

    private final String label;

    private final ListType listType;

    private final LoTEServiceTypeIdentifier sti;

    private final LoTEServiceStatus status;

    CertificateUsageEnum(final String label, final ListType listType, final LoTEServiceTypeIdentifier sti, final LoTEServiceStatus status) {
        this.label = label;
        this.listType = listType;
        this.sti = sti;
        this.status = status;
    }

    @Override
    public ListType getListType() {
        return listType;
    }

    @Override
    public LoTEServiceTypeIdentifier getServiceTypeIdentifier() {
        return sti;
    }

    @Override
    public LoTEServiceStatus getServiceStatus() {
        return status;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
