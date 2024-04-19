package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.UnsignedInteger;

public class COSEMap {

    private final co.nstant.in.cbor.model.Map map;

    public COSEMap(final co.nstant.in.cbor.model.Map map) {
        this.map = map;
    }

    public DataItem getHeader(long key) {
        return map.get(new UnsignedInteger(key));
    }

    public DataItem getHeader(DataItem key) {
        return map.get(key);
    }

    public Long getHeaderAsLong(long key) {
        DataItem dataItem = getHeader(key);
        if (dataItem != null && (MajorType.UNSIGNED_INTEGER == dataItem.getMajorType() || MajorType.NEGATIVE_INTEGER == dataItem.getMajorType())) {
            return ((co.nstant.in.cbor.model.Number) dataItem).getValue().longValue();
        }
        return null;
    }

}
