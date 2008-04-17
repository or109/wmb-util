package com.googlecode.nodes.qsys;

import java.io.IOException;

import com.ibm.as400.access.Record;

public class FieldEqualsSelector implements Selector {

	private String fieldName;
	private Object fieldValue;
	
	public FieldEqualsSelector(String fieldName, Object fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public boolean match(Record record) throws IOException {
		Object actual = record.getField(fieldName);
		if(actual == null) {
			return fieldValue == null;
		} else {
			return actual.equals(fieldValue);
		}
	}
}
