package com.googlecode.nodes.qsys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.as400.access.AS400;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbInputTerminal;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbNode;
import com.ibm.broker.plugin.MbNodeInterface;
import com.ibm.broker.plugin.MbOutputTerminal;

public class QsysOutputNode extends MbNode implements MbNodeInterface {

	private static final Log logger = LogFactory.getLog(QsysOutputNode.class);

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
		createInputTerminal("in");
		createOutputTerminal("out");
	}

	public void evaluate(MbMessageAssembly inAssembly,
			MbInputTerminal inTerminal) throws MbException {

		MbElement localEnv = inAssembly.getLocalEnvironment().getRootElement();

		if (this.host == null || this.host.equals("")) {
			String localEnvHost = this.getValueFromXPathExpr(
					"Variables/QsysNode/Host", localEnv);
			if (localEnvHost != null && !localEnvHost.equals("")) {
				this.host = localEnvHost;
			}
			if (host == null) {
				throw new IllegalArgumentException("host can not be null");
			}
		}
		if (this.filePath == null || this.filePath.equals("")) {
			String localEnvFilePath = this.getValueFromXPathExpr(
					"Variables/QsysNode/FilePath", localEnv);
			if (localEnvFilePath != null && !localEnvFilePath.equals("")) {
				this.filePath = localEnvFilePath;
			}
			if (this.filePath == null) {
				throw new IllegalArgumentException("filePath can not be null");
			}
		}
		if (this.command == null || this.command.equals("")) {
			String localEnvCommand = this.getValueFromXPathExpr(
					"Variables/QsysNode/Command", localEnv);
			if (localEnvCommand != null && !localEnvCommand.equals("")) {
				this.command = localEnvCommand;
			}
		}

		try {
			if (this.userName == null || this.userName.equals("")) {
				this.readUserProperties();
			}
			if (this.userName == null)
				throw new IllegalArgumentException("userName can not be null");
		} catch (FileNotFoundException e1) {
			logger.error("Could not find user properties. "
					+ "These should be placed in a "
					+ "properties file (qsys.properties) in user.home", e1);
			throw new RuntimeException("Failed writing to QSYS file", e1);
		} catch (IOException e1) {
			logger.error("Could not read user properties file", e1);
			throw new RuntimeException("Failed writing to QSYS file", e1);
		}

		AS400 sys = null;

		logger.debug("\nhost=" + host + "\nuserName=" + userName
				+ "\npassword=" + password + "\nfilePath=" + filePath
				+ "\ncommand=" + command);

		try {
			sys = new AS400(host, userName, password);

			Command as400Command = null;
			if (command != null) {
				as400Command = new Command(command);
			}
			WmbQsysWriter writer = new WmbQsysWriter(filePath, as400Command);
			try {
				writer.write(sys, inAssembly.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException("Failed writing to QSYS file", e);
			}
			MbOutputTerminal out = getOutputTerminal("out");
			out.propagate(inAssembly);
		} finally {
			if (sys != null) {
				sys.disconnectAllServices();
			}
		}
	}

	private void readUserProperties() throws FileNotFoundException, IOException {
		String path = System.getProperty("user.home") + File.separator
				+ "qsys.properties";
		Properties props = new Properties();
		props.load(new FileInputStream(path));
		this.userName = props.getProperty("userName");
		this.password = props.getProperty("password");
	}

	private String getValueFromXPathExpr(String xpath, MbElement localEnv)
			throws MbException {
		String value = "";
		List list = (List) localEnv.evaluateXPath(xpath);
		if (list != null && list.size() > 0) {
			MbElement el = (MbElement) list.get(0);
			value = (String) el.getValue();
		}
		return value;
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
