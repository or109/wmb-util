package com.googlecode.nodes.qsys;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

public class QsysWriter {

	private static final Log logger = LogFactory.getLog(QsysWriter.class);

	private String filePath;

	private Command command;

	public QsysWriter(String filePath, Command command) {
		if (filePath == null) {
			throw new IllegalArgumentException("filePath can not be null");
		}
		this.filePath = filePath;
		this.command = command;
	}

	public void write(AS400 sys, List<com.googlecode.nodes.qsys.Record> records)
			throws Exception {
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

			for (com.googlecode.nodes.qsys.Record record : records) {
				Record qsysRecord = new Record(format[0]);
				for (int i = 0; i < record.size(); i++) {
					qsysRecord.setField(i, record.getField(i).getValue());
				}
				file.write(qsysRecord);
			}
			file.commit();
			if (logger.isDebugEnabled())
				logger.debug("All records written to file.");

			if (command != null) {
				command.call(sys);
				if (logger.isDebugEnabled())
					logger.debug("Command ran successfully.");
			}

		} catch (Exception e) {
			if (file != null) {
				file.rollback();
			}
			throw e;
		} finally {
			// Close the file since I am done using it
			if (file != null) {
				file.close();
			}
		}
	}
}
