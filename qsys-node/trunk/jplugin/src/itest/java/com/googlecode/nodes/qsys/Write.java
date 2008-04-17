package com.googlecode.nodes.qsys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as400.access.AS400;

public class Write {

    /**
     * arg[0] = host
     * arg[1] = user name
     * arg[2] = password
     * arg[3] = file name
     * arg[4] = command
     */
	public static void main(String[] args) throws Exception {
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("Z4FLAG", " "));
		fields.add(new Field("Z4BEHDT", new BigDecimal(9)));
		fields.add(new Field("Z4BEHKL", new BigDecimal(0)));
		fields.add(new Field("Z4EDI", "X"));
		fields.add(new Field("Z4EQNO", "TEST1"));
		fields.add(new Field("Z4EQNO", new BigDecimal(80328)));
		fields.add(new Field("Z4TOM", new BigDecimal(80328)));
		fields.add(new Field("Z4SKAPDT", new BigDecimal(80328)));
		fields.add(new Field("Z4SKAPKL", new BigDecimal(91316)));
		fields.add(new Field("Z4USER", "EDI"));
		
		Record record = new Record(fields);
		
		List<Record> records = new ArrayList<Record>();
		records.add(record);
		
		AS400 sys = new AS400(args[0], args[1], args[2]);
		
		QsysWriter writer = new QsysWriter(args[3], 
				new Command(args[4])
		);
		writer.write(sys, records);
		
		sys.disconnectAllServices();
		
	}
}
