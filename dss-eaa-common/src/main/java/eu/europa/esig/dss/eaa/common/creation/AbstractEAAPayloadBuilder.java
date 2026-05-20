package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;

/**
 *
 */
public abstract class AbstractEAAPayloadBuilder<P extends EAAPayloadParameters, C extends EAAClaim, D extends EAADisclosure> implements EAAPayloadBuilder<P, C, D> {

    protected EAASaltGenerator saltGenerator = new DefaultEAASaltGenerator();

    public void setSaltGenerator(final EAASaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }

}
