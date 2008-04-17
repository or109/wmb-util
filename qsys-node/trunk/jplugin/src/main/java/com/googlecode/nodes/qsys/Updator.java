package com.googlecode.nodes.qsys;

import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.Record;

public interface Updator {

	boolean update(AS400File file, Record record) throws Exception;
}
