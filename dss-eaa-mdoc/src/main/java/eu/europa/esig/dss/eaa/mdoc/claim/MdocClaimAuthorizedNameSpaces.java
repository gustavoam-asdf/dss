package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all the namespaces the key may sign or MAC.
 *
 */
public class MdocClaimAuthorizedNameSpaces extends MdocClaimArray {

    private static final long serialVersionUID = 1383542498839236404L;

    private static final Logger LOG = LoggerFactory.getLogger(MdocClaimAuthorizedNameSpaces.class);

    /**
     * Constructor to initialize MdocClaimAuthorizedNameSpaces from a ClaimMap
     *
     * @param value {@link ClaimArray}
     */
    public MdocClaimAuthorizedNameSpaces(ClaimArray value) {
        super(value.getName(), value.getNamespace(), value.getListValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    /**
     * Gets a list of namespaces the key is allowed to sign or MAC
     *
     * @return a list of {@link String} namespaces
     */
    public List<String> getNamespaces() {
        List<Claim> listValue = getListValue();
        if (Utils.isCollectionEmpty(listValue)) {
            return Collections.emptyList();
        }
        final List<String> namespaces = new ArrayList<>();
        for (Claim namespaceClaim : listValue) {
            if (namespaceClaim.isStringValueType()) {
                namespaces.add(namespaceClaim.getValueAsString());
            } else {
                LOG.warn("The entry of AuthorizedNameSpaces shall be a type of CBOR String!");
            }
        }
        return namespaces;
    }

}
