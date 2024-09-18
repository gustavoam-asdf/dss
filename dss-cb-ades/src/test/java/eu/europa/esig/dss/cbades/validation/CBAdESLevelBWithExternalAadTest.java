package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.InMemoryDocument;

class CBAdESLevelBWithExternalAadTest extends AbstractCBAdESTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/cb-ades-level-b-external-aad.cose");
    }

    @Override
    protected DSSDocument getExternallySuppliedData() {
        return new InMemoryDocument("Bye World!".getBytes());
    }

}
