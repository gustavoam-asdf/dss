package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;

import java.util.Date;

public class TrustedEntityServiceByDateFilter extends AbstractTrustedEntityServiceFilter {

    /** Time to filter by */
    private final Date date;

    /**
     * Default constructor
     *
     * @param date {@link Date} to filter TrustServices valid at the time
     */
    public TrustedEntityServiceByDateFilter(Date date) {
        this.date = date;
    }

    @Override
    protected boolean isAcceptable(TrustedEntityServiceWrapper service) {
        Date startDate = service.getStartDate();
        Date endDate = service.getEndDate();

        if (date == null) { // possible in case of null signing time
            return false;
        }

        boolean afterStartRange = (startDate != null && (date.compareTo(startDate) >= 0));
        boolean beforeEndRange = (endDate == null || (date.compareTo(endDate) <= 0)); // end date can be null (in case
        // of current status)

        return afterStartRange && beforeEndRange;
    }

}
