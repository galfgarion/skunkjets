package skunkjets.server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Timer;
import org.lwjgl.util.vector.Vector2f;

import skunkjets.client.BeamProjectile;
import skunkjets.client.Bullet;
import skunkjets.client.Cannon;
import skunkjets.client.Explosion;
import skunkjets.client.GameObject;
import skunkjets.client.Jet;
import skunkjets.client.ProjectileType;
import skunkjets.client.RocketProjectile;
import skunkjets.client.SkunkJets;

public class JetServer {
	ArrayList<ListenerThread> people = new ArrayList<ListenerThread>();
	ArrayList<LogicThread> logics = new ArrayList<LogicThread>();
	ServerSocket serverSocket = null;
	int port;

	public JetServer(int port) throws IOException {
		this.port = port;
		// TODO Auto-generated constructor stub

		boolean listening = true;

		try {
			serverSocket = new ServerSocket(port);

			InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			System.out.println("IP:" + addr.getHostAddress() + " Port:" + port);

		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}

		int playerNumber = 0;
		while (listening) {
			ListenerThread temp = new ListenerThread(this, playerNumber++, serverSocket.accept());
			System.out.println("Client Added");
			people.add(temp);
			
			if (playerNumber == 2) {
				playerNumber = 0;
				LogicThread tempLogic = new LogicThread(this, people.get(people.size() - 2), people.get(people.size() - 1));
				logics.add(tempLogic);
				tempLogic.start();
				people.get(people.size() - 2).start();
				temp.start();
			}

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
