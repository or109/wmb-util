package com.googlecode.nodes.qsys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.as400.access.AS400;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbMessage;

public class WmbQsysWriter {
	
	private static final Log logger = LogFactory.getLog(WmbQsysWriter.class);

	private QsysWriter writer;
	
	public WmbQsysWriter(String filePath, Command command) {
		writer = new QsysWriter(filePath, command);
	}
	
	private Object wmbObjectToQsysObject(Object o) {
		if(o instanceof Float) {
			return new BigDecimal(((Float)o).doubleValue());
		} else if(o instanceof Double) {
			return new BigDecimal(((Double)o).doubleValue());
		} else {
			return o;
		}

	}
	
	public void write(AS400 sys, MbMessage message) throws Exception {
		MbElement root = message.getRootElement();
		
		MbElement wmbRecord = root.getFirstElementByPath("MRM").getFirstChild();
		//TODO Null-koll
		
		List<Record> records = new ArrayList<Record>();
		
		while(wmbRecord != null) {
			MbElement wmbField = wmbRecord.getFirstChild();
			
			List<Field> fields = new ArrayList<Field>();
			
			while(wmbField != null) {
				Object value = wmbField.getValue();
				fields.add(new Field(wmbField.getName(), wmbObjectToQsysObject(value)));
				wmbField = wmbField.getNextSibling();
			}
			
			records.add(new Record(fields));
			wmbRecord = wmbRecord.getNextSibling();
		}
		logger.debug("Found " + records.size() + " records.");
		writer.write(sys, records);
	}

}
