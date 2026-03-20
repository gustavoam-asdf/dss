package eu.europa.esig.dss.eaa.mdoc.model;

/**
 * Contains error cde for not returned document within an mdoc structure
 *
 */
public class MdocDocumentError {

    /** DocType of the corresponding document */
    private String docType;

    /** Error code for the concerned document */
    private Long errorCode;

    /**
     * Default constructor
     */
    public MdocDocumentError() {
        // empty
    }

    /**
     * Gets the docType of the related document
     *
     * @return {@link String}
     */
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the docType of the related document
     *
     * @param docType {@link String}
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * Gets an error code for the concerned document
     *
     * @return {@link Long}
     */
    public Long getErrorCode() {
        return errorCode;
    }

    /**
     * Sets an error code for the concerned document
     *
     * @param errorCode {@link Long}
     */
    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

}
