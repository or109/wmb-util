package com.googlecode.nodes.qsys;

import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.Record;

public class SingleFieldUpdator implements Updator {

	private String fieldName;
	private Object updateValue;
	
	public SingleFieldUpdator(String fieldName, Object updateValue) {
		this.fieldName = fieldName;
		this.updateValue = updateValue;
	}

	public boolean update(AS400File file, Record record) throws Exception {
		record.setField(fieldName, updateValue);
		file.update(record);
		return true;
	}

}
