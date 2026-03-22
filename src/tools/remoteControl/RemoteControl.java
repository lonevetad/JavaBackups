package tools.remoteControl;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import tools.remoteControl.view.ViewRC;

/** Main launcher and works as a test */
public class RemoteControl {

	public static void main(String args[]) {
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			System.out.println("IP Address:- " + inetAddress.getHostAddress());
			System.out.println("Host Name:- " + inetAddress.getHostName());
			System.out.println("loopback address: " + InetAddress.getLoopbackAddress());
			System.out.println("CanonicalHostName: " + inetAddress.getCanonicalHostName());
			System.out.println("address as array: " + Arrays.toString(inetAddress.getAddress()));
			System.out.println("\n\n trying different way: DatagramSocket");

			try (final DatagramSocket socket = new DatagramSocket()) {
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				var ip = socket.getLocalAddress().getHostAddress();
				System.out.println("ip got:");
				System.out.println(ip);
				System.out.println("inet address:" + socket.getInetAddress());
			} catch (SocketException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		}

		ViewRC v;
		v = new ViewRC();
		v.init();
	}
}