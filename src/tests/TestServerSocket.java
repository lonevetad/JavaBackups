package tests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class TestServerSocket {
	static final Object LOCK = new Object();

	public static void main(String[] args) throws IOException {
		ServerSocket server;
		AddressAndPort aap;
		Socket masterSocket;

//

		server =
//				new ServerSocket(0);
				ServerSocketFactory.getDefault().createServerSocket(0, 1, InetAddress.getLocalHost());
		aap = new AddressAndPort(server.getInetAddress().getHostAddress());
		aap.port = server.getLocalPort();

		System.out.println("server at: " + aap);

		createAndRunMaster(aap);

		masterSocket = server.accept();

		System.out.println("now start reading");
//		ObjectInputStream is;
//		is = new ObjectInputStream(masterSocket.getInputStream());
		ObjectInputStream is;
		is = new ObjectInputStream(masterSocket.getInputStream());
		while (is.available() <= 0) {
//		while (is.ready()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String text = is.readLine();
		System.out.println("read line: " + text);
//		bis.readLine()
		System.out.println("bye");

		server.close();
	}

	static void createAndRunMaster(AddressAndPort aap) {
		Thread masterThread;
		masterThread = new Thread(() -> {
			Socket masterSocket;
			ObjectOutputStream writer;
			String text;

			System.out.println("--MASTEEER");

			try {
				masterSocket = new Socket(aap.address, aap.port);

//				writer = new BufferedWriter(new OutputStreamWriter(masterSocket.getOutputStream()));
				writer = new ObjectOutputStream(masterSocket.getOutputStream());

				System.out.println("--waiting 2000ms");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				text = "sciao beloooo";
				System.out.println("--writing: " + text);
//				writer.append(text);
//				writer.newLine();
//				text.chars().forEachOrdered(c -> {
//					try {
//						writer.writeChar(c);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//				});
				writer.write(text.getBytes());
				writer.writeChar('\n');
				writer.flush();
				System.out.println("-- wrote, now closing");

				masterSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		masterThread.start();
	}

//	static AddressAndPort produceServer

	static class AddressAndPort {
		int port = 0;
		String address;

		protected AddressAndPort(String address) {
			super();
			this.address = address;
		}

		@Override
		public String toString() { return "AddressAndPort [address=" + address + ", port=" + port + "]"; }
	}
}