/*
 * Copyright 2007 (C) Callista Enterprise.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *	http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.googlecode.wmbutil.messages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.googlecode.wmbutil.NiceMbException;
import com.ibm.broker.plugin.MbDate;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbTime;
import com.ibm.broker.plugin.MbTimestamp;

public class TdsRecord extends MbElementWrapper {

	public TdsRecord(MbElement wrappedElm, boolean readOnly) throws MbException {
		super(wrappedElm, readOnly);
	}

	public Object getField(String name) throws MbException {
		MbElement elm = getMbElement().getFirstElementByPath(name);

		if (elm != null) {
			return elm.getValue();
		} else {
			throw new NiceMbException("Unknown field: " + name);
		}
	}

	public void setField(String name, Object value) throws MbException {
		checkReadOnly();

		MbElement elm = getMbElement().getFirstElementByPath(name);
		if (elm == null) {
			elm = getMbElement().createElementAsLastChild(
					MbElement.TYPE_NAME_VALUE, name, value);
		} else {
			elm.setValue(value);
		}
	}

	public String getStringField(String name) throws MbException {
		Object o = getField(name);

		if (o != null) {
			return o.toString();
		} else {
			return null;
		}
	}

	public int getIntField(String name) throws MbException {
		return ((Integer) getField(name)).intValue();
	}

	public long getLongField(String name) throws MbException {
		if (getField(name) instanceof Integer)
			return ((Integer) getField(name)).longValue();
		else
			return ((Long) getField(name)).longValue();
	}

	public float getFloatField(String name) throws MbException {
		if (getField(name) instanceof Integer)
			return ((Integer) getField(name)).floatValue();
		else
			return ((Float) getField(name)).floatValue();
	}

	public double getDoubleValue(String name) throws MbException {
		if (getField(name) instanceof Integer)
			return ((Integer) getField(name)).doubleValue();
		else
			return ((Double) getField(name)).doubleValue();
	}

	public boolean getBooleanValue(String name) throws MbException {
		return ((Boolean) getField(name)).booleanValue();
	}

	public Date getDateField(String name) throws MbException {
		if (getField(name) instanceof MbTimestamp) {
			return ((MbTimestamp) getField(name)).getTime();
		} else if (getField(name) instanceof MbDate) {
			return ((MbDate) getField(name)).getTime();
		} else {
			return ((MbTime) getField(name)).getTime();
		}
	}

	/**
	 * Get a date value based on a String field and a date pattern which is used
	 * for parsing. Useful for example when handling dates with time zones,
	 * something that WMB does not currently support.
	 * 
	 * @param name
	 *            The element whos value if to be retrieved
	 * @param datePattern
	 *            The date pattern to use for parsning, see
	 *            {@link SimpleDateFormat} for documentation of the allowed
	 *            tokens
	 * @return A date based on the String field value
	 * @throws MbException
	 */
	public Date getDateField(String name, String datePattern)
			throws MbException {
		String dateStr = getStringField(name);

		return parseDate(dateStr, datePattern);
	}

	/**
	 * Get a date value based on a String field and a date pattern which is used
	 * for parsing. Useful for example when handling dates with time zones,
	 * something that WMB does not currently support.
	 * 
	 * @param name
	 *            The element whos value if to be retrieved
	 * @param datePattern
	 *            The date pattern to use for parsning, see
	 *            {@link SimpleDateFormat} for documentation of the allowed
	 *            tokens
	 * @param fieldLength
	 *            The specified length of the field, useful when input format
	 *            uses number fields to store dates and the leading 0 is removed
	 *            in parsing
	 * @return A date based on the String field value
	 * @throws MbException
	 */
	public Date getDateField(String name, String datePattern, int fieldLength)
			throws MbException {
		StringBuffer dateStr = new StringBuffer(getStringField(name));

		while (dateStr.length() < fieldLength) {
			dateStr.insert(0, '0');
		}

		return parseDate(dateStr.toString(), datePattern);
	}

	private Date parseDate(String dateStr, String datePattern)
			throws MbException {
		if (dateStr != null) {
			DateFormat df = new SimpleDateFormat(datePattern);
			df.setLenient(false);
			try {
				return df.parse(dateStr);
			} catch (ParseException e) {
				throw new NiceMbException("Failed to parse date \"" + dateStr
						+ "\" with format \"" + datePattern + "\"");
			}
		} else {
			return null;
		}
	}

	public void setStringField(String name, String value) throws MbException {
		setField(name, value);
	}

	public void setIntField(String name, int value) throws MbException {
		setField(name, new Integer(value));
	}

	public void setLongField(String name, long value) throws MbException {
		setField(name, new Long(value));
	}

	public void setFloatField(String name, float value) throws MbException {
		setField(name, new Float(value));
	}

	public void setDoubleField(String name, double value) throws MbException {
		setField(name, new Double(value));
	}

	public void setBooleanField(String name, boolean value) throws MbException {
		setField(name, new Boolean(value));
	}

	/**
	 * Sets the date for a known element. The date is extracted out of a Date
	 * object and an MbDate object is created.
	 * 
	 * @param name
	 *            The element whos value is to be changed.
	 * @param value
	 *            A Date object that contains a specific date.
	 * @throws MbException
	 */
	public void setDateField(String name, Date value) throws MbException {
		Calendar cal = new GregorianCalendar();
		cal.setTime(value);
		MbDate mbDate = new MbDate(cal.get(Calendar.YEAR), cal
				.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		setField(name, mbDate);
	}

	/**
	 * Sets the time for a known element. The time is extracted out of a Date
	 * object and an MbTime object is created.
	 * 
	 * @param name
	 *            The element whos value is to be changed.
	 * @param value
	 *            A Date object that contains a specific time.
	 * @throws MbException
	 */
	public void setTimeField(String name, Date value) throws MbException {
		Calendar cal = new GregorianCalendar();
		cal.setTime(value);
		MbTime mbTime = new MbTime(cal.get(Calendar.HOUR), cal
				.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal
				.get(Calendar.MILLISECOND));
		setField(name, mbTime);
	}

	/**
	 * Sets the timestamp for a known element. The timestamp is extracted out of
	 * a Date object and an MbTimestamp object is created.
	 * 
	 * @param name
	 *            The element whos value is to be changed.
	 * @param value
	 *            A Date object that contains a specific timestamp.
	 * @throws MbException
	 */
	public void setTimestampField(String name, Date value) throws MbException {
		Calendar cal = new GregorianCalendar();
		cal.setTime(value);
		MbTimestamp mbTimestamp = new MbTimestamp(cal.get(Calendar.YEAR), cal
				.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal
				.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal
				.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
		setField(name, mbTimestamp);
	}

	/**
	 * Sets the timestamp for a known element. The timestamp is formated using
	 * the provided date pattern and set as a String field. Useful for example
	 * when handling dates with time zones, something that WMB does not
	 * currently support.
	 * 
	 * Note that this method can also be used for times and dates only.
	 * 
	 * @param name
	 *            The element whos value is to be changed.
	 * @param value
	 *            A Date object that contains a specific timestamp.
	 * @param datePattern
	 *            The date pattern to use for formating, see
	 *            {@link SimpleDateFormat} for documentation of the allowed
	 *            tokens
	 * @throws MbException
	 */
	public void setTimestampField(String name, Date value, String datePattern)
			throws MbException {
		DateFormat df = new SimpleDateFormat(datePattern);

		setStringField(name, df.format(value));
	}
}
