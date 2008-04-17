package com.googlecode.nodes.qsys;

import java.math.BigDecimal;
import java.util.List;

import com.ibm.as400.access.AS400;

public class Read {

    /**
     * arg[0] = host
     * arg[1] = user name
     * arg[2] = password
     * arg[3] = file name
     * arg[4] = command
     */
	public static void main(String[] args) throws Exception {

	    AS400 sys = new AS400(args [0], args [1], args [2]);
	    //new FieldEqualsSelector("Z4BEHDT", new BigDecimal(7))
		QsysReader reader = new QsysReader(args[3], 
				null,
				new SingleFieldUpdator("Z4BEHDT", new BigDecimal(9)),
				new Command(args[4])
		);
		List<Record> records = reader.read(sys);
		
		System.out.println(records.size());
		
		Record record = records.get(0);
		for(int i = 0; i<record.size(); i++) {
			System.out.println(record.getField(i).getName() + " - \"" + record.getField(i).getValue() + "\" - " + record.getField(i).getValue().getClass());
		}
		
		sys.disconnectAllServices();
		
	}
}
