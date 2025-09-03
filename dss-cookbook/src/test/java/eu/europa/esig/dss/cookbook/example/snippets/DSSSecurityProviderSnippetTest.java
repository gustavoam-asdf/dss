package eu.europa.esig.dss.cookbook.example.snippets;

import eu.europa.esig.dss.spi.DSSSecurityProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

class DSSSecurityProviderSnippetTest {

    @Test
    void test() throws Exception {

        // tag::main-provider[]
        // import eu.europa.esig.dss.spi.DSSSecurityProvider;
        // import org.bouncycastle.jce.provider.BouncyCastleProvider;

        DSSSecurityProvider.setSecurityProvider(new BouncyCastleProvider());
        // end::main-provider[]

        // tag::alt-providers[]
        // import eu.europa.esig.dss.spi.DSSSecurityProvider;

        DSSSecurityProvider.setAlternativeSecurityProviders("SUN");
        // end::alt-providers[]

        // tag::init-providers[]
        // import eu.europa.esig.dss.spi.DSSSecurityProvider;

        DSSSecurityProvider.initSystemProviders();
        // end::init-providers[]

    }

}
