package com.googlecode.nodes.qsys;

import java.io.IOException;

import com.ibm.as400.access.Record;

public interface Selector {

	boolean match(Record record) throws IOException;
}
