package com.googlecode.nodes.qsys;

import java.util.ArrayList;
import java.util.List;

public class Record {

	private List<Field> fields = new ArrayList<Field>();
	
	public Record(List<Field> fields) {
		this.fields = fields;
	}

	public String[] getFieldNames() {
		String[] names = new String[fields.size()];
		for(int i = 0; i<names.length; i++) {
			names[i] = fields.get(i).getName();
		}
		
		return names;
	}
	
	public Field getField(int i) {
		return fields.get(i);
	}
	
	public int size() {
		return fields.size();
	}
	
	public Field getField(String name) {
		for(Field field : fields) {
			if(field.getName().equals(name)) {
				return field;
			}
		}
		
		return null;
	}
}
