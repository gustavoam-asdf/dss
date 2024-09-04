package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORMap;

public class COSEUnprotectedHeader extends CBORMap {

    public COSEUnprotectedHeader() {
        super();
    }

    public COSEUnprotectedHeader(final CBORMap headerMap) {
        super((co.nstant.in.cbor.model.Map) headerMap.toDataItem());
    }

}
