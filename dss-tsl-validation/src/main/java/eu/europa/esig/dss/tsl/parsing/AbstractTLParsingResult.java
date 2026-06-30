package eu.europa.esig.dss.tsl.parsing;

import eu.europa.esig.dss.enumerations.TSLType;
import eu.europa.esig.dss.validation.job.parsing.AbstractParsingResult;

import java.util.Date;
import java.util.List;

public abstract class AbstractTLParsingResult extends AbstractParsingResult {

    /** The LOTL/TL TSLType */
    private TSLType tslType;

    /** The LOTL/TL sequence number */
    private Integer sequenceNumber;

    /** LOTL/TL version */
    private Integer version;

    /** The country (territory) */
    private String territory;

    /** The issuance date of the LOTL/TL */
    private Date issueDate;

    /** The next update date */
    private Date nextUpdateDate;

    /** The distribution points urls */
    private List<String> distributionPoints;

    /** A list of error messages occurred during a structure validation */
    protected List<String> structureValidationMessages;

    /**
     * Default constructor instantiating object with null values
     */
    protected AbstractTLParsingResult() {
        // empty
    }

    /**
     * Gets the TSLType
     *
     * @return {@link TSLType}
     */
    public TSLType getTSLType() {
        return tslType;
    }

    /**
     * Sets the TSLType
     *
     * @param tslType {@link String}
     */
    public void setTSLType(TSLType tslType) {
        this.tslType = tslType;
    }

    /**
     * Gets the sequence number
     *
     * @return sequence number
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the sequence number
     *
     * @param sequenceNumber sequence number
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Gets the version
     *
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the version
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Gets the territory (country)
     *
     * @return {@link String}
     */
    public String getTerritory() {
        return territory;
    }

    /**
     * Sets the territory
     *
     * @param territory {@link String}
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }

    /**
     * Gets issuing date
     *
     * @return {@link Date}
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the issue date
     *
     * @param issueDate {@link Date}
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Gets next update date
     *
     * @return {@link Date}
     */
    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    /**
     * Sets the next update date
     *
     * @param nextUpdateDate {@link Date}
     */
    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    /**
     * Gets distribution points
     *
     * @return a list of {@link String}s
     */
    public List<String> getDistributionPoints() {
        return distributionPoints;
    }

    /**
     * Sets a list of distribution point urls
     *
     * @param distributionPoints a list of {@link String}s
     */
    public void setDistributionPoints(List<String> distributionPoints) {
        this.distributionPoints = distributionPoints;
    }

}
