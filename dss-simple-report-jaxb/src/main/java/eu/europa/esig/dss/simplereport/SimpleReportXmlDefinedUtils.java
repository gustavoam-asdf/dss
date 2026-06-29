package eu.europa.esig.dss.simplereport;

import eu.europa.esig.dss.xml.common.SchemaFactoryBuilder;
import eu.europa.esig.dss.xml.common.TransformerFactoryBuilder;

import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;

/**
 * This class provides access to XML Securities configuration required for processing and
 * building of DSS XML Simple Report
 *
 */
public final class SimpleReportXmlDefinedUtils {

    /** Singleton */
    private static SimpleReportXmlDefinedUtils singleton;

    /** Builds the secure version of {@code TransformerFactory} */
    private TransformerFactoryBuilder secureTransformerFactoryBuilder = TransformerFactoryBuilder.getSecureTransformerBuilder();

    /** Builds the secure version of {@code SchemaFactory} */
    private SchemaFactoryBuilder secureSchemaFactoryBuilder = SchemaFactoryBuilder.getSecureSchemaBuilder();

    /**
     * Singleton
     */
    private SimpleReportXmlDefinedUtils() {
        // empty
    }

    /**
     * Instantiate the {@code SimpleReportXmlDefinedUtils}
     *
     * @return {@link SimpleReportXmlDefinedUtils}
     */
    public static SimpleReportXmlDefinedUtils getInstance() {
        if (singleton == null) {
            singleton = new SimpleReportXmlDefinedUtils();
        }
        return singleton;
    }

    /**
     * Sets a pre-configured builder to instantiate a {@code TransformerFactory}
     *
     * @param transformerFactoryBuilder {@link TransformerFactoryBuilder}
     */
    public void setTransformerFactoryBuilder(TransformerFactoryBuilder transformerFactoryBuilder) {
        this.secureTransformerFactoryBuilder = transformerFactoryBuilder;
    }

    /**
     * Returns a TransformerFactory with enabled security features (disabled
     * external DTD/XSD + secure processing
     *
     * @return {@link TransformerFactory}
     */
    public TransformerFactory getSecureTransformerFactory() {
        return secureTransformerFactoryBuilder.build();
    }

    /**
     * Sets a pre-configured builder to instantiate a {@code SchemaFactory}
     *
     * @param schemaFactoryBuilder {@link SchemaFactoryBuilder}
     */
    public void setSchemaFactoryBuilder(SchemaFactoryBuilder schemaFactoryBuilder) {
        this.secureSchemaFactoryBuilder = schemaFactoryBuilder;
    }

    /**
     * Returns a SchemaFactory with enabled security features (disabled external
     * DTD/XSD + secure processing
     *
     * @return {@link SchemaFactory}
     */
    public SchemaFactory getSecureSchemaFactory() {
        return secureSchemaFactoryBuilder.build();
    }

}
