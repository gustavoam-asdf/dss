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
package eu.europa.esig.dss.xades.evidencerecord;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.DSSMessageDigest;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.spi.DSSMessageDigestCalculator;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.SignatureAttribute;
import eu.europa.esig.dss.spi.validation.evidencerecord.AbstractSignatureEvidenceRecordDigestBuilder;
import eu.europa.esig.dss.spi.validation.evidencerecord.ByteArrayComparator;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.xades.DSSXMLUtils;
import eu.europa.esig.dss.xades.XAdESSignatureUtils;
import eu.europa.esig.dss.xades.definition.XAdESPath;
import eu.europa.esig.dss.xades.reference.ReferenceOutputType;
import eu.europa.esig.dss.xades.validation.XAdESAttribute;
import eu.europa.esig.dss.xades.validation.XAdESSignature;
import eu.europa.esig.dss.xades.validation.XAdESUnsignedSigProperties;
import eu.europa.esig.dss.xades.validation.XMLDocumentAnalyzer;
import eu.europa.esig.dss.xml.common.definition.xmldsig.XMLDSigPath;
import eu.europa.esig.dss.xml.utils.DomUtils;
import eu.europa.esig.dss.xml.utils.XMLCanonicalizer;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.Manifest;
import org.apache.xml.security.signature.Reference;
import org.apache.xml.security.signature.ReferenceNotInitializedException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Computes message-imprint of an XML signature to be protected by an evidence-record
 *
 */
