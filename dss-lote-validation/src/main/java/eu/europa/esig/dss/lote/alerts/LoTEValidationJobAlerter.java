package eu.europa.esig.dss.lote.alerts;

import eu.europa.esig.dss.alert.Alert;
import eu.europa.esig.dss.model.lote.ListInfo;
import eu.europa.esig.dss.model.lote.LoTEValidationJobSummary;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoTEValidationJobAlerter {

    private static final Logger LOG = LoggerFactory.getLogger(LoTEValidationJobAlerter.class);

    /**
     * Contains a list of TL alerts
     */
    private final List<Alert<ListInfo>> alerts;

    /**
     * The constructor to instantiate a TLValidationJobAlerter
     *
     * @param alerts a list of {@link ListInfo}s to be applied on TL changes
     */
    public LoTEValidationJobAlerter(final List<Alert<ListInfo>> alerts) {
        this.alerts = alerts;
    }

    /**
     * The method to run alerts on the given LoTEValidationJobSummary
     *
     * @param jobSummary {@link LoTEValidationJobSummary} to execute alerts on
     */
    public void detectChanges(final LoTEValidationJobSummary jobSummary) {
        // other TLs
        if (Utils.isCollectionNotEmpty(alerts)) {
            for (ListInfo loteInfo : jobSummary.getListInfos()) {
                for (Alert<ListInfo> alert : alerts) {
                    execute(alert, loteInfo);
                }
            }
        }
    }

    private <T extends ListInfo> void execute(Alert<T> alert, T info) {
        try {
            alert.alert(info);
        } catch (Exception e) {
            LOG.warn("An error occurred while trying to detect changes inside '{}'. Reason : {}",
                    info.getDSSId().asXmlId(), e.getMessage());
        }
    }

}