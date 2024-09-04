package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.Tag;

/**
 * Represents a wrapper for a CBOR Object's Tag
 *
 */
public class CBORTag extends AbstractCBORObject {

    /** The Tag implementation */
    private final Tag tag;

    /**
     * Creates a new CBOR Tag object from the given {@code tag}
     *
     * @param tag {@link Tag}
     */
    public CBORTag(final Tag tag) {
        super(tag);
        this.tag = tag;
    }

    /**
     * Returns the Tag value
     *
     * @return long value
     */
    public long getValue() {
        return tag.getValue();
    }

}
