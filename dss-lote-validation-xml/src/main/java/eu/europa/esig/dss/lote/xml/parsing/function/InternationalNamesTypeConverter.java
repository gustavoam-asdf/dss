package eu.europa.esig.dss.lote.xml.parsing.function;

import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.lote.jaxb.InternationalNamesType;
import eu.europa.esig.lote.jaxb.MultiLangNormStringType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class converts a JAXB {@code InternationalNamesType} object to a Java map
 *
 */
public class InternationalNamesTypeConverter implements Function<InternationalNamesType, Map<String, List<String>>> {

    /** The predicate to be used */
    private final Predicate<String> predicate;

    /**
     * Default constructor (selects all)
     */
    public InternationalNamesTypeConverter() {
        // select all
        this(x -> true);
    }

    /**
     * Default constructor with a filter predicate
     *
     * @param predicate {@link Predicate}
     */
    public InternationalNamesTypeConverter(Predicate<String> predicate) {
        super();
        this.predicate = predicate;
    }

    @Override
    public Map<String, List<String>> apply(InternationalNamesType original) {
        Map<String, List<String>> result = new HashMap<>();
        if (original != null && Utils.isCollectionNotEmpty(original.getName())) {
            for (MultiLangNormStringType multiLangNormString : original.getName()) {
                final String lang = multiLangNormString.getLang();
                final String value = multiLangNormString.getValue();
                if (predicate.test(value)) {
                    result.computeIfAbsent(lang, k -> new ArrayList<>()).add(value);
                }
            }
        }
        return result;
    }

}
