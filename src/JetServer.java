
import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class JetServer {

	ArrayList<JetServerThread> people = new ArrayList<JetServerThread>();
	ServerSocket serverSocket = null;

	public JetServer() throws IOException {
		// TODO Auto-generated constructor stub

		boolean listening = true;

		try {
			serverSocket = new ServerSocket(1234);
			
			 InetAddress addr = InetAddress.getLocalHost();

			    // Get IP Address
			    byte[] ipAddr = addr.getAddress();
			
			    for (int i = 0; i < ipAddr.length; i++) {
			    	System.out.print(((int)ipAddr[i] & 0xFF) + " ");
			    }
			    System.out.println();
			    
			
		} catch (IOException e) {
			System.err.println("Could not listen on port: 1234.");
			System.exit(-1);
		}

		while (listening) {
			JetServerThread temp = new JetServerThread(this, serverSocket.accept());
			people.add(temp);
			temp.start();
		}

		serverSocket.close();
	}

	public static void main(String[] args) {
		try {
			new JetServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class JetServerThread extends Thread {
	private JetServer js;
	private Socket socket = null;

	public JetServerThread(JetServer js, Socket socket) {
		super("JetServerThread");
		this.socket = socket;
		this.js = js;
	}

	public void run() {
		boolean going = true;

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			while (going) {
				System.out.println(in.readLine());
			}

			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
