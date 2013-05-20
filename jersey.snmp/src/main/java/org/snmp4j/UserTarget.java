/*_############################################################################
  _## 
  _##  SNMP4J 2 - UserTarget.java  
  _## 
  _##  Copyright (C) 2003-2013  Frank Fock and Jochen Katz (SNMP4J.org)
  _##  
  _##  Licensed under the Apache License, Version 2.0 (the "License");
  _##  you may not use this file except in compliance with the License.
  _##  You may obtain a copy of the License at
  _##  
  _##      http://www.apache.org/licenses/LICENSE-2.0
  _##  
  _##  Unless required by applicable law or agreed to in writing, software
  _##  distributed under the License is distributed on an "AS IS" BASIS,
  _##  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  _##  See the License for the specific language governing permissions and
  _##  limitations under the License.
  _##  
  _##########################################################################*/

package org.snmp4j;

import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;

// for JavaDoc

/**
 * User based target for SNMPv3 or later.
 * 
 * @author Frank Fock
 * @version 2.0
 */
public class UserTarget extends SecureTarget {

	private static final long serialVersionUID = -1426511355567423746L;

	private OctetString authoritativeEngineID = new OctetString();

	/**
	 * Creates a target for a user based security model target.
	 */
	public UserTarget() {
		setSecurityModel(MPv3.ID);
	}

	/**
	 * Creates a SNMPv3 USM target with security level noAuthNoPriv, one second
	 * time-out without retries.
	 * 
	 * @param address
	 *            the transport <code>Address</code> of the target.
	 * @param securityName
	 *            the USM security name to be used to access the target.
	 * @param authoritativeEngineID
	 *            the authoritative engine ID as a possibly zero length byte
	 *            array which must not be <code>null</code>.
	 */
	public UserTarget(Address address, OctetString securityName, byte[] authoritativeEngineID) {
		super(address, securityName);
		setAuthoritativeEngineID(authoritativeEngineID);
		setSecurityModel(MPv3.ID);
	}

	/**
	 * Creates a SNMPv3 USM target with the supplied security level, one second
	 * time-out without retries.
	 * 
	 * @param address
	 *            the transport <code>Address</code> of the target.
	 * @param securityName
	 *            the USM security name to be used to access the target.
	 * @param authoritativeEngineID
	 *            the authoritative engine ID as a possibly zero length byte
	 *            array which must not be <code>null</code>.
	 * @param securityLevel
	 *            the {@link SecurityLevel} to use.
	 * @since 1.1
	 */
	public UserTarget(Address address, OctetString securityName, byte[] authoritativeEngineID, int securityLevel) {
		super(address, securityName);
		setAuthoritativeEngineID(authoritativeEngineID);
		setSecurityLevel(securityLevel);
		setSecurityModel(MPv3.ID);
	}

	/**
	 * Sets the authoritative engine ID of this target.
	 * 
	 * @param authoritativeEngineID
	 *            a possibly zero length byte array (must not be
	 *            <code>null</code>).
	 */
	public void setAuthoritativeEngineID(byte[] authoritativeEngineID) {
		this.authoritativeEngineID.setValue(authoritativeEngineID);
	}

	/**
	 * Gets the authoritative engine ID of this target.
	 * 
	 * @return a possibly zero length byte array.
	 */
	public byte[] getAuthoritativeEngineID() {
		return authoritativeEngineID.getValue();
	}

	@Override
	public String toString() {
		return "UserTarget[" + toStringAbstractTarget() + ", authoritativeEngineID=" + authoritativeEngineID + ']';
	}
}
