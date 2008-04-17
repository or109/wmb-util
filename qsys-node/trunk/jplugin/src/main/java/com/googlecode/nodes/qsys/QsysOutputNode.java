package com.googlecode.nodes.qsys;

import com.ibm.as400.access.AS400;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbInputTerminal;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbNode;
import com.ibm.broker.plugin.MbNodeInterface;
import com.ibm.broker.plugin.MbOutputTerminal;

public class QsysOutputNode extends MbNode implements MbNodeInterface {

	private String host;
	private String userName;
	private String password;
	private String filePath;
	private String command;

	public static String getNodeName() {
	   return "QsysOutputNode";
	}

	
	public QsysOutputNode() throws MbException {
		// create terminals here
		createInputTerminal ("in");
		createOutputTerminal ("out");
	}

	
	
	public void evaluate(MbMessageAssembly inAssembly, MbInputTerminal inTerminal)
			throws MbException {

		if (host == null) {
			throw new IllegalArgumentException("host can not be null");
		}
		if (userName == null) {
			throw new IllegalArgumentException("userName can not be null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password can not be null");
		}

		AS400 sys = null;
		
		try {
			sys = new AS400(host, userName, password);
			
			Command as400Command = null;
			if(command != null) {
				as400Command = new Command(command);
			}
			
			WmbQsysWriter writer = new WmbQsysWriter(filePath, as400Command);
			try {
				writer.write(sys, inAssembly.getMessage());
			} catch (Exception e) {
				throw new RuntimeException("Failed writing to QSYS file", e);
			}
			
		    MbOutputTerminal out = getOutputTerminal("out");
		    out.propagate(inAssembly);
		} finally {
			if(sys != null) {
				sys.disconnectAllServices();
			}
		}
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}
}
