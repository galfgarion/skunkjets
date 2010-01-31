// moo

package skunkjets.server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector2f;

import skunkjets.client.Bullet;
import skunkjets.client.Cannon;
import skunkjets.client.Explosion;
import skunkjets.client.GameObject;
import skunkjets.client.Jet;
import skunkjets.client.ProjectileType;
import skunkjets.client.SkunkJets;

class PlayerData {
	HashMap<Integer, GameObject> gameObjects = new HashMap<Integer, GameObject>();
	LinkedList<Explosion> explosions = new LinkedList<Explosion>();
	HashMap<Integer, Jet> jets = new HashMap<Integer, Jet>();
	
	public void addObject(GameObject obj) {
		gameObjects.put(obj.id, obj);
		if (obj instanceof Explosion)
			explosions.add((Explosion)obj);
		if (obj instanceof Jet)
			jets.put(obj.id, (Jet)obj);
	}
	
	Cannon redCannon;
	public float lastJetSpawnTime = -25;
	public float lastBulletSpawnTime = -25;
	public int playerNumber;

	public void writeData(PrintWriter stream, boolean friendly, boolean flip) {
		for (GameObject object : gameObjects.values()) {
			String positionAndVelocity;
			if (flip)
				positionAndVelocity = -object.getPosition().x + " " + -object.getPosition().y + " " + -object.getVelocity().x + " " + -object.getVelocity().y;
			else
				positionAndVelocity = object.getPosition().x + " " + object.getPosition().y + " " + object.getVelocity().x + " " + object.getVelocity().y;
				
			if (object instanceof Bullet)
				stream.println("bullet " + friendly + " " + ((Bullet)object).img + " " + positionAndVelocity);
			else if (object instanceof Jet)
				stream.println("jet " + friendly + " " + object.id + " " + positionAndVelocity);
			else if (object instanceof Cannon)
				stream.println("cannon " + friendly + " " + object.id + " " + positionAndVelocity);
			else if (object instanceof Explosion)
				stream.println("explosion " + positionAndVelocity);
		}
	}
}

class ListenerThread extends Thread {
	private static final int MAX_JETS = 3;
	private static final int TIME_BETWEEN_JET_SPAWN = 5;

	private JetServer js;
	private Socket socket = null;
	LogicThread logicThread;

	float tturretAngle = 0;
	
	PlayerData playerData = new PlayerData();
	PrintWriter out;
	
/*
	public Bullet fire() {
	}*/
	public ListenerThread(JetServer js, int playerNumber, Socket socket) {
		
			super("JetServerThread");
			this.socket = socket;
			this.js = js;
			playerData.playerNumber = playerNumber;
	
			playerData.redCannon = new Cannon(new Vector2f(0, -1), 1 / 20f, 90f, true, 0)
					.setColor(1.0f, 0.0f, 0.0f);
			playerData.addObject(playerData.redCannon);
			
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Couldnt create print writer, exiting");
				e.printStackTrace();
				System.exit(-1);
			}
	}

	public void run() {
		assert logicThread != null;
		
		try {
			Scanner in = new Scanner(socket.getInputStream());

			outerloop:
			while (true) {
				String line = in.nextLine();
				String[] tokens = line.split(" ");

				final List<String> types = Arrays
						.asList(new String[] { "quit", "changejet", "addjet", "addmissile",
								"turretangle", "lightning" });
				switch (types.indexOf(tokens[0])) {
				case 0:
					break outerloop;
					
				case 1:
					int jetID = Integer.parseInt(tokens[1]);
					String request = tokens[2];
					Jet jet = playerData.jets.get(jetID);

					if ("left".equals(request))
						jet.turnLeft();
					else if ("right".equals(request))
						jet.turnRight();
					else if ("fast".equals(request))
						jet.speedUp();
					else if ("slow".equals(request))
						jet.slowDown();
					else
						assert false;
					
					break;
					
				case 2:
					float worldX = Float.parseFloat(tokens[1]);
					
					if (canSpawnJet()) {
						Jet newJet = new Jet(new Vector2f(worldX, -1.0f), new Vector2f(0, 0.2f), true, logicThread.getNewID());
						playerData.addObject(newJet);
						playerData.lastJetSpawnTime = logicThread.mainTimer.getTime();
					}

				case 3:

					double oldLastFireTime = playerData.lastBulletSpawnTime;
					if (oldLastFireTime + 5 >= logicThread.mainTimer.getTime())
						break;
					
					System.out.println("last fire time: " + playerData.lastBulletSpawnTime);		
					playerData.lastBulletSpawnTime = SkunkJets.mainTimer.getTime();
					
					ProjectileType currentProjectile = null;
					String missileType = tokens[1];
					if ("rocket".equals(missileType))
						currentProjectile = logicThread.rocket;
					else if ("beam".equals(missileType))
						currentProjectile = logicThread.beam;
					
					float missileDegrees = Float.parseFloat(tokens[2]);
					
					float velX = (float) (currentProjectile.getSpeed() * Math.cos(Math.toRadians(missileDegrees)));
					float velY = (float) (currentProjectile.getSpeed() * Math.sin(Math.toRadians(missileDegrees)));

					Vector2f velocity = new Vector2f(velX, velY);
					//TODO fix start of bullet
					Bullet bullet = currentProjectile.fire(new Vector2f(new Vector2f(0, -1)), velocity, true, logicThread.getNewID());
					playerData.addObject(bullet);
					
					break;

				case 4:
					float turretDegrees = Float.parseFloat(tokens[1]);
					playerData.redCannon.orientation = turretDegrees;
					break;
				}
			}

			out.close();
			in.close();
			socket.close();
			js.people.remove(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean canSpawnJet() {
		return ((SkunkJets.mainTimer.getTime() - playerData.lastJetSpawnTime) >= TIME_BETWEEN_JET_SPAWN)
				&& (playerData.jets.size() < MAX_JETS);
	}
	
	public void sendInfo(ListenerThread other) {
		boolean switched = (this.logicThread.one == this);

		this.playerData.writeData(out, true, switched);
		other.playerData.writeData(out, false, switched);
	}
	
}