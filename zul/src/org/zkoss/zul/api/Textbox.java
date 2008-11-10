/* Textbox.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Oct 22 09:27:29     2008, Created by Flyworld
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
 */
package org.zkoss.zul.api;

import org.zkoss.zk.ui.WrongValueException;


/**
 * A textbox.
 * 
 * <p>
 * See <a href="package-summary.html">Specification</a>.
 * </p>
 * <p>
 * Default {@link #getZclass}: z-textbox.(since 3.5.0)
 * 
 * @author tomyeh
 */
public interface Textbox extends org.zkoss.zul.impl.api.InputElement {

	/**
	 * Returns the value. The same as {@link #getText}.
	 * <p>
	 * Default: "".
	 * 
	 * @exception WrongValueException
	 *                if user entered a wrong value
	 */
	public String getValue() throws WrongValueException;

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value; If null, it is considered as empty.
	 * @exception WrongValueException
	 *                if value is wrong
	 */
	public void setValue(String value) throws WrongValueException;

	/**
	 * Returns the type.
	 * <p>
	 * Default: text.
	 */
	public String getType();

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type. Acceptable values are "text" and "password". Unlike
	 *            XUL, "timed" is redudant because it is enabled as long as
	 *            onChanging is added.
	 */
	public void setType(String type) throws WrongValueException;

	/**
	 * Returns the rows.
	 * <p>
	 * Default: 1.
	 */
	public int getRows();

	/**
	 * Sets the rows.
	 */
	public void setRows(int rows) throws WrongValueException;

	/**
	 * Returns whether it is multiline.
	 * <p>
	 * Default: false.
	 */
	public boolean isMultiline();

	/**
	 * Sets whether it is multiline.
	 */
	public void setMultiline(boolean multiline);

	// -- super --//
	public String getZclass();
}
