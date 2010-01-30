import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class JetClient {
	PrintWriter out;
	Socket client;
	
	JetClient() {
		try {
			System.out.print("Enter ip: ");
			Scanner scanner = new Scanner(System.in);
			byte[] addr = new byte[4];
			for (int i = 0; i < 4; i++)
				addr[i] = (byte)scanner.nextInt();
			System.out.println("doing stuff now");
			client = new Socket(InetAddress.getByAddress(addr), 1234, true);
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
	}
	
	List<GameObject> objectsToSend = new LinkedList<GameObject>();
	
	public void handleNetworkShit() throws IOException {
		/*InputStream in = client.getInputStream();
		int available = in.available();
		
		if (available > 0) {
			try {
				byte[] bytes = new byte[available];
				
				
				
				String line;
				
				in.read
				
				line = in.readLine();
				// Send data back to client
				out.println(line);
				// Append data to text area
				System.out.println(line);
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			}
		}*/
	}
}