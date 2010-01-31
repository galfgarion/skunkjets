import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class JetServer {

	ArrayList<JetClientToServerThread> people = new ArrayList<JetClientToServerThread>();
	ServerSocket serverSocket = null;
	int port;

	public JetServer(int port) throws IOException {
		this.port = port;
		// TODO Auto-generated constructor stub

		boolean listening = true;

		try {
			serverSocket = new ServerSocket(1234);

			InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			System.out.println("IP:" + addr.getHostAddress() + " Port:" + port);

		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}

		while (listening) {
			JetClientToServerThread temp = new JetClientToServerThread(this,
					serverSocket.accept());
			people.add(temp);
			temp.start();
		}

		serverSocket.close();
	}

	public static void main(String[] args) {

		if (args.length != 2) {
			try {
				new JetServer(1234);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				new JetServer(Integer.valueOf(args[0]));
			} catch (Exception e) {
				System.err.println("Port IpAdress");
				System.exit(-1);
			}
		}
	}
}

class JetClientToServerThread extends Thread {
	private JetServer js;
	private Socket socket = null;

	public JetClientToServerThread(JetServer js, Socket socket) {
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

	public void sendInfo() {
		// TODO Auto-generated method stub
		
	}
}

class JetServerToClientsThread extends Thread {
	private JetServer js;
	private Socket socket = null;

	public JetServerToClientsThread(JetServer js, Socket socket) {
		super("JetServerThread");
		this.socket = socket;
		this.js = js;
	}

	public void run() {
		boolean going = true;
        
		while(going){
			for(int i=0; i < js.people.size(); i++){
				JetClientToServerThread person = js.people.get(i);
				person.sendInfo();
				
			}
		}
	}
}
