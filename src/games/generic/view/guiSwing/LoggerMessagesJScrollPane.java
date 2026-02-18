package games.generic.view.guiSwing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tools.LoggerMessages;

public class LoggerMessagesJScrollPane extends JScrollPane implements LoggerMessages {
	private static final long serialVersionUID = 984015618044L;
	public static final Component COLUMN_HEADER_VIEW_DEFAULT = new JLabel("Log:");

	public LoggerMessagesJScrollPane() { this(false); }

	public LoggerMessagesJScrollPane(boolean rememberMessages) { this(new JTextArea(), rememberMessages); }

	//

	protected LoggerMessagesJScrollPane(JTextArea jta, boolean rememberMessages) {
		super(jta);
		JPanel jpCMH;
		lmjta = new LoggerMessagesJTextArea(jta, rememberMessages);
		super.setViewportView(jta);
		jpCMH = new JPanel(new BorderLayout());
		jbClearLog = new JButton("Clear");
		jbClearLog.addActionListener(l -> clearLog());
		jpCMH.add(jbClearLog, BorderLayout.WEST);
		jpCMH.add(COLUMN_HEADER_VIEW_DEFAULT, BorderLayout.CENTER);
		setColumnHeaderView(jpCMH);
		// this.setColumnHeaderView(jbClearLog);
		// this.setRowHeaderView(jbClearLog);
	}

	protected LoggerMessagesJTextArea lmjta;
	protected JButton jbClearLog;

	public JTextArea getTextArea() { return lmjta.getTextArea(); }

	public LoggerMessagesJTextArea getInnerLogger() { return lmjta; }

	@Override
	public boolean log(String text, boolean newLineRequired) { return lmjta.log(text, newLineRequired); }

	@Override
	public void clearLog() { lmjta.clearLog(); }

	@Override
	public List<String> getEntireLog() { return lmjta.getEntireLog(); }
}