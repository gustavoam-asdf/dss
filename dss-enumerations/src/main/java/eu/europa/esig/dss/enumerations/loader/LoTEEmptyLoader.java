package eu.europa.esig.dss.enumerations.loader;

import eu.europa.esig.dss.enumerations.CertificateUsage;
import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.enumerations.LoTEServiceStatus;
import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifier;

public class LoTEEmptyLoader implements LoTELoader {

    /**
     * Default constructor
     */
    public LoTEEmptyLoader() {
        // empty
    }

    @Override
    public ListType listTypeFromUri(String uri) {
        return new ListType() {

            private static final long serialVersionUID = -2473908260368214513L;

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public String getUri() {
                return uri;
            }

        };
    }

    @Override
    public LoTEServiceTypeIdentifier serviceTypeIdentifierFromUri(String uri) {
        return new LoTEServiceTypeIdentifier() {

            private static final long serialVersionUID = 6585326102373562248L;

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public String getUri() {
                return uri;
            }

        };
    }

    @Override
    public LoTEServiceStatus serviceStatusFromUri(String uri) {
        return new LoTEServiceStatus() {

            private static final long serialVersionUID = -2749490865159156345L;

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public String getUri() {
                return uri;
            }

        };
    }

    @Override
    public CertificateUsage certificateUsageFromLabel(String label) {
        return new CertificateUsage() {

            @Override
            public ListType getListType() {
                return null;
            }

            @Override
            public LoTEServiceTypeIdentifier getServiceTypeIdentifier() {
                return null;
            }

            @Override
            public LoTEServiceStatus getServiceStatus() {
                return null;
            }

            @Override
            public String getLabel() {
                return label;
            }

        };
    }

    @Override
    public CertificateUsage certificateUsageFromDefinition(ListType listType, LoTEServiceTypeIdentifier sti, LoTEServiceStatus status) {
        // not supported
        return null;
    }

}
