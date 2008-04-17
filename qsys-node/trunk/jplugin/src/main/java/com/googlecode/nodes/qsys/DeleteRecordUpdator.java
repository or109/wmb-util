package com.googlecode.nodes.qsys;

import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.Record;

public class DeleteRecordUpdator implements Updator {

	public boolean update(AS400File file, Record record) throws Exception {
		file.deleteCurrentRecord();
		return true;
	}

}
