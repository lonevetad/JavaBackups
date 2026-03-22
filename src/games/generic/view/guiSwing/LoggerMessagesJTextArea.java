package games.generic.view.guiSwing;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextArea;

import tools.LoggerMessages;

public class LoggerMessagesJTextArea implements LoggerMessages {

	private static final long serialVersionUID = 6520990880808084085L;

	public LoggerMessagesJTextArea() { this(new JTextArea()); }

	public LoggerMessagesJTextArea(JTextArea jta) { this(jta, false); }

	public LoggerMessagesJTextArea(JTextArea jta, boolean rememberMessages) {
		this.jTextArea = jta;
		memoryLog = (this.rememberMessages = rememberMessages) ? new LinkedList<>() : null;
	}

	boolean rememberMessages;
	JTextArea jTextArea;
	LinkedList<String> memoryLog;

	public JTextArea getTextArea() { return jTextArea; }

	@Override
	public boolean log(String text, boolean newLineRequired) {
		if (text != null && (/* ( */text /* = text.trim()) */.length() > 0)) {
			jTextArea.append(text);
			if (newLineRequired)
				jTextArea.append(NEW_LINE);
			if (rememberMessages) { memoryLog.addLast(text); }
			return true;
		}
		return false;
	}

	@Override
	public void clearLog() {
		jTextArea.setText("");
//		if (rememberMessages) { memoryLog.clear(); }
	}

	@Override
	public List<String> getEntireLog() { return memoryLog; }

}