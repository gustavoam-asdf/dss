package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A wrapper of a CBOR array object
 *
 */
public class CBORArray extends AbstractCBORObject {

    /** Implementation of a CBOR Array object */
    private final Array array;

    /**
     * Constructor to create an empty array
     */
    public CBORArray() {
        this(new Array());
    }

    /**
     * Constructor to create an empty array with an initial size
     *
     * @param arraySize initial size of the array
     */
    public CBORArray(int arraySize) {
        this(new Array(arraySize));
    }

    /**
     * Constructor to create a CBORArray from an Array implementation
     *
     * @param array {@link co.nstant.in.cbor.model.Array}
     */
    public CBORArray(final Array array) {
        super(array);
        this.array = array;
    }

    /**
     * Constructor to create a CBORArray from a List of objects
     *
     * @param list {@link List}
     */
    public CBORArray(final List<?> list) {
        this(toArray(list));
    }

    private static Array toArray(final List<?> list) {
        Array array = new Array(list.size());
        list.forEach(l -> array.add(CBORUtils.toDataItem(l)));
        return array;
    }

    /**
     * Constructor to create a CBORArray from an array of objects
     *
     * @param array {@link List}
     */
    public <T> CBORArray(final T[] array) {
        this(toArray(array));
    }

    private static <T> Array toArray(final T[] inputArray) {
        Array array = new Array(inputArray.length);
        for (Object object : inputArray) {
            array.add(CBORUtils.toDataItem(object));
        }
        return array;
    }

    /**
     * This method adds a new object to the array in the latest position, by extending the array.
     * The method transforms the given Object to a supported DataItem type.
     *
     * @param object {@link Object} to add
     */
    public void add(Object object) {
        array.add(CBORUtils.toDataItem(object));
    }

    /**
     * Checks if the given array is empty
     *
     * @return TRUE if the array is empty, FALSE otherwise
     */
    public boolean isEmpty() {
        return Utils.isCollectionEmpty(array.getDataItems());
    }

    /**
     * Returns a list of embedded {@code CBORObject}s
     *
     * @return a list of {@link CBORObject}s
     */
    public List<CBORObject> getItems() {
        return array.getDataItems().stream().map(CBORUtils::toCBORObject).collect(Collectors.toList());
    }

    @Override
    public DataItem toDataItem() {
        return array;
    }
}
