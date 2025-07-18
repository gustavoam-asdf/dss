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

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.TimestampType;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;

/**
 * Checks if a validation-data-time-stamp attribute is present
 *
 */
public class ValidationDataTimeStampCheck extends AbstractTimeStampTypeCheck {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param signature {@link SignatureWrapper}
     * @param constraint {@link LevelRule}
     */
    public ValidationDataTimeStampCheck(I18nProvider i18nProvider, XmlSAV result, SignatureWrapper signature,
                                   LevelRule constraint) {
        super(i18nProvider, result, signature, constraint);
    }

    @Override
    protected TimestampType getTimestampType() {
        return TimestampType.VALIDATION_DATA_TIMESTAMP;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.BBB_SAV_IUQPVDTSP;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.BBB_SAV_IUQPVDTSP_ANS;
    }

}
