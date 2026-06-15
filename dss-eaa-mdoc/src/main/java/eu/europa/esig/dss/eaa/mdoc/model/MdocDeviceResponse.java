package eu.europa.esig.dss.eaa.mdoc.model;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a complete parsed mDoc as per ISO 18013-5 "8.3.2.1.2 Message structures".
 *
 */
public class MdocDeviceResponse implements Serializable {

    private static final long serialVersionUID = 6963076674532828037L;

    /**
     * Version of the message structure.
     * For the ISO 18013-5 compliant documents, the value shall be "1.0"
     */
    private String version;

    /**
     * List of returned documents within mDoc message
     */
    private List<MdocDocument> documents;

    /**
     * List of document types and corresponding error codes for not returned documents
     */
    private List<MdocDocumentError> documentErrors;

    /**
     * Status code of the mDoc message
     */
    private Long status;

    /**
     * Default constructor
     */
    public MdocDeviceResponse() {
        // empty
    }

    /**
     * Gets the document version.
     * NOTE: Shall be equal to "1.0" for ISO 18013-5 compliant documents
     *
     * @return {@link String}
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the document version.
     * NOTE: Shall be equal to "1.0" for ISO 18013-5 compliant documents
     *
     * @param version {@link String}
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the returned documents
     *
     * @return a list of {@link MdocDocument}s
     */
    public List<MdocDocument> getDocuments() {
        return documents;
    }

    /**
     * Sets the returned documents
     *
     * @param documents a list of {@link MdocDocument}s
     */
    public void setDocuments(List<MdocDocument> documents) {
        this.documents = documents;
    }

    /**
     * Gets a list of document types and error codes for not returned documents
     *
     * @return a list of document errors
     */
    public List<MdocDocumentError> getDocumentErrors() {
        return documentErrors;
    }

    /**
     * Sets a list of document types and error codes for not returned documents
     *
     * @param documentErrors a list of document errors
     */
    public void setDocumentErrors(List<MdocDocumentError> documentErrors) {
        this.documentErrors = documentErrors;
    }

    /**
     * Gets the status code of the mdoc message
     *
     * @return {@link Long}
     */
    public Long getStatus() {
        return status;
    }

    /**
     * Sets the status code of the mdoc message
     *
     * @param status {@link Long}
     */
    public void setStatus(Long status) {
        this.status = status;
    }

}
