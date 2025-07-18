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
package eu.europa.esig.dss.xades.validation.timestamp;

import eu.europa.esig.dss.crl.CRLBinary;
import eu.europa.esig.dss.crl.CRLUtils;
import eu.europa.esig.dss.enumerations.ArchiveTimestampType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EvidenceRecordOrigin;
import eu.europa.esig.dss.enumerations.TimestampType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.DSSMessageDigest;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSRevocationUtils;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.SignatureProperties;
import eu.europa.esig.dss.spi.validation.analyzer.evidencerecord.EvidenceRecordAnalyzer;
import eu.europa.esig.dss.spi.validation.analyzer.evidencerecord.EvidenceRecordAnalyzerFactory;
import eu.europa.esig.dss.spi.validation.timestamp.SignatureTimestampIdentifierBuilder;
import eu.europa.esig.dss.spi.validation.timestamp.SignatureTimestampSource;
import eu.europa.esig.dss.spi.x509.CertificateRef;
import eu.europa.esig.dss.spi.x509.evidencerecord.EvidenceRecord;
import eu.europa.esig.dss.spi.x509.revocation.crl.CRLRef;
import eu.europa.esig.dss.spi.x509.revocation.ocsp.OCSPRef;
import eu.europa.esig.dss.spi.x509.revocation.ocsp.OCSPResponseBinary;
import eu.europa.esig.dss.spi.x509.tsp.TimestampInclude;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;
import eu.europa.esig.dss.spi.x509.tsp.TimestampedReference;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.xades.DSSXMLUtils;
import eu.europa.esig.dss.xades.XAdESSignatureUtils;
import eu.europa.esig.dss.xades.definition.XAdESNamespace;
import eu.europa.esig.dss.xades.definition.XAdESPath;
import eu.europa.esig.dss.xades.definition.xades132.XAdES132Element;
import eu.europa.esig.dss.xades.definition.xades141.XAdES141Element;
import eu.europa.esig.dss.xades.definition.xadesen.XAdESEvidencerecordNamespaceElement;
import eu.europa.esig.dss.xades.evidencerecord.XAdESEmbeddedEvidenceRecordHelper;
import eu.europa.esig.dss.xades.reference.XAdESReferenceValidation;
import eu.europa.esig.dss.xades.validation.XAdESAttribute;
import eu.europa.esig.dss.xades.validation.XAdESCertificateRefExtractionUtils;
import eu.europa.esig.dss.xades.validation.XAdESRevocationRefExtractionUtils;
import eu.europa.esig.dss.xades.validation.XAdESSignature;
import eu.europa.esig.dss.xades.validation.XAdESSignedDataObjectProperties;
import eu.europa.esig.dss.xades.validation.XAdESUnsignedSigProperties;
import eu.europa.esig.dss.xades.validation.scope.XAdESTimestampScopeFinder;
import eu.europa.esig.dss.xml.utils.DomUtils;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The timestamp source for a XAdES signature
 */
@SuppressWarnings("serial")
public class XAdESTimestampSource extends SignatureTimestampSource<XAdESSignature, XAdESAttribute> {

	private static final Logger LOG = LoggerFactory.getLogger(XAdESTimestampSource.class);

	/** The signature element */
	private final transient Element signatureElement;

	/** XAdES XPaths to use */
	private final XAdESPath xadesPaths;

	/** Map between time-stamp tokens and corresponding XAdES attributes */
	private final Map<TimestampToken, XAdESAttribute> timestampAttributeMap = new HashMap<>();

	/**
	 * Default constructor
	 *
	 * @param signature {@link XAdESSignature}
	 */
	public XAdESTimestampSource(final XAdESSignature signature) {
		super(signature);
		this.signatureElement = signature.getSignatureElement();
		this.xadesPaths = signature.getXAdESPaths();
	}

	@Override
	protected SignatureProperties<XAdESAttribute> buildSignedSignatureProperties() {
		return XAdESSignedDataObjectProperties.build(signatureElement, xadesPaths);
	}

	@Override
	protected SignatureProperties<XAdESAttribute> buildUnsignedSignatureProperties() {
		return XAdESUnsignedSigProperties.build(signatureElement, xadesPaths);
	}

