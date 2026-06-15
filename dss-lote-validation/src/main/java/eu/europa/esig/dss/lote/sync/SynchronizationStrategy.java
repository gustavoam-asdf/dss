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
package eu.europa.esig.dss.lote.sync;

import eu.europa.esig.dss.model.lote.ListInfo;
import eu.europa.esig.dss.model.lote.ListOfListsInfo;

/**
 * Defines a behaviour for a trusted certificate source synchronization
 */
public interface SynchronizationStrategy {

	/**
	 * Returns true if the certificates from the list can be synchronized
	 * 
	 * @param list
	 *                    the trusted list to be verified
	 * @return true if the list can be synchronized
	 */
	boolean canBeSynchronized(ListInfo list);

	/**
	 * Returns true if the certificates from the list of lists and its
	 * list can be synchronized
	 * 
	 * @param listOfListsInfo
	 *                          the list of lists to be verified
	 * @return true if the list of lists can be synchronized
	 */
	boolean canBeSynchronized(ListOfListsInfo listOfListsInfo);

}
