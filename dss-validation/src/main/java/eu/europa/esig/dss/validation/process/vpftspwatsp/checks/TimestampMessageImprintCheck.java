/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * <p>
 * This file is part of the "DSS - Digital Signature Services" project.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.validation.process.vpftspwatsp.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Checks message-imprint validity for a timestamp token
 *
 * @param <T> implementation of the block's conclusion
 */
public class TimestampMessageImprintCheck<T extends XmlConstraintsConclusion> extends ChainItem<T> {

    /** The timestamp to check */
    protected final TimestampWrapper timestamp;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param timestamp {@link TimestampWrapper}
     * @param constraint {@link LevelRule}
     */
    public TimestampMessageImprintCheck(I18nProvider i18nProvider, T result, TimestampWrapper timestamp,
                                 LevelRule constraint) {
        this(i18nProvider, result, timestamp, constraint, null);
    }

    /**
     * Constructor to instantiate check with Id provided
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param timestamp {@link TimestampWrapper}
     * @param constraint {@link LevelRule}
     * @param bbbId {@link String} if of the corresponding basic building block
     */
    protected TimestampMessageImprintCheck(I18nProvider i18nProvider, T result, TimestampWrapper timestamp,
                                        LevelRule constraint, String bbbId) {
        super(i18nProvider, result, constraint, bbbId);
        this.timestamp = timestamp;
    }

    @Override
    protected boolean process() {
        return timestamp.isMessageImprintDataFound() && timestamp.isMessageImprintDataIntact();
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.BBB_SAV_DMICTSTMCMI;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.BBB_SAV_DMICTSTMCMI_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.INDETERMINATE;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.SIG_CONSTRAINTS_FAILURE;
    }

}