public class XAdESEvidenceRecordDigestBuilder extends AbstractSignatureEvidenceRecordDigestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(XAdESEvidenceRecordDigestBuilder.class);

    /**
     * The list of detached documents covered by the signature
     */
    private List<DSSDocument> detachedContent;

    /**
     * DSS identifier or a signature's identifier of the signature to be covered by an evidence-record
     */
    private String signatureId;

    /**
     * Default constructor to instantiate XAdESEvidenceRecordDigestBuilder with a SHA-256 digest algorithm
     *
     * @param signatureDocument {@link DSSDocument} to compute message-imprint for
     */
    public XAdESEvidenceRecordDigestBuilder(final DSSDocument signatureDocument) {
        super(signatureDocument);
    }

    /**
     * Constructor to instantiate XAdESEvidenceRecordDigestBuilder with a custom digest algorithm
     *
     * @param signatureDocument {@link DSSDocument} to compute message-imprint for
     * @param digestAlgorithm {@link DigestAlgorithm} to be used
     */
    public XAdESEvidenceRecordDigestBuilder(final DSSDocument signatureDocument, final DigestAlgorithm digestAlgorithm) {
        super(signatureDocument, digestAlgorithm);
    }

    /**
     * Constructor to instantiate XAdESEvidenceRecordDigestBuilder from a {@code signature}
     * for the given {@code evidenceRecordAttribute}.
     * This constructor is used on validation of existing evidence record.
     *
     * @param signature {@link AdvancedSignature} containing the incorporated evidence record
     * @param evidenceRecordAttribute {@link SignatureAttribute} location of the evidence record
     * @param digestAlgorithm {@link DigestAlgorithm} to be used
     */
    protected XAdESEvidenceRecordDigestBuilder(final AdvancedSignature signature, final SignatureAttribute evidenceRecordAttribute,
                                               final DigestAlgorithm digestAlgorithm) {
        super(signature, evidenceRecordAttribute, digestAlgorithm);
    }

    /**
     * Sets a list of detached documents covered by the signature
     *
     * @param detachedContent a list of detached {@link DSSDocument}s
     * @return this builder
     */
    public XAdESEvidenceRecordDigestBuilder setDetachedContent(List<DSSDocument> detachedContent) {
        this.detachedContent = detachedContent;
        return this;
    }

    /**
     * Sets identifier of the signature to be covered by an evidence-record. Accepts a DSS identifier,
     * or an internal signature element's identifier
     * Note: required for documents containing multiple signatures
     *
     * @param signatureId {@link String}
     * @return this builder
     */
    public XAdESEvidenceRecordDigestBuilder setSignatureId(String signatureId) {
        this.signatureId = signatureId;
        return this;
    }

    @Override
    public XAdESEvidenceRecordDigestBuilder setParallelEvidenceRecord(boolean parallelEvidenceRecord) {
        return (XAdESEvidenceRecordDigestBuilder) super.setParallelEvidenceRecord(parallelEvidenceRecord);
    }

    @Override
    public Digest build() {
        final XAdESSignature xadesSignature = getXAdESSignature();
        return getXmlSignatureMessageImprint(xadesSignature);
    }

    /**
     * Returns a signature to compute evidence record's digest for
     *
     * @return {@link XAdESSignature}
     */
    protected XAdESSignature getXAdESSignature() {
        if (signature != null) {
            return (XAdESSignature) signature;

        } else if (signatureDocument != null) {
            final XMLDocumentAnalyzer documentAnalyzer = new XMLDocumentAnalyzer(signatureDocument);
            documentAnalyzer.setDetachedContents(detachedContent);

            AdvancedSignature signature;
            final List<AdvancedSignature> signatures = documentAnalyzer.getSignatures();
            if (Utils.collectionSize(signatures) == 0) {
                throw new IllegalInputException("The provided document does not contain any signature! " +
                        "Unable to compute message-imprint for an integrated evidence-record.");

            } else if (Utils.isStringNotEmpty(signatureId)) {
                signature = documentAnalyzer.getSignatureById(signatureId);
                if (signature == null) {
                    throw new IllegalArgumentException(
                            String.format("No signature with Id '%s' found in the document!", signatureId));
                }

            } else if (Utils.collectionSize(signatures) > 1) {
                throw new IllegalInputException("The provided document contains multiple signatures! " +
                        "Please use #setSignatureId method in order to provide the identifier.");

            } else {
                signature = signatures.get(0);
            }

            return (XAdESSignature) signature;

        } else {
            throw new IllegalStateException("Either DSSDocument containing the signature or AdvancedSignature shall be defined!");
        }
    }

    /**
     * Generates message-imprint for the given {@code XAdESSignature}
     *
     * @param signature {@link XAdESSignature} to be covered by an evidence-record
     * @return {@link Digest} of the signature
     */
    protected DSSMessageDigest getXmlSignatureMessageImprint(XAdESSignature signature) {
        try {
            /*
             * The initial time-stamp token encapsulated within the first ArchiveTimeStamp of any of
             * the evidence-records enclosed within the xadesen:SealingEvidenceRecords unsigned qualifying property,
             * shall incorporate a HashTree, whose first child shall contain the digest value of the group of
             * data objects listed below, concatenated in the order specified in IETF RFC 6283 [5] if
             * the xadesen:SealingEvidenceRecords unsigned qualifying property contains XMLERS evidence-records, or
             * in IETF RFC 4998 [8] if the xadesen:SealingEvidenceRecords unsigned qualifying property contains
             * ERS evidence-records:
             */
            final List<byte[]> digestObjectsGroup = new ArrayList<>();
            byte[] digestValue = null;

            /*
             * 1) The data objects resulting of processing each ds:Reference element within ds:SignedInfo as
             * specified below:
             * - Process the ds:Reference element according to the reference processing model of XMLDSIG [7],
             *   clause 4.4.3.2.
             * - If the result is a XML node set, canonicalize using the canonicalization algorithm present in
             *   ds:CanonicalizationMethod element.
             */
            if (LOG.isTraceEnabled()) {
                LOG.trace("Step 1): Processing ds:Reference's within ds:SignedInfo");
            }
            // TODO : not clear which ds:CanonicalizationMethod to use. Using ds:SignedInfo/ds:CanonicalizationMethod for now
            final String canonicalizationAlgorithm = getCanonicalizationAlgorithm(signature);

            for (final Reference reference : signature.getReferences()) {
                digestValue = getReferenceBytesDigestValue(reference, canonicalizationAlgorithm);
                digestObjectsGroup.add(digestValue);
            }

            /*
             * 2) The data objects resulting of taking the XMLDSIG elements listed below, and canonicalizing
             * each one using the canonicalization algorithm present in ds:CanonicalizationMethod element:
             * - The ds:SignedInfo element.
             * - The ds:SignatureValue element.
             * - The ds:KeyInfo element, if present.
             */
            if (LOG.isTraceEnabled()) {
                LOG.trace("Step 2): Canonicalization of ds:SignedInfo, ds:SignatureValue, ds:KeyInfo element");
            }
            digestValue = getDigestValueOnCanonicalizedNode(signature, XMLDSigPath.SIGNED_INFO_PATH, canonicalizationAlgorithm);
            digestObjectsGroup.add(digestValue);

            digestValue = getDigestValueOnCanonicalizedNode(signature, XMLDSigPath.SIGNATURE_VALUE_PATH, canonicalizationAlgorithm);
            digestObjectsGroup.add(digestValue);

            digestValue = getDigestValueOnCanonicalizedNode(signature, XMLDSigPath.KEY_INFO_PATH, canonicalizationAlgorithm);
            digestObjectsGroup.add(digestValue);

            // Steps 3) and 4) are done together (signature is expected to be prepared)
            /*
             * 3) The data objects resulting of taking all the unsigned qualifying properties incorporated into the XAdES
             * signature except the xadesen:SealingEvidenceRecords element under construction, and
             * canonicalizing each one as specified in clause 4.5 of ETSI EN 319 132-1 [1].
             */
            /*
             * 4) As many xadesv141:TimeStampValidationData qualifying properties will be added as required for
             * incorporating the validation data, not already present in the XAdES signature, that are required for validating
             * all the time-stamp tokens incorporated (within signed or unsigned qualifying properties) into the XAdES
             * qualifying properties different than xadesen:SealingEvidenceRecords. Each xadesv141:TimeStampValidationData
             * shall be generated following the specifications of ETSI EN 319 132-1 [1]. For every
             * xadesv141:TimeStampValidationData qualifying property incorporated, the corresponding data object
             * resulting of canonicalizing this qualifying property as specified in clause 4.5 of ETSI EN 319 132-1 [1]
             * will be generated and added to the group of data objects to be time stamped.
             */
            if (LOG.isTraceEnabled()) {
                LOG.trace("Step 3): Processing of unsigned qualifying properties");
            }
            List<XAdESAttribute> unsignedProperties = getUnsignedSignaturePropertiesList(signature);
            if (Utils.isCollectionNotEmpty(unsignedProperties)) {
                for (XAdESAttribute xadesAttribute : unsignedProperties) {
                    digestValue = getDigestValueOnCanonicalizedNode(xadesAttribute.getElement(), canonicalizationAlgorithm);
                    digestObjectsGroup.add(digestValue);
                }
            }

            /*
             * 5) All the ds:Object elements except the one containing QualifyingProperties element, as specified
             * in step 5) of clause 5.5.2.2 of ETSI EN 319 132-1 [1].
             */
            if (LOG.isTraceEnabled()) {
                LOG.trace("Step 5): Processing of ds:Object's");
            }
            for (Node object : getObjects(signature)) {
                if (!containsQualifyingProperties(object, signature.getXAdESPaths())) {
                    digestValue = getDigestValueOnCanonicalizedNode(object, canonicalizationAlgorithm);
                    digestObjectsGroup.add(digestValue);
                }
            }

            /*
             * 6) The objects derived from the presence of signed ds:Manifest elements. These objects shall be generated
             * as it is specified below:
             */
            if (LOG.isTraceEnabled()) {
                LOG.trace("Step 6): Processing of ds:Manifest's");
            }
            for (final Reference reference : signature.getReferences()) {
                if (reference.typeIsReferenceToManifest()) {
                    List<byte[]> manifestDataObjectDigests = getManifestDataObjectDigests(signature, reference, canonicalizationAlgorithm);
                    digestObjectsGroup.addAll(manifestDataObjectDigests);
                }
            }

            // compute final digest
            final DSSMessageDigest dataGroupDigest = computeDigestValueGroupHash(digestObjectsGroup);
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("Evidence-record signature data group digest: %s", dataGroupDigest));
            }
            return dataGroupDigest;

        } catch (XMLSecurityException | IOException e) {
            throw new DSSException(String.format("Unable to compute message-imprint for an evidence-record. " +
                    "Reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Returns corresponding ds:CanonicalizationMethod used within the signature
     *
     * @param signature {@link XAdESSignature}
     * @return {@link String} canonicalization method identifier
     */
    protected String getCanonicalizationAlgorithm(XAdESSignature signature) {
        Element signedInfo = signature.getSignedInfo();
        if (signedInfo == null) {
            throw new IllegalStateException("ds:SignedInfo element shall be defined within a signature!");
        }
        String canonicalizationMethod = DomUtils.getValue(signedInfo, XMLDSigPath.CANONICALIZATION_ALGORITHM_PATH);
        if (Utils.isStringEmpty(canonicalizationMethod)) {
            LOG.warn("No canonicalization method found within ds:SignedInfo element. " +
                    "Re-use the default canonicalization algorithm 'http://www.w3.org/TR/2001/REC-xml-c14n-20010315'");
            canonicalizationMethod = XMLCanonicalizer.DEFAULT_XMLDSIG_C14N_METHOD;
        }
        return canonicalizationMethod;
    }

    private byte[] getReferenceBytesDigestValue(final Reference reference, final String canonicalizationAlgorithm) throws XMLSecurityException, IOException {
        try {
            /*
             * 1) process the retrieved ds:Reference element according to the reference-processing model of XMLDSIG [1]
             * clause 4.4.3.2;
             */

            byte[] digest;
            /*
             * 2) If the result is a XML node set, canonicalize using the canonicalization algorithm present in
             *   ds:CanonicalizationMethod element.
             */
            if (isResultXmlNodeSet(reference)) {
                final byte[] referencedBytes = reference.getReferencedBytes();
                if (DomUtils.isDOM(referencedBytes)) {
                    digest = DSSXMLUtils.getDigestOnCanonicalizedBytes(referencedBytes, digestAlgorithm, canonicalizationAlgorithm).getValue();
                } else {
                    digest = DSSUtils.digest(digestAlgorithm, referencedBytes);
                }
            } else {
                XMLSignatureInput input = reference.getContentsAfterTransformation();
                digest = getDigestValueOnInputStream(input.getOctetStream());
            }
            return digest;

        } catch (ReferenceNotInitializedException e) {
            throw new DSSException(String.format("An error occurred on ds:Reference processing. In case of detached signature, " +
                    "please use #setDetachedContent method to provide original documents. More information : %s", e.getMessage()), e);
        }
    }

    private byte[] getDigestValueOnInputStream(InputStream is) throws IOException {
        final DSSMessageDigestCalculator messageDigestCalculator = new DSSMessageDigestCalculator(digestAlgorithm);
        messageDigestCalculator.update(is);
        return messageDigestCalculator.getMessageDigest(digestAlgorithm).getValue();
    }

    private byte[] getDigestValueOnCanonicalizedNode(final XAdESSignature signature, final String xPathString,
                                                     final String canonicalizationAlgorithm) {
        final Element element = DomUtils.getElement(signature.getSignatureElement(), xPathString);
        return getDigestValueOnCanonicalizedNode(element, canonicalizationAlgorithm);
    }

    private byte[] getDigestValueOnCanonicalizedNode(final Node node,
                                                     final String canonicalizationAlgorithm) {
        return DSSXMLUtils.getDigestOnCanonicalizedNode(node, digestAlgorithm, canonicalizationAlgorithm).getValue();
    }

    private List<XAdESAttribute> getUnsignedSignaturePropertiesList(XAdESSignature signature) {
        // NOTE : only direct incorporation is supported
        XAdESUnsignedSigProperties unsignedSigProperties = XAdESUnsignedSigProperties.build(signature.getSignatureElement(), signature.getXAdESPaths());
        if (!unsignedSigProperties.isExist()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No xades:UnsignedSignatureProperties is present to compute the message-imprint for an evidence-record");
            }
            return Collections.emptyList();
        }
        if (evidenceRecordAttribute != null) {
            return getPrecedingAttributes(unsignedSigProperties, evidenceRecordAttribute);
        }
        
        if (parallelEvidenceRecord) {
            XAdESAttribute erAttribute = XAdESSignatureUtils.getLastSealingEvidenceRecordAttribute(unsignedSigProperties);
            if (erAttribute != null) {
                return getPrecedingAttributes(unsignedSigProperties, erAttribute);
            }
        }
        return unsignedSigProperties.getAttributes();
    }
    
    private List<XAdESAttribute> getPrecedingAttributes(XAdESUnsignedSigProperties unsignedSigProperties, SignatureAttribute attribute) {
        final List<XAdESAttribute> attributes = new ArrayList<>();
        for (XAdESAttribute currentAttribute : unsignedSigProperties.getAttributes()) {
            if (attribute.equals(currentAttribute)) {
                break;
            }
            attributes.add(currentAttribute);
        }
        return attributes;
    }

    private List<Node> getObjects(XAdESSignature signature) {
        NodeList objects = signature.getObjects();
        if (objects != null && objects.getLength() > 0) {
            final List<Node> result = new ArrayList<>();
            for (int i = 0; i < objects.getLength(); i++) {
                result.add(objects.item(i));
            }
            return result;
        }
        return Collections.emptyList();
    }

    private boolean containsQualifyingProperties(Node node, XAdESPath xadesPath) {
        Node qualifyingProperties = DomUtils.getNode(node, xadesPath.getCurrentQualifyingPropertiesPath());
        return qualifyingProperties != null;
    }

    private List<byte[]> getManifestDataObjectDigests(XAdESSignature signature, Reference referenceToManifest,
                                                      String canonicalizationAlgorithm) throws XMLSecurityException, IOException {
        final List<byte[]> digestObjectsGroup = new ArrayList<>();
        getManifestDataObjectDigestsRecursively(signature, referenceToManifest, canonicalizationAlgorithm, digestObjectsGroup);
        return digestObjectsGroup;
    }

    private void getManifestDataObjectDigestsRecursively(XAdESSignature signature, Reference referenceToManifest,
                                                   String canonicalizationAlgorithm, List<byte[]> digestObjectsGroup) throws XMLSecurityException, IOException {
        byte[] bytes;
        for (Reference manifestReference : getManifestReferences(signature, referenceToManifest)) {

            /*
             * a) For each ds:Reference child element of each signed ds:Manifest element retrieve the data
             *    object referenced by its URI attribute.
             */
            if (!isResultXmlNodeSet(manifestReference) || !manifestReference.typeIsReferenceToManifest()) {
                /*
                 * b) If the retrieved data object is not a XML node set, or it is a XML node set different than a
                 *    ds:Manifest element, process it as specified by the reference processing model of XMLDSIG [7],
                 *    clause 4.4.3.2. The resulting data object shall be added to the group of data objects to be digested.
                 */
                bytes = getReferenceBytesDigestValue(manifestReference, canonicalizationAlgorithm);

                digestObjectsGroup.add(bytes);

            } else {
                /*
                 * c) If the retrieved data object is a ds:Manifest element, apply the steps 6) a) to 6) c) recursively for
                 *    generating the objects to be added to the group of data objects to be digested.
                 */
                getManifestDataObjectDigestsRecursively(signature, manifestReference, canonicalizationAlgorithm, digestObjectsGroup);
            }
        }
    }

    private List<Reference> getManifestReferences(XAdESSignature signature, Reference referenceToManifest) throws XMLSecurityException {
        String uri = referenceToManifest.getURI();
        Element manifestElement = DSSXMLUtils.getManifestById(signature.getSignatureElement(), uri);
        Manifest manifest = DSSXMLUtils.initManifestWithDetachedContent(manifestElement, detachedContent);
        return DSSXMLUtils.extractReferences(manifest);
    }

    private boolean isResultXmlNodeSet(Reference reference) throws XMLSecurityException {
        return ReferenceOutputType.NODE_SET.equals(DSSXMLUtils.getReferenceOutputType(reference));
    }

    private DSSMessageDigest computeDigestValueGroupHash(List<byte[]> digestValueGroup) {
        /*
         * The algorithm by which a root hash value is generated from the
         * <HashTree> element is as follows: the content of each <DigestValue>
         * element within the first <Sequence> element is base64 ([RFC4648],
         * using the base64 alphabet not the base64url alphabet) decoded to
         * obtain a binary value (representing the hash value). All collected
         * hash values from the sequence are ordered in binary ascending order,
         * concatenated and a new hash value is generated from that string.
         * With one exception to this rule: when the first <Sequence> element
         * has only one <DigestValue> element, then its binary value is added to
         * the next list obtained from the next <Sequence> element.
         */
        // 1. Group together items
        // NOTE: byte array already contains digest only
        if (LOG.isTraceEnabled()) {
            LOG.trace("1. Digest Value Group:");
            digestValueGroup.forEach(d -> LOG.trace(Utils.toHex(d)));
        }
        // 2a. Exception
        if (Utils.collectionSize(digestValueGroup) == 1) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("2a. Only one data object: {}", digestValueGroup.get(0));
            }
            return new DSSMessageDigest(digestAlgorithm, digestValueGroup.get(0));
        }
        // 2b. Binary ascending sort
        digestValueGroup.sort(ByteArrayComparator.getInstance());
        if (LOG.isTraceEnabled()) {
            LOG.trace("2b. Sorted Digest Value Group:");
            digestValueGroup.forEach(d -> LOG.trace(Utils.toHex(d)));
        }
        // 3. Concatenate
        final DSSMessageDigestCalculator digestCalculator = new DSSMessageDigestCalculator(digestAlgorithm);
        for (byte[] hashValue : digestValueGroup) {
            digestCalculator.update(hashValue);
        }
        // 4. Calculate hash value
        DSSMessageDigest messageDigest = digestCalculator.getMessageDigest(digestAlgorithm);
        if (LOG.isTraceEnabled()) {
            LOG.trace("4. Message-digest of concatenated string: {}", messageDigest.getHexValue());
        }
        return messageDigest;
    }

}
