package eu.europa.esig.dss.eaa.common.creation.claim;

import java.io.Serializable;

/**
 * Base interface for defining a claim
 */
public interface EAAClaim extends Serializable {

    Object getName();
    Object getValue();
}
