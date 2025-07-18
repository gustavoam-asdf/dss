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
package eu.europa.esig.trustedlist;

import eu.europa.esig.trustedlist.jaxb.mra.MutualRecognitionAgreementInformationType;
import eu.europa.esig.trustedlist.jaxb.tsl.AdditionalInformationType;
import eu.europa.esig.trustedlist.jaxb.tsl.AnyType;
import eu.europa.esig.trustedlist.jaxb.tsl.OtherTSLPointerType;
import eu.europa.esig.trustedlist.jaxb.tsl.OtherTSLPointersType;
import eu.europa.esig.trustedlist.jaxb.tsl.TrustStatusListType;
import jakarta.xml.bind.MarshalException;
import jakarta.xml.bind.UnmarshalException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrustedListFacadeTest {

	@Test
	void testTL() throws JAXBException, XMLStreamException, IOException, SAXException {
		marshallUnmarshall(new File("src/test/resources/tl.xml"));
	}

	@Test
	void testTLv5() throws JAXBException, XMLStreamException, IOException, SAXException {
		marshallUnmarshall(new File("src/test/resources/tlv5.xml"));
	}

	@Test
	void testTLv6() throws JAXBException, XMLStreamException, IOException, SAXException {
		marshallUnmarshall(new File("src/test/resources/tlv6.xml"));
	}

	@Test
	void testTLv6CustomAttribute() throws JAXBException, XMLStreamException, IOException, SAXException {
		File file = new File("src/test/resources/tlv6_custom_attribute.xml");

		TrustedListFacade facade = TrustedListFacade.newFacade();

		JAXBException exception = assertThrows(UnmarshalException.class, () -> facade.unmarshall(file));
		assertTrue(exception.getLinkedException().getMessage().contains("StreetAddress"));

		TrustStatusListType trustStatusListType = facade.unmarshall(file, false);
		assertNotNull(trustStatusListType);

		String marshall = facade.marshall(trustStatusListType);
		assertNotNull(marshall);
	}

	@Test
	void testTLv6WrongInside() throws JAXBException, XMLStreamException, IOException, SAXException {
		File file = new File("src/test/resources/tlv6_wrong_inside.xml");

		TrustedListFacade facade = TrustedListFacade.newFacade();

		JAXBException exception = assertThrows(UnmarshalException.class, () -> facade.unmarshall(file));
		assertTrue(exception.getLinkedException().getMessage().contains("hello"));

		TrustStatusListType trustStatusListType = facade.unmarshall(file, false);
		assertNotNull(trustStatusListType);

		exception = assertThrows(MarshalException.class, () -> facade.marshall(trustStatusListType));
		assertTrue(exception.getLinkedException().getMessage().contains("SchemeInformation"));

		String marshall = facade.marshall(trustStatusListType, false);
		assertNotNull(marshall);
	}

	@Test
	void testTLv6Empty() throws JAXBException, XMLStreamException, IOException, SAXException {
		File file = new File("src/test/resources/tlv6_empty.xml");

		TrustedListFacade facade = TrustedListFacade.newFacade();

		JAXBException exception = assertThrows(UnmarshalException.class, () -> facade.unmarshall(file));
		assertTrue(exception.getLinkedException().getMessage().contains("TrustServiceStatusList"));

		TrustStatusListType trustStatusListType = facade.unmarshall(file, false);
		assertNotNull(trustStatusListType);

		exception = assertThrows(MarshalException.class, () -> facade.marshall(trustStatusListType));
		assertTrue(exception.getLinkedException().getMessage().contains("SchemeInformation"));

		String marshall = facade.marshall(trustStatusListType, false);
		assertNotNull(marshall);
	}

	@Test
	void testHelloWorld() {
		File file = new File("src/test/resources/hello_world.xml");

		TrustedListFacade facade = TrustedListFacade.newFacade();

		JAXBException exception = assertThrows(UnmarshalException.class, () -> facade.unmarshall(file));
		assertTrue(exception.getLinkedException().getMessage().contains("hello"));

		exception = assertThrows(UnmarshalException.class, () -> facade.unmarshall(file, false));
		assertTrue(exception.getLinkedException().getMessage().contains("hello"));
	}

	@Test
	void testLOTL() throws JAXBException, XMLStreamException, IOException, SAXException {
		marshallUnmarshall(new File("src/test/resources/lotl.xml"));
	}

	private void marshallUnmarshall(File file) throws JAXBException, XMLStreamException, IOException, SAXException {
		TrustedListFacade facade = TrustedListFacade.newFacade();

		TrustStatusListType trustStatusListType = facade.unmarshall(file);
		assertNotNull(trustStatusListType);

		trustStatusListType = facade.unmarshall(file, false);
		assertNotNull(trustStatusListType);

		trustStatusListType = facade.unmarshall(file, true);
		assertNotNull(trustStatusListType);

		String marshall = facade.marshall(trustStatusListType);
		assertNotNull(marshall);

		marshall = facade.marshall(trustStatusListType, false);
		assertNotNull(marshall);

		marshall = facade.marshall(trustStatusListType, true);
		assertNotNull(marshall);
	}

	@Test
	@SuppressWarnings("unchecked")
	void testMRA_LOTL_extract_withTrustedListFacade() throws JAXBException, XMLStreamException, IOException, SAXException {
		TrustedListFacade facade = TrustedListFacade.newFacade();

		TrustStatusListType trustStatusListType = facade
				.unmarshall(new File("src/test/resources/mra/mra-lotl.xml"));
		assertNotNull(trustStatusListType);

		OtherTSLPointersType pointersToOtherTSL = trustStatusListType.getSchemeInformation().getPointersToOtherTSL();
		assertEquals(44, pointersToOtherTSL.getOtherTSLPointer().size());

		OtherTSLPointerType tcTL = pointersToOtherTSL.getOtherTSLPointer().get(pointersToOtherTSL.getOtherTSLPointer().size() - 1);

		AdditionalInformationType additionalInformation = tcTL.getAdditionalInformation();
		List<Serializable> textualInformationOrOtherInformation = additionalInformation
				.getTextualInformationOrOtherInformation();

		MutualRecognitionAgreementInformationType mraContent = null;

		Serializable serializable = textualInformationOrOtherInformation.get(5);
		if (serializable instanceof AnyType) {
			AnyType anyType = (AnyType) serializable;
			for (Object content : anyType.getContent()) {
				if (content instanceof JAXBElement) {
					JAXBElement<MutualRecognitionAgreementInformationType> jaxbElement = (JAXBElement<MutualRecognitionAgreementInformationType>) content;
					mraContent = jaxbElement.getValue();
				}
			}
		}
		assertNull(mraContent);
	}

}