	@Override
	protected XAdESTimestampMessageDigestBuilder getTimestampMessageImprintDigestBuilder(DigestAlgorithm digestAlgorithm) {
		return new XAdESTimestampMessageDigestBuilder(signature, digestAlgorithm);
	}

	@Override
	protected XAdESTimestampMessageDigestBuilder getTimestampMessageImprintDigestBuilder(TimestampToken timestampToken) {
		return new XAdESTimestampMessageDigestBuilder(signature, timestampToken)
				.setTimestampAttribute(timestampAttributeMap.get(timestampToken));
	}
	
	/**
	 * Returns message-imprint digest for a SignatureTimestamp
	 *
	 * @param digestAlgorithm
	 *              {@link DigestAlgorithm} to be used for message-digest computation
	 * @param canonicalizationMethod
	 *              {@link String} canonicalization method to use
	 * @return {@link DSSMessageDigest}
	 */
	public DSSMessageDigest getSignatureTimestampMessageDigest(DigestAlgorithm digestAlgorithm, String canonicalizationMethod) {
		XAdESTimestampMessageDigestBuilder builder = getTimestampMessageImprintDigestBuilder(digestAlgorithm)
				.setCanonicalizationAlgorithm(canonicalizationMethod);
		return builder.getSignatureTimestampMessageDigest();
	}
	
	/**
	 * Returns message-imprint digest for a SigAndRefsTimestamp
	 *
	 * @param digestAlgorithm
	 *              {@link DigestAlgorithm} to be used for message-digest computation
	 * @param canonicalizationMethod
	 *              {@link String} canonicalization method to use
	 * @param en319132
	 *              defines if the timestamp shall be created accordingly to ETSI EN 319 132-1 (SigAndRefsTimestampV2)
	 * @return {@link DSSMessageDigest}
	 */
	public DSSMessageDigest getTimestampX1MessageDigest(DigestAlgorithm digestAlgorithm, String canonicalizationMethod, boolean en319132) {
		XAdESTimestampMessageDigestBuilder builder = getTimestampMessageImprintDigestBuilder(digestAlgorithm)
				.setCanonicalizationAlgorithm(canonicalizationMethod)
				.setEn319132(en319132);
		return builder.getTimestampX1MessageDigest();
	}

	/**
	 * Returns message-imprint digest for a RefsOnlyTimestamp
	 *
	 * @param digestAlgorithm
	 *              {@link DigestAlgorithm} to be used for message-digest computation
	 * @param canonicalizationMethod
	 *              {@link String} canonicalization method to use
	 * @param en319132
	 *              defines if the timestamp shall be created accordingly to ETSI EN 319 132-1 (RefsOnlyTimestampV2)
	 * @return {@link DSSMessageDigest}
	 */
	public DSSMessageDigest getTimestampX2MessageDigest(DigestAlgorithm digestAlgorithm, String canonicalizationMethod, boolean en319132) {
		XAdESTimestampMessageDigestBuilder builder = getTimestampMessageImprintDigestBuilder(digestAlgorithm)
				.setCanonicalizationAlgorithm(canonicalizationMethod)
				.setEn319132(en319132);
		return builder.getTimestampX2MessageDigest();
	}
	
	/**
	 * Returns message-imprint digest for an ArchiveTimeStamp
	 *
	 * @param digestAlgorithm
	 *              {@link DigestAlgorithm} to be used for message-digest computation
	 * @param canonicalizationMethod
	 *              {@link String} canonicalization method to use
	 * @return {@link DSSMessageDigest}
	 */
	public DSSMessageDigest getArchiveTimestampData(DigestAlgorithm digestAlgorithm, String canonicalizationMethod) {
		XAdESTimestampMessageDigestBuilder builder = getTimestampMessageImprintDigestBuilder(digestAlgorithm)
				.setCanonicalizationAlgorithm(canonicalizationMethod);
		return builder.getArchiveTimestampMessageDigest();
	}

	@Override
	protected boolean isContentTimestamp(XAdESAttribute signedAttribute) {
		// Not applicable for XAdES
		return false;
	}

	@Override
	protected boolean isAllDataObjectsTimestamp(XAdESAttribute signedAttribute) {
		return XAdES132Element.ALL_DATA_OBJECTS_TIMESTAMP.isSameTagName(signedAttribute.getName());
	}

