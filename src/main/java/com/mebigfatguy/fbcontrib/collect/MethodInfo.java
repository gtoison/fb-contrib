/*
 * fb-contrib - Auxiliary detectors for Java programs
 * Copyright (C) 2005-2019 Dave Brosius
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.mebigfatguy.fbcontrib.collect;

import org.apache.bcel.Const;

import com.mebigfatguy.fbcontrib.utils.ToString;

/**
 * represents statistics including attributes, mutability and sizes of a method
 */
public class MethodInfo {

	public static final int PUBLIC_USE = 1;
	public static final int PRIVATE_USE = 2;
	public static final int PROTECTED_USE = 4;
	public static final int PACKAGE_USE = 8;

	private short numMethodBytes;
	private byte numMethodCalls;
	private byte immutabilityOrdinal;
	private byte declaredAccess;
	private byte isCalledType;
	private boolean modifiesState;
	private boolean canReturnNull;
	private boolean isDerived;

	public int getNumBytes() {
		return 0x0000FFFF & numMethodBytes;
	}

	public void setNumBytes(int numBytes) {
		numMethodBytes = (short) numBytes;
	}

	public int getNumMethodCalls() {
		return 0x000000FF & numMethodCalls;
	}

	public void setNumMethodCalls(int numCalls) {
		numMethodCalls = numCalls > 255 ? Byte.MAX_VALUE : (byte) numCalls;
	}

	public void setDeclaredAccess(int access) {
		declaredAccess = (byte) access;
	}

	public int getDeclaredAccess() {
		return declaredAccess;
	}

	public void addCallingAccess(int access) {
		if ((access & Const.ACC_PUBLIC) != 0) {
			isCalledType |= PUBLIC_USE;
		} else if ((access & Const.ACC_PROTECTED) != 0) {
			isCalledType |= PROTECTED_USE;
		} else if ((access & Const.ACC_PRIVATE) != 0) {
			isCalledType |= PRIVATE_USE;
		} else {
			isCalledType |= PACKAGE_USE;
		}
	}

	public boolean wasCalled() {
		return (isCalledType & (PUBLIC_USE | PROTECTED_USE | PACKAGE_USE | PRIVATE_USE)) != 0;
	}

	public boolean wasCalledPublicly() {
		return (isCalledType & PUBLIC_USE) != 0;
	}

	public boolean wasCalledProtectedly() {
		return (isCalledType & PROTECTED_USE) != 0;
	}

	public boolean wasCalledPackagely() {
		return (isCalledType & PACKAGE_USE) != 0;
	}

	public boolean wasCalledPrivately() {
		return (isCalledType & PRIVATE_USE) != 0;
	}

	public ImmutabilityType getImmutabilityType() {
		return ImmutabilityType.values()[immutabilityOrdinal];
	}

	public void setImmutabilityType(ImmutabilityType imType) {
		immutabilityOrdinal = (byte) imType.ordinal();
	}

	public boolean getModifiesState() {
		return modifiesState;
	}

	public void setModifiesState(boolean modifiesState) {
		this.modifiesState = modifiesState;
	}

	public boolean getCanReturnNull() {
		return canReturnNull;
	}

	public void setCanReturnNull(boolean canReturnNull) {
		this.canReturnNull = canReturnNull;
	}

	public boolean isDerived() {
		return isDerived;
	}

	public void setDerived(boolean isDerived) {
		this.isDerived = isDerived;
	}

	public boolean isVarArg() {
		return (declaredAccess & Const.ACC_VARARGS) != 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MethodInfo)) {
			return false;
		}

		MethodInfo mi = (MethodInfo) o;

		return (numMethodBytes == mi.numMethodBytes) && (numMethodCalls == mi.numMethodCalls)
				&& (immutabilityOrdinal == mi.immutabilityOrdinal) && (declaredAccess == mi.declaredAccess)
				&& (isCalledType == mi.isCalledType) && (modifiesState == mi.modifiesState);
	}

	@Override
	public int hashCode() {
		return numMethodBytes ^ numMethodCalls ^ immutabilityOrdinal ^ declaredAccess ^ isCalledType
				^ (modifiesState ? 1 : -1);
	}

	@Override
	public String toString() {
		return ToString.build(this);
	}
}
