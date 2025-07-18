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
package eu.europa.esig.dss.service.http.commons;

import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains util methods for dealing with LDAP protocol urls
 */
public final class LdapURLUtils {

	private static final Logger LOG = LoggerFactory.getLogger(LdapURLUtils.class);
	
	/**
	 * Utils class
	 */
	private LdapURLUtils() {
		// empty
	}
	
	/**
	 * RFC 4516 Lightweight Directory Access Protocol (LDAP):
     * Uniform Resource Locator
     *                  
     * 2.1.  Percent-Encoding
     * An octet MUST be encoded using the percent-encoding mechanism
     * described in section 2.1 of [RFC3986] in any of these situations:
     * - The octet is not in the reserved set defined in section 2.2 of
     *   [RFC3986] or in the unreserved set defined in section 2.3 of [RFC3986].
     *   
     *   Must be implemented by URI provider:
     * - It is the single Reserved character '?' and occurs inside a 'dn',
     *   'filter', or other element of an LDAP URL.
     * - It is a comma character ',' that occurs inside an 'exvalue'.
	 * @param str {@link String} ldap uri to encode
	 * @return encoded {@link String} ldap uri
	 */
	public static String encode(String str) {
		
		char[] legalChars = { 
				// reserved characters
				':' , '/' , '?' , '#' , '[' , ']' , '@', // gen-delims
				'!' , '$' , '&' , '\'' , '(' , ')', // sub-delims
	            '*' , '+' , ',' , ';' , '=',
	            // unreserved characters
	            '-' , '.' , '_' , '~',
		};
		
		StringBuilder url = new StringBuilder();
		char curChar;
		boolean encoded = false;
		
		for (int i = 0; i < str.length(); i++) {
			curChar = str.charAt(i);
			if (Character.isLetter(curChar) || Character.isDigit(curChar) || contains(legalChars, curChar) || charIsEncoded(str, i)) {
				url.append(curChar);
			} else {
				url.append(getEncodedChar(curChar));
				encoded = true;
			}
		}
		if (encoded) {
			LOG.warn("The obtained ldap url [{}] contains illegal characters. Use encoded address instead : [{}]", str, url);
		}
		return url.toString();
	}
	
	private static boolean contains(char[] chars, char c) {
		for (char ch : chars) {
			if (ch == c) {
				return true;
			}
		}
		return false;
	}
	
	private static String getEncodedChar(char c) {
		String enc = Integer.toHexString(c);
		return enc.length() == 1 ? "%0" + enc : "%" + enc;
	}
	
	private static boolean charIsEncoded(String str, int i) {
		try {
			if (str.charAt(i) != '%') {
				return false;
			}
			String subString = str.substring(i, i+3);
			if (!subString.equals(DSSUtils.decodeURI(subString))) {
				return true;
			}
		} catch (Exception e) {
			LOG.trace("Cannot decode a part of string [{}] starting from position with index [{}]", str, i);
			return false;
		}
		return false;
	}

	/**
	 * Gets host name based on the given URL string.
	 * E.g. for "ldap://ldap.infonotary.com/dc=identity-ca,dc=infonotary,dc=com" returns -> "ldap.infonotary.com"
	 *
	 * @param urlString {@link String}
	 * @return {@link String} corresponding to a host name
	 */
	public static String getHost(String urlString) {
		if (Utils.isStringEmpty(urlString)) {
			return "";
		}

		int doubleslash = urlString.indexOf("//");
		if (doubleslash == -1) {
			doubleslash = 0;
		} else {
			doubleslash += 2;
		}

		int end = urlString.indexOf('/', doubleslash);
		end = end >= 0 ? end : urlString.length();

		int port = urlString.indexOf(':', doubleslash);
		end = (port > 0 && port < end) ? port : end;

		return urlString.substring(doubleslash, end);
	}

}
