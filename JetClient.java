import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class JetClient {

	Socket jetSocket = null;
	PrintWriter out = null; // could use Scanner class much easier
	BufferedReader in = null;

	public JetClient() {
		// TODO Auto-generated constructor stub

		// change IP
		try {
			jetSocket = new Socket("129.65.109.98", 1234);
			out = new PrintWriter(jetSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(jetSocket
					.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: Jet Socket.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: Jet Socket.");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

	}

	public static void main(String[] args) {
		new JetClient();
	}
}

// main needs new ReadIn.start();
class ReadIn extends Thread {
	JetClient jc;

	public ReadIn(JetClient jc) {
		this.jc = jc;
	}

	public void run() {
		while (true) {
			try {
				System.out.println(jc.in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}