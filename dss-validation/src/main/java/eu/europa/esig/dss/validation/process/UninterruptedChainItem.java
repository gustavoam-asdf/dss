package eu.europa.esig.dss.validation.process;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.LevelRule;

/**
 * This class allows to continue the chain validation process in case of a check failure
 *
 * @see Chain
 * @param <T> constraint conclusion
 */
public abstract class UninterruptedChainItem<T extends XmlConstraintsConclusion> extends ChainItem<T> {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlConstraintsConclusion}
     * @param constraint {@link LevelRule}
     */
    protected UninterruptedChainItem(I18nProvider i18nProvider, T result, LevelRule constraint) {
        super(i18nProvider, result, constraint);
    }

    /**
     * Constructor with custom Id
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlConstraintsConclusion}
     * @param constraint {@link LevelRule}
     * @param id {@link String}
     */
    protected UninterruptedChainItem(I18nProvider i18nProvider, T result, LevelRule constraint, String id) {
        super(i18nProvider, result, constraint, id);
    }

    @Override
    protected boolean continueProcessOnFail() {
        return true;
    }

}
