package appunti.testRoomJoiner;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import tools.LoggerMessages;

public class ConnectionTRJ {
	public static final String FUNCTIONALITY_NOT_IMPLEMENTED = "Functionality not implemented";

	public ConnectionTRJ(Socket s) {
		this.s = s;
	}

	Socket s;
	LoggerMessages log;

	//

	public LoggerMessages getLog() {
		return log;
	}

	public ConnectionTRJ setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
		return this;
	}

	//

	public String sendMessage(MessageTestRoomJoiner mess) {
		String error;
		ObjectOutputStream oos;
		error = FUNCTIONALITY_NOT_IMPLEMENTED;
		if (mess != null) {
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(mess);
				oos.flush();
				error = null;
			} catch (Exception e) {
				e.printStackTrace();
				log.log(e.toString());
			}
		}
		return error;
	}

	// getChannel().size()

	public MessageTestRoomJoiner readMessage() {
		Object o;
		MessageTestRoomJoiner mtrj;
		ObjectInputStream ois;
		InputStream is;
		mtrj = null;
		try {
			is = s.getInputStream();
			// try to read a byte. If none avaiable, return -1
			if (is.available() > 0) {
				// something into inbox
				ois = new ObjectInputStream(is);
				// now read
				o = ois.readObject();
				if (o != null && o instanceof MessageTestRoomJoiner) // ok
					mtrj = (MessageTestRoomJoiner) o;
			}
			// else log.log("No message avaiable");
		} catch (Exception e) {
			e.printStackTrace();
			log.log(e.toString());
		}
		// if (mtrj == null) log.log("CANNOT READ");
		return mtrj;
	}

	public MessageTestRoomJoiner readMessage_OLD_1() {
		int x;
		Object o;
		MessageTestRoomJoiner mtrj;
		ObjectInputStream ois;
		PushbackInputStream pis;
		mtrj = null;
		try {
			pis = new PushbackInputStream(s.getInputStream());
			// try to read a byte. If none avaiable, return -1
			if ((x = pis.read()) != -1) {
				// something into inbox
				// uread: could be the header's first byte
				pis.unread(x);
				ois = new ObjectInputStream(s.getInputStream());
				// now read
				o = ois.readObject();
				if (o != null && o instanceof MessageTestRoomJoiner) // ok
					mtrj = (MessageTestRoomJoiner) o;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.log(e.toString());
		}
		if (mtrj == null) log.log("CANNOT READ");
		return mtrj;
	}

	// delegates

	public void connect(SocketAddress endpoint) throws IOException {
		s.connect(endpoint);
	}

	public void connect(SocketAddress endpoint, int timeout) throws IOException {
		s.connect(endpoint, timeout);
	}

	public void bind(SocketAddress bindpoint) throws IOException {
		s.bind(bindpoint);
	}

	public InetAddress getInetAddress() {
		return s.getInetAddress();
	}

	public InetAddress getLocalAddress() {
		return s.getLocalAddress();
	}

	public int getPort() {
		return s.getPort();
	}

	public int getLocalPort() {
		return s.getLocalPort();
	}

	public SocketAddress getLocalSocketAddress() {
		return s.getLocalSocketAddress();
	}

	public InputStream getInputStream() throws IOException {
		return s.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return s.getOutputStream();
	}

	public boolean getTcpNoDelay() throws SocketException {
		return s.getTcpNoDelay();
	}

	public void close() throws IOException {
		s.close();
	}

	public boolean isConnected() {
		return s.isConnected();
	}

	public boolean isClosed() {
		return s.isClosed();
	}
}