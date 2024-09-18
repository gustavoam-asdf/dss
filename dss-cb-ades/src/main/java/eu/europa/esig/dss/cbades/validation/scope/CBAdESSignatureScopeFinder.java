package eu.europa.esig.dss.cbades.validation.scope;

import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.validation.scope.AbstractSignatureScopeFinder;
import eu.europa.esig.dss.spi.validation.scope.CounterSignatureScope;
import eu.europa.esig.dss.spi.validation.scope.DigestSignatureScope;
import eu.europa.esig.dss.spi.validation.scope.FullSignatureScope;
import eu.europa.esig.dss.spi.validation.scope.SignatureScopeFinder;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Finds a SignatureScope for a CB-AdES signature
 * 
 */
public class CBAdESSignatureScopeFinder extends AbstractSignatureScopeFinder implements SignatureScopeFinder<CBAdESSignature> {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESSignatureScopeFinder.class);

    /**
     * Default constructor
     */
    public CBAdESSignatureScopeFinder() {
        // empty
    }

    @Override
    public List<SignatureScope> findSignatureScope(final CBAdESSignature cbadesSignature) {
        List<SignatureScope> result = new ArrayList<>();

        List<DSSDocument> originalDocuments = getOriginalDocuments(cbadesSignature);
        if (Utils.isCollectionEmpty(originalDocuments)) {
            return result;
        }

        List<ReferenceValidation> referenceValidations = cbadesSignature.getReferenceValidations();
        for (ReferenceValidation referenceValidation : referenceValidations) {
            if (referenceValidation.isIntact()) {
                if (originalDocuments.size() == 1) {
                    if (cbadesSignature.isCounterSignature()) {
                        // only one document shall be present
                        return Collections.singletonList(new CounterSignatureScope(cbadesSignature.getMasterSignature(), originalDocuments.get(0)));
                    } else {
                        return Collections.singletonList(getSignatureScopeFromOriginalDocument(originalDocuments.get(0)));
                    }

                } else if (referenceValidations.size() == 1) {
                    return getSignatureScopeFromOriginalDocuments(originalDocuments);

                } else if (referenceValidation.getUri() != null) {
                    DSSDocument documentByName = getDocumentByName(originalDocuments, referenceValidation.getUri());
                    result.add(getSignatureScopeFromOriginalDocument(documentByName));

                }
            }
        }

        return result;
    }

    /**
     * Returns original documents for the given CBAdES signature
     *
     * @param cbadesSignature {@link CBAdESSignature} to get original document for
     * @return a list of {@link DSSDocument}s original document
     */
    protected List<DSSDocument> getOriginalDocuments(final CBAdESSignature cbadesSignature) {
        try {
            return cbadesSignature.getOriginalDocuments();
        } catch (DSSException e) {
            LOG.warn("A CB-AdES signer's original document is not found [{}].", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Returns a {@code SignatureScope} for the given {@code originalDocument}
     *
     * @param originalDocument {@link DSSDocument} to get a SignatureScope for
     * @return {@link SignatureScope}
     */
    protected SignatureScope getSignatureScopeFromOriginalDocument(DSSDocument originalDocument) {
        if (originalDocument instanceof DigestDocument) {
            DigestDocument digestDocument = (DigestDocument) originalDocument;
            return new DigestSignatureScope(originalDocument.getName(), digestDocument);

        } else {
            return new FullSignatureScope(originalDocument.getName(), originalDocument);
        }
    }

    /**
     * Returns a DSSDocument with the given name from the available list of documents
     *
     * @param documents a list of {@link DSSDocument}s
     * @param documentName {@link String} document name to extract
     * @return {@link DSSDocument}
     */
    private DSSDocument getDocumentByName(List<DSSDocument> documents, String documentName) {
        documentName = DSSUtils.decodeURI(documentName);
        for (DSSDocument document : documents) {
            if (documentName.equals(document.getName())) {
                return document;
            }
        }
        return null;
    }

    /**
     * Extracts a SignatureScope list from a list of original documents
     *
     * @param originalDocuments a list of {@link DSSDocument} original documents
     * @return a list of {@link SignatureScope}s
     */
    protected List<SignatureScope> getSignatureScopeFromOriginalDocuments(List<DSSDocument> originalDocuments) {
        List<SignatureScope> result = new ArrayList<>();
        if (Utils.isCollectionEmpty(originalDocuments)) {
            return result;
        }

        for (DSSDocument originalDocument : originalDocuments) {
            String documentName = originalDocument.getName() != null ? originalDocument.getName() : "Detached content";
            if (originalDocument instanceof DigestDocument) {
                DigestDocument digestDocument = (DigestDocument) originalDocument;
                result.add(new DigestSignatureScope(documentName, digestDocument));

            } else {
                result.add(new FullSignatureScope(documentName, originalDocument));

            }
        }

        return result;
    }

}
