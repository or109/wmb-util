package com.googlecode.nodes.qsys;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

public class Command {

	private String command;
	
	public Command(String command) {
		this.command = command;
	}

	public void call(AS400 sys) throws Exception {
		// Create a command call object. This
		// program sets the command to run
		// later. It could set it here on the
		// constructor.
		CommandCall cmd = new CommandCall(sys);
		cmd.run(command);

		// Get the message list which
		// contains the result of the
		// command.
		AS400Message[] messageList = cmd.getMessageList();

		for (AS400Message message : messageList) {
			System.out.println(message.getText());
		}
	}
}
