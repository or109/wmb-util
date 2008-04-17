package com.googlecode.nodes.qsys;

import java.io.IOException;

import com.ibm.as400.access.Record;

public class MatchAllSelector implements Selector {

	public boolean match(Record record) throws IOException {
		return true;
	}
}