	@Override
	protected boolean isIndividualDataObjectsTimestamp(XAdESAttribute signedAttribute) {
		return XAdES132Element.INDIVIDUAL_DATA_OBJECTS_TIMESTAMP.isSameTagName(signedAttribute.getName());
	}

	@Override
	protected boolean isSignatureTimestamp(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.SIGNATURE_TIMESTAMP.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isCompleteCertificateRef(XAdESAttribute unsignedAttribute) {
		String localName = unsignedAttribute.getName();
		return XAdES132Element.COMPLETE_CERTIFICATE_REFS.isSameTagName(localName) || XAdES141Element.COMPLETE_CERTIFICATE_REFS_V2.isSameTagName(localName);
	}

	@Override
	protected boolean isAttributeCertificateRef(XAdESAttribute unsignedAttribute) {
		String localName = unsignedAttribute.getName();
		return XAdES132Element.ATTRIBUTE_CERTIFICATE_REFS.isSameTagName(localName) || XAdES141Element.ATTRIBUTE_CERTIFICATE_REFS_V2.isSameTagName(localName);
	}

	@Override
	protected boolean isCompleteRevocationRef(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.COMPLETE_REVOCATION_REFS.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isAttributeRevocationRef(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.ATTRIBUTE_REVOCATION_REFS.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isRefsOnlyTimestamp(XAdESAttribute unsignedAttribute) {
		String localName = unsignedAttribute.getName();
		return XAdES132Element.REFS_ONLY_TIMESTAMP.isSameTagName(localName) || XAdES141Element.REFS_ONLY_TIMESTAMP_V2.isSameTagName(localName);
	}

	@Override
	protected boolean isSigAndRefsTimestamp(XAdESAttribute unsignedAttribute) {
		String localName = unsignedAttribute.getName();
		return XAdES132Element.SIG_AND_REFS_TIMESTAMP.isSameTagName(localName) || XAdES141Element.SIG_AND_REFS_TIMESTAMP_V2.isSameTagName(localName);
	}

	@Override
	protected boolean isCertificateValues(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.CERTIFICATE_VALUES.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isRevocationValues(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.REVOCATION_VALUES.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isAttrAuthoritiesCertValues(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.ATTR_AUTHORITIES_CERT_VALUES.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isAttributeRevocationValues(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.ATTRIBUTE_REVOCATION_VALUES.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isArchiveTimestamp(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.ARCHIVE_TIMESTAMP.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isTimeStampValidationData(XAdESAttribute unsignedAttribute) {
		return XAdES141Element.TIMESTAMP_VALIDATION_DATA.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isAnyValidationData(XAdESAttribute unsignedAttribute) {
		return XAdES141Element.ANY_VALIDATION_DATA.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isValidationDataReferences(XAdESAttribute unsignedAttribute) {
		// not supported
		return false;
	}

	@Override
	protected boolean isCounterSignature(XAdESAttribute unsignedAttribute) {
		return XAdES132Element.COUNTER_SIGNATURE.isSameTagName(unsignedAttribute.getName());
	}
	
	@Override
	protected boolean isSignaturePolicyStore(XAdESAttribute unsignedAttribute) {
		return XAdES141Element.SIGNATURE_POLICY_STORE.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected boolean isEvidenceRecord(XAdESAttribute unsignedAttribute) {
		return XAdESEvidencerecordNamespaceElement.SEALING_EVIDENCE_RECORDS.isSameTagName(unsignedAttribute.getName());
	}

	@Override
	protected List<TimestampToken> makeTimestampTokens(XAdESAttribute signatureAttribute, TimestampType timestampType,
			List<TimestampedReference> references) {
		final NodeList encapsulatedTimestamps = signatureAttribute
				.getNodeList(xadesPaths.getCurrentEncapsulatedTimestamp());
		if (encapsulatedTimestamps == null || encapsulatedTimestamps.getLength() == 0) {
			LOG.warn("The timestamp(s) {} cannot be extracted from the signature!", timestampType.name());
			return Collections.emptyList();
		}

		/*
		 * 6.3 Requirements on XAdES signature's elements, qualifying properties and
		 * services n) Requirement for SignatureTimeStamp. Each SignatureTimeStamp
		 * element shall contain only one electronic time-stamp.
		 */
		if (isSignatureTimestamp(signatureAttribute) && encapsulatedTimestamps.getLength() > 1) {
			LOG.warn("Only one EncapsulatedTimeStamp is allowed in '{}' element!", signatureAttribute.getName());
		}

		final List<TimestampToken> result = new ArrayList<>();
		for (int ii = 0; ii < encapsulatedTimestamps.getLength(); ii++) {
			final Element encapsulatedTimeStamp = (Element) encapsulatedTimestamps.item(ii);
			TimestampToken timestampToken = createTimestampToken(encapsulatedTimeStamp, timestampType, references, signatureAttribute, ii);
			if (timestampToken != null) {
				timestampToken.setCanonicalizationMethod(signatureAttribute.getTimestampCanonicalizationMethod());
				timestampToken.setTimestampIncludes(signatureAttribute.getTimestampIncludedReferences());
				if (TimestampType.INDIVIDUAL_DATA_OBJECTS_TIMESTAMP.equals(timestampType)) {
					addReferences(timestampToken.getTimestampedReferences(), getIndividualDataContentTimestampReferences(signatureAttribute.getTimestampIncludedReferences()));
				}
				timestampAttributeMap.put(timestampToken, signatureAttribute);
				result.add(timestampToken);
			}
		}
		return result;
	}

	private TimestampToken createTimestampToken(final Element encapsulatedTimeStamp, TimestampType timestampType,
			List<TimestampedReference> references, XAdESAttribute signatureAttribute, Integer orderWithinAttribute) {
		try {
			String base64EncodedTimestamp = encapsulatedTimeStamp.getTextContent();
			byte[] binaries = Utils.fromBase64(base64EncodedTimestamp);
			final SignatureTimestampIdentifierBuilder identifierBuilder = new SignatureTimestampIdentifierBuilder(binaries)
					.setSignature(signature)
					.setAttribute(signatureAttribute)
					.setOrderOfAttribute(getAttributeOrder(signatureAttribute))
					.setOrderWithinAttribute(orderWithinAttribute);
			return new TimestampToken(binaries, timestampType, references, identifierBuilder);

		} catch (Exception e) {
			if (LOG.isDebugEnabled()) {
				LOG.warn("Unable to build timestamp token from binaries '{}'! Reason : {}",
						encapsulatedTimeStamp.getTextContent(), e.getMessage(), e);
			} else {
				LOG.warn("Unable to build timestamp token! Reason : {}", e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	protected List<EvidenceRecord> makeEvidenceRecords(XAdESAttribute signatureAttribute, List<TimestampedReference> references) {
		Element element = signatureAttribute.getElement();
		if (element == null || element.getChildNodes().getLength() == 0) {
			LOG.warn("The element containing evidence record(s) is empty!");
			return Collections.emptyList();
		}

		final List<EvidenceRecord> result = new ArrayList<>();
		for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
			final Element encapsulatedEvidenceRecord = (Element) element.getChildNodes().item(ii);
			EvidenceRecord evidenceRecord = createEvidenceRecord(signatureAttribute, encapsulatedEvidenceRecord, ii);
			if (evidenceRecord != null) {
				result.add(evidenceRecord);
			}
		}
		return result;
	}

	private EvidenceRecord createEvidenceRecord(XAdESAttribute signatureAttribute, Element encapsulatedEvidenceRecord, int orderWithinAttribute) {
		try {
			DSSDocument erDocument = getEvidenceRecordDocument(encapsulatedEvidenceRecord);
			EvidenceRecordAnalyzer evidenceRecordAnalyzer = EvidenceRecordAnalyzerFactory.fromDocument(erDocument);
			evidenceRecordAnalyzer.setEvidenceRecordOrigin(EvidenceRecordOrigin.SIGNATURE);

			final XAdESEmbeddedEvidenceRecordHelper embeddedEvidenceRecordHelper = new XAdESEmbeddedEvidenceRecordHelper(signature, signatureAttribute);
			embeddedEvidenceRecordHelper.setDetachedContents(signature.getDetachedContents());
			embeddedEvidenceRecordHelper.setOrderOfAttribute(getAttributeOrder(signatureAttribute));
			embeddedEvidenceRecordHelper.setOrderWithinAttribute(orderWithinAttribute);
			evidenceRecordAnalyzer.setEmbeddedEvidenceRecordHelper(embeddedEvidenceRecordHelper);

			return evidenceRecordAnalyzer.getEvidenceRecord();

		} catch (Exception e) {
			LOG.warn("Unable to build an embedded evidence record. Reason : {}", e.getMessage(), e);
			return null;
		}
	}

	private DSSDocument getEvidenceRecordDocument(Element encapsulatedEvidenceRecord) {
		if (XAdESEvidencerecordNamespaceElement.EVIDENCE_RECORD.isSameTagName(encapsulatedEvidenceRecord.getLocalName())) {
			return new InMemoryDocument(DomUtils.serializeNode(encapsulatedEvidenceRecord));

		} else if (XAdESEvidencerecordNamespaceElement.ASN1_EVIDENCE_RECORD.isSameTagName(encapsulatedEvidenceRecord.getLocalName())) {
			String base64EncodedEvidenceRecord = encapsulatedEvidenceRecord.getTextContent();
			if (Utils.isBase64Encoded(base64EncodedEvidenceRecord)) {
				return new InMemoryDocument(Utils.fromBase64(base64EncodedEvidenceRecord));
			}
		}
		throw new UnsupportedOperationException(String.format("The provided format of an evidence record within " +
				"the '%s' element is not supported.", encapsulatedEvidenceRecord.getLocalName()));
	}

	@Override
	protected List<SignatureScope> getTimestampScopes(TimestampToken timestampToken) {
		XAdESTimestampScopeFinder timestampScopeFinder = new XAdESTimestampScopeFinder();
		timestampScopeFinder.setSignature(signature);
		return timestampScopeFinder.findTimestampScope(timestampToken);
	}

	private List<TimestampedReference> getIndividualDataContentTimestampReferences(List<TimestampInclude> timestampIncludes) {
		List<SignatureScope> result = new ArrayList<>();
		List<SignatureScope> signatureScopes = signature.getSignatureScopes();
		if (Utils.isCollectionNotEmpty(signatureScopes)) {
			for (ReferenceValidation referenceValidation : signature.getReferenceValidations()) {
				XAdESReferenceValidation xadesReferenceValidation = (XAdESReferenceValidation) referenceValidation;
				if (isContentTimestampedReference(xadesReferenceValidation, timestampIncludes)) {
					for (SignatureScope signatureScope : signatureScopes) {
						if (Utils.endsWithIgnoreCase(xadesReferenceValidation.getUri(), signatureScope.getDocumentName())) {
							result.add(signatureScope);
						}
					}
				}
			}
		}
		return getSignerDataTimestampedReferences(result);
	}

	private boolean isContentTimestampedReference(XAdESReferenceValidation xadesReferenceValidation, List<TimestampInclude> includes) {
		if (xadesReferenceValidation.getId() != null) {
			for (TimestampInclude timestampInclude : includes) {
				if (xadesReferenceValidation.getId().equals(timestampInclude.getURI())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected TimestampToken makeTimestampToken(XAdESAttribute signatureAttribute, TimestampType timestampType,
			List<TimestampedReference> references) {
		throw new UnsupportedOperationException("XAdESTimeStampType element can contain more than one timestamp");
	}
	
	@Override
	protected List<TimestampedReference> getArchiveTimestampReferences(List<TimestampToken> previousTimestamps) {
		List<TimestampedReference> timestampedReferences = super.getArchiveTimestampReferences(previousTimestamps);
		addReferences(timestampedReferences, getKeyInfoReferences());
		return timestampedReferences;
	}
	
	@Override
	protected List<TimestampedReference> getSignatureTimestampReferences() {
		List<TimestampedReference> timestampedReferences = super.getSignatureTimestampReferences();
		if (XAdESSignatureUtils.isKeyInfoCovered(signature)) {
			addReferences(timestampedReferences, getKeyInfoReferences());
		}
		return timestampedReferences;
	}

	@Override
	protected List<CertificateRef> getCertificateRefs(XAdESAttribute unsignedAttribute) {
		List<CertificateRef> certRefs = new ArrayList<>();
		boolean certificateRefV1 = isCertificateRefV1(unsignedAttribute);

		NodeList certRefsNodeList = null;
		if (certificateRefV1) {
			String currentCertRefsCertChildrenPath = xadesPaths.getCurrentCertRefsCertChildren();
			if (Utils.isStringNotEmpty(currentCertRefsCertChildrenPath)) {
				certRefsNodeList = unsignedAttribute.getNodeList(currentCertRefsCertChildrenPath);
			}
		} else {
			String currentCertRefs141CertChildrenPath = xadesPaths.getCurrentCertRefs141CertChildren();
			if (Utils.isStringNotEmpty(currentCertRefs141CertChildrenPath)) {
				certRefsNodeList = unsignedAttribute.getNodeList(currentCertRefs141CertChildrenPath);
			}
		}

		if (certRefsNodeList != null) {
			for (int ii = 0; ii < certRefsNodeList.getLength(); ii++) {
				Element certRefElement = (Element) certRefsNodeList.item(ii);
				CertificateRef certificateRef;
				if (certificateRefV1) {
					certificateRef = XAdESCertificateRefExtractionUtils.createCertificateRefFromV1(certRefElement, xadesPaths);
				} else {
					certificateRef = XAdESCertificateRefExtractionUtils.createCertificateRefFromV2(certRefElement, xadesPaths);
				}
				if (certificateRef != null) {
					certRefs.add(certificateRef);
				}
			}
		}
		return certRefs;
	}

	private boolean isCertificateRefV1(XAdESAttribute unsignedAttribute) {
		String localName = unsignedAttribute.getName();
		return XAdES132Element.ATTRIBUTE_CERTIFICATE_REFS.isSameTagName(localName) || XAdES132Element.COMPLETE_CERTIFICATE_REFS.isSameTagName(localName);
	}

	@Override
	protected List<CRLRef> getCRLRefs(XAdESAttribute unsignedAttribute) {
		List<CRLRef> crlRefs = new ArrayList<>();
		NodeList nodeList = unsignedAttribute.getNodeList(xadesPaths.getCurrentCRLRefsChildren());
		for (int ii = 0; ii < nodeList.getLength(); ii++) {
			Element element = (Element) nodeList.item(ii);
			CRLRef crlRef = XAdESRevocationRefExtractionUtils.createCRLRef(xadesPaths, element);
			if (crlRef != null) {
				crlRefs.add(crlRef);
			}
		}
		return crlRefs;
	}

	@Override
	protected List<OCSPRef> getOCSPRefs(XAdESAttribute unsignedAttribute) {
		List<OCSPRef> ocspRefs = new ArrayList<>();
		NodeList nodeList = unsignedAttribute.getNodeList(xadesPaths.getCurrentOCSPRefsChildren());
		for (int ii = 0; ii < nodeList.getLength(); ii++) {
			Element element = (Element) nodeList.item(ii);
			OCSPRef ocspRef = XAdESRevocationRefExtractionUtils.createOCSPRef(xadesPaths, element);
			if (ocspRef != null) {
				ocspRefs.add(ocspRef);
			}
		}
		return ocspRefs;
	}

	@Override
	protected List<Identifier> getEncapsulatedCertificateIdentifiers(XAdESAttribute unsignedAttribute) {
		List<Identifier> certificateIdentifiers = new ArrayList<>();
		String xPathString = isTimeStampValidationData(unsignedAttribute) || isAnyValidationData(unsignedAttribute) ?
				xadesPaths.getCurrentCertificateValuesEncapsulatedCertificate() : xadesPaths.getCurrentEncapsulatedCertificate();
		NodeList encapsulatedNodes = unsignedAttribute.getNodeList(xPathString);
		for (int ii = 0; ii < encapsulatedNodes.getLength(); ii++) {
			try {
				Element element = (Element) encapsulatedNodes.item(ii);
				byte[] binaries = getEncapsulatedTokenBinaries(element);
				CertificateToken certificateToken = DSSUtils.loadCertificate(binaries);
				certificateIdentifiers.add(certificateToken.getDSSId());
			} catch (Exception e) {
				String errorMessage = "Unable to parse an encapsulated certificate : {}";
				if (LOG.isDebugEnabled()) {
					LOG.warn(errorMessage, e.getMessage(), e);
				} else {
					LOG.warn(errorMessage, e.getMessage());
				}
			}
		}
		return certificateIdentifiers;
	}

	@Override
	protected List<CRLBinary> getEncapsulatedCRLIdentifiers(XAdESAttribute unsignedAttribute) {
		List<CRLBinary> crlIdentifiers = new ArrayList<>();
		String xPathString = isTimeStampValidationData(unsignedAttribute) || isAnyValidationData(unsignedAttribute) ?
				xadesPaths.getCurrentRevocationValuesEncapsulatedCRLValue() : xadesPaths.getCurrentEncapsulatedCRLValue();
		NodeList encapsulatedNodes = unsignedAttribute.getNodeList(xPathString);
		for (int ii = 0; ii < encapsulatedNodes.getLength(); ii++) {
			try {
				Element element = (Element) encapsulatedNodes.item(ii);
				byte[] binaries = getEncapsulatedTokenBinaries(element);
				crlIdentifiers.add(CRLUtils.buildCRLBinary(binaries));
			} catch (Exception e) {
				String errorMessage = "Unable to parse CRL binaries : {}";
				if (LOG.isDebugEnabled()) {
					LOG.warn(errorMessage, e.getMessage(), e);
				} else {
					LOG.warn(errorMessage, e.getMessage());
				}
			}
		}
		return crlIdentifiers;
	}

	@Override
	protected List<OCSPResponseBinary> getEncapsulatedOCSPIdentifiers(XAdESAttribute unsignedAttribute) {
		List<OCSPResponseBinary> ocspIdentifiers = new ArrayList<>();
		String xPathString = isTimeStampValidationData(unsignedAttribute) || isAnyValidationData(unsignedAttribute) ?
				xadesPaths.getCurrentRevocationValuesEncapsulatedOCSPValue() : xadesPaths.getCurrentEncapsulatedOCSPValue();
		NodeList encapsulatedNodes = unsignedAttribute.getNodeList(xPathString);
		for (int ii = 0; ii < encapsulatedNodes.getLength(); ii++) {
			try {
				Element element = (Element) encapsulatedNodes.item(ii);
				byte[] binaries = getEncapsulatedTokenBinaries(element);
				BasicOCSPResp basicOCSPResp = DSSRevocationUtils.loadOCSPFromBinaries(binaries);
				ocspIdentifiers.add(OCSPResponseBinary.build(basicOCSPResp));
			} catch (Exception e) {
				String errorMessage = "Unable to parse OCSP response binaries : {}";
				if (LOG.isDebugEnabled()) {
					LOG.warn(errorMessage, e.getMessage(), e);
				} else {
					LOG.warn(errorMessage, e.getMessage());
				}
			}
		}
		return ocspIdentifiers;
	}
	
	/**
	 * Returns encapsulated byte array from the given {@code encapsulatedElement}
	 *
	 * @param encapsulatedElement {@link Element} to get binaries from
	 * @return byte array
	 */
	private byte[] getEncapsulatedTokenBinaries(Element encapsulatedElement) {
		if (encapsulatedElement.hasChildNodes()) {
			Node firstChild = encapsulatedElement.getFirstChild();
			if (Node.TEXT_NODE == firstChild.getNodeType()) {
				String base64String = firstChild.getTextContent();
				if (Utils.isBase64Encoded(base64String)) {
					return Utils.fromBase64(base64String);
				}
			}
		}
		throw new DSSException(String.format("Cannot create the token reference. "
				+ "The element with local name [%s] must contain an encapsulated base64 token value! "
				+ "The found value is not a text node!", encapsulatedElement.getLocalName()));
	}

	@Override
	protected ArchiveTimestampType getArchiveTimestampType(XAdESAttribute unsignedAttribute) {
		if (XAdESNamespace.XADES_141.isSameUri(unsignedAttribute.getNamespace())) {
			return ArchiveTimestampType.XAdES_141;
		}
		return ArchiveTimestampType.XAdES;
	}

	@Override
	protected List<AdvancedSignature> getCounterSignatures(XAdESAttribute unsignedAttribute) {
		XAdESSignature counterSignature = DSSXMLUtils.createCounterSignature(unsignedAttribute.getElement(), signature);
		if (counterSignature != null) {
			return Collections.singletonList(counterSignature);
		}
		return Collections.emptyList();
	}

}
