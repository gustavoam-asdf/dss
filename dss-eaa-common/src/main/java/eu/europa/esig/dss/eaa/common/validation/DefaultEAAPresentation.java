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
package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.eaa.EAA;

import java.util.List;

/**
 * Abstract implementation of an EAA Presentation
 */
public abstract class DefaultEAAPresentation implements EAAPresentation {

    /** Type of the EAA Presentation */
    private EAAPresentationType eaaPresentationType;

    /** List of incorporated Electronic Attestations of Attributes */
    private List<EAA> electronicAttestationsOfAttributes;

    /**
     * Default constructor
     */
    protected DefaultEAAPresentation() {
        // empty
    }

    @Override
    public EAAPresentationType getEAAPresentationType() {
        return eaaPresentationType;
    }

    /**
     * Sets the type of the EAA Presentation document
     *
     * @param eaaPresentationType {@link EAAPresentationType}
     */
    public void setEAAPresentationType(EAAPresentationType eaaPresentationType) {
        this.eaaPresentationType = eaaPresentationType;
    }

    @Override
    public List<EAA> getElectronicAttestationsOfAttributes() {
        return electronicAttestationsOfAttributes;
    }

    /**
     * Sets a list of incorporated Electronic Attestations of Attributes
     *
     * @param electronicAttestationsOfAttributes a list of {@link EAA}
     */
    public void setElectronicAttestationsOfAttributes(List<EAA> electronicAttestationsOfAttributes) {
        this.electronicAttestationsOfAttributes = electronicAttestationsOfAttributes;
    }

}
