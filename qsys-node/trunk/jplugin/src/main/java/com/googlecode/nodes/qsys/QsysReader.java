package com.googlecode.nodes.qsys;

import java.util.ArrayList;
import java.util.List;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

public class QsysReader {

	private String filePath;
	private Selector selector;
	private Updator updator;
	private Command command;

	public QsysReader(String filePath, Selector selector, Updator updator, Command command) {
		if (filePath == null) {
			throw new IllegalArgumentException("filePath can not be null");
		}

		this.filePath = filePath;

		if (selector != null) {
			this.selector = selector;
		} else {
			this.selector = new MatchAllSelector();
		}
		
		this.updator = updator;
	}

	public List<com.googlecode.nodes.qsys.Record> read(AS400 sys) throws Exception {
		// Create an AS400 object, the file exists on this
		// server.
		SequentialFile file = null;

		try {
			

			AS400FileRecordDescription myFileDesc = new AS400FileRecordDescription(
					sys, filePath);
			// Retrieve the record format for the file
			RecordFormat[] format = myFileDesc.retrieveRecordFormat();
			// Create a file object that represents the file
			file = new SequentialFile(sys, filePath);
			file.setRecordFormat(format[0]);
			file
					.open(AS400File.READ_WRITE, 0,
							AS400File.COMMIT_LOCK_LEVEL_NONE);

			String[] fieldNames = format[0].getFieldNames();

			Record record = file.readNext();

			List<com.googlecode.nodes.qsys.Record> records = new ArrayList<com.googlecode.nodes.qsys.Record>();
			while (record != null) {
				if (selector.match(record)) {
					List<Field> fields = new ArrayList<Field>();

					int size = record.getNumberOfFields();

					for (int i = 0; i < size; i++) {
						fields
								.add(new Field(fieldNames[i], record
										.getField(i)));
					}

					records.add(new com.googlecode.nodes.qsys.Record(fields));

					if (updator != null) {
						updator.update(file, record);
					}
				}

				record = file.readNext();
			}

			if(command != null) {
				command.call(sys);
			}
			
			file.commit();

			return records;

		} catch (Exception e) {
			if (file != null) {
				file.rollback();
			}
			
			throw e;
		} finally {
			// Close the file since I am done using it
			if(file != null) {
				file.close();
			}

			// Disconnect since I am done using
			// record-level access.
		}
	}
}
