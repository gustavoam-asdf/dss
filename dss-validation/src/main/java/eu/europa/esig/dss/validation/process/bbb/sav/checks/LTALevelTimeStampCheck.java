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
package eu.europa.esig.dss.validation.process.bbb.sav.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlTimestamp;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;

import java.util.Collection;
import java.util.Map;

/**
 * Verifies if there is at least one valid LTA-level timestamp
 *
 * @param <T> {@code XmlConstraintsConclusion}
 */
public class LTALevelTimeStampCheck<T extends XmlConstraintsConclusion> extends AbstractTimeStampPresentCheck<T> {

    /** The signature to check */
    private final SignatureWrapper signature;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlConstraintsConclusion}
     * @param signature {@link SignatureWrapper}
     * @param bbbs map between token ids and corresponding {@code XmlBasicBuildingBlocks}
     * @param xmlTimestamps a collection of {@link XmlTimestamp}s
     * @param constraint {@link LevelRule}
     */
    public LTALevelTimeStampCheck(I18nProvider i18nProvider, T result, SignatureWrapper signature,
                                Map<String, XmlBasicBuildingBlocks> bbbs, Collection<XmlTimestamp> xmlTimestamps,
                                LevelRule constraint) {
        super(i18nProvider, result, bbbs, xmlTimestamps, constraint);
        this.signature = signature;
    }

    @Override
    protected Collection<TimestampWrapper> getTimestamps() {
        return signature.getALevelTimestamps();
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.BBB_SAV_IVLTATSTP;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.BBB_SAV_IVLTATSTP_ANS;
    }

}
