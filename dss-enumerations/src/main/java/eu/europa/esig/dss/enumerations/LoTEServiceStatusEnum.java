package eu.europa.esig.dss.enumerations;

public enum LoTEServiceStatusEnum implements LoTEServiceStatus {

    PUB_EAA_PROVIDER_NOTIFIED("http://uri.etsi.org/19602/PubEAAProvidersList/SvcStatus/notified", "Notified Pub-EAA provider service"),

    PUB_EAA_PROVIDER_WITHDRAWN("http://uri.etsi.org/19602/PubEAAProvidersList/SvcStatus/withdrawn", "Withdrawn Pub-EAA provider service");

    /** Service Status URI */
    private final String statusUri;

    /** User-friendly label */
    private final String label;

    LoTEServiceStatusEnum(final String statusUri, final String label) {
        this.statusUri = statusUri;
        this.label = label;
    }

    @Override
    public String getUri() {
        return statusUri;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
