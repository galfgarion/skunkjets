package skunkjets.client;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

// TODO: make an addjet key send info to the server SkunkJets.p2w_x(Mouse.getX())

public class SkunkJets {
	static interface Notification {
		void go(SkunkJets game);
	}
	
	static class ChangeJetNotification implements Notification {
		public final float newX, newY, newVX, newVY;
		
		public ChangeJetNotification(String[] tokens) {
			newX = Float.parseFloat(tokens[1]);
			newY = Float.parseFloat(tokens[2]);
			newVX = Float.parseFloat(tokens[3]);
			newVY = Float.parseFloat(tokens[4]);
			assert newVX != 0 && newVY != 0;
		}
		
		public void go(SkunkJets game) {
			game.currentSelectedJet.setVelocity(new Vector2f(newVX, newVY));
			game.currentSelectedJet.setPosition(new Vector2f(newX, newY));
		}
	}
	
	static class AddMissileNotification implements Notification {
		public final String missileType;
		public final int id;
		public final float newX, newY, newVX, newVY;
		
		public AddMissileNotification(String[] tokens) {
			id = Integer.parseInt(tokens[1]);
			missileType = tokens[2];
			assert "rocket".equals(missileType) || "beam".equals(missileType);
			newX = Float.parseFloat(tokens[3]);
			newY = Float.parseFloat(tokens[4]);
			newVX = Float.parseFloat(tokens[5]);
			newVY = Float.parseFloat(tokens[6]);
		}
		
		public void go(SkunkJets game) {
			double time = SkunkJets.mainTimer.getTime();
			
			Bullet bullet = null;
			
			if (missileType.equals("rocket"))
				bullet = game.rocket.fire(new Vector2f(newX, newY), new Vector2f(newVX, newVY), true, id);
			else
				bullet = game.rocket.fire(new Vector2f(newX, newY), new Vector2f(newVX, newVY), true, id);
			/* AAAHHHH game.beam.fire...; */ 
			
			game.gameObjects.add(bullet);
		}
	}
	
	/** Intended display mode */
	private DisplayMode mode;

	Cannon redCannon;
	Jet currentSelectedJet;
	
	public static Timer mainTimer = new Timer();
	ProjectileType rocket = new RocketProjectile();
	LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();

	private Socket jetSocket;
	PrintWriter out;
	Scanner in;
	private String ip_g;
	private int port_g;
	private ReadIn ri;

	/**
	 * Creates a FullScreenWindowedTest
	 */
	public SkunkJets() {
		this.ip_g = "localhost";
		this.port_g = 1234;
	}

	public SkunkJets(int port_g, String ip_g) {
		this.ip_g = ip_g;
		this.port_g = port_g;
	}

	/**
	 * Executes the test
	 */
	public void execute() {
		initialize();
		try {
			mainLoop();
		}
		finally {
			cleanup();
		}
	}

	private void switchMode() throws LWJGLException {
		mode = findDisplayMode(Display.getDisplayMode().getWidth(), Display
				.getDisplayMode().getHeight(), Display.getDisplayMode()
				.getBitsPerPixel());
		Display.setDisplayModeAndFullscreen(mode);
	}

	/**
	 * Initializes the test
	 */
	private void initialize() {
		connectToServer();

		try {
			// find displaymode
			switchMode();
			// start of in windowed mode
			Display.create();
			glInit();

			gameObjects.add(redCannon = new Cannon(new Vector2f(0, -1), 1 / 20f, 90, true, 0).setColor(1.0f, 0.0f, 0.0f));
			//redCannon.setCurProjectile(rocket);
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connectToServer() {
		try {
			jetSocket = new Socket(ip_g, port_g);
			out = new PrintWriter(jetSocket.getOutputStream(), true);
			in = new Scanner(jetSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: Jet Socket. So Exiting");
			System.exit(-1);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to: Jet Socket. So Exiting");
			System.exit(-1);
		}
		ri = new ReadIn(this);
		ri.start();
	}

	/**
	 * Runs the main loop of the "test"
	 */
	private void mainLoop() {
		double lastUpdateTime = mainTimer.getTime();

		while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
				&& !Display.isCloseRequested()) {
			int lastButton = -1;

		    // iterate all events, use the last button down
		    while(Mouse.next()) {
		      if(Mouse.getEventButton() != -1 && Mouse.getEventButtonState()) {
		        lastButton = Mouse.getEventButton();
		      }
		    }
		    
		    if (lastButton != -1) {
				out.println("addmissile " + redCannon.orientation);
		    }
			
			Timer.tick();
			double now = mainTimer.getTime();
			double timeDelta = now - lastUpdateTime;
			lastUpdateTime = now;

			if (Display.isVisible()) {
				// check keyboard input
				processKeyboard();
				processMouse();
				// do "game" logic, and render it
				render();
			} else {
				// no need to render/paint if nothing has changed (ie. window
				// dragged over)
				if (Display.isDirty()) {
					render();
				}
				// don't waste cpu time, sleep more
				try {
					Thread.sleep(100);
				} catch (InterruptedException inte) {

				}
			}
			
			synchronized (ri.waitingNotifications) {
				for (Notification notification : ri.waitingNotifications)
					notification.go(this);
			}
			
			// Update window
			Display.update();
		}
	}

	private void processMouse() { // iterate all events, use the last button
		// down
		float x = 2f * Mouse.getX() / mode.getWidth() - 1;
		float y = 2f * Mouse.getY() / mode.getHeight() - 1;
		float newOrientation =(float) (Math.atan2(y + 1, x) * 180 / Math.PI);
		if (redCannon.getOrientation() != newOrientation) {
			redCannon.setOrientation(newOrientation);
			if (out != null)
				out.println("turretangle " + newOrientation);
		}
	}

	private void render() {
		// clear background
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		GL11.glColor3f(0f, 0f, 0.5f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-2 / 3f, -1f);
		GL11.glVertex2f(2 / 3f, -1f);
		GL11.glVertex2f(2 / 3f, 1f);
		GL11.glVertex2f(-2 / 3f, 1f);
		GL11.glEnd();

		for (GameObject gameObject : gameObjects) {
			gameObject.draw();
		}
	}

	private void processKeyboard() {
		//check for speed changes
		if (currentSelectedJet != null) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				out.println("changejet " + currentSelectedJet.id + " fast");
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				out.println("changejet " + currentSelectedJet.id + " slow");
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				out.println("changejet " + currentSelectedJet.id + " right");
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				out.println("changejet " + currentSelectedJet.id + " left");
			}
		}
		
		//check weapon switch
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			//redCannon.setCurProjectile(rocket);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
			
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
			
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
			
		}
	}

	/**
	 * Cleans up the test
	 */
	private void cleanup() {
		Display.destroy();
		out.println("quit");
	}
	
	private DisplayMode findDisplayMode(int width, int height, int bpp)
			throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for (int i = 0; i < modes.length; i++) {
			if (modes[i].getWidth() == width && modes[i].getHeight() == height
					&& modes[i].getBitsPerPixel() >= bpp
					&& modes[i].getFrequency() <= 60) {
				return modes[i];
			}
		}
		return Display.getDesktopDisplayMode();
	}

	private void glInit() {
		int width = mode.getWidth();
		int height = mode.getHeight();
		float ratio = (float) width / height;

		// Go into orthographic projection mode.
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(-ratio, ratio, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, mode.getWidth(), mode.getHeight());
		// set clear color to black
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// sync frame (only works on windows)
		Display.setVSyncEnabled(true);
	}

	/**
	 * Test entry point
	 */
	public static void main(String[] args) {
		SkunkJets fswTest = (args.length == 2) ? new SkunkJets(Integer
				.valueOf(args[0]), args[1]) : new SkunkJets();
		fswTest.execute();
		System.exit(0);
	}

}

class ReadIn extends Thread {
	SkunkJets jc;
	
	List<SkunkJets.Notification> waitingNotifications = new LinkedList<SkunkJets.Notification>();
	
	public ReadIn(SkunkJets jc) {
		this.jc = jc;
	}

	public void run() {
		while (true) {
			if (jc.in.hasNextLine()) {
				String wholeLine = jc.in.nextLine();
				assert wholeLine.length() > 0;
				String[] tokens = wholeLine.split(" ");
				final List<String> types = Arrays.asList(new String[]{ "changejet", "addmissile" });
				synchronized (waitingNotifications) {
					switch (types.indexOf(tokens[0])) {
					case 0:
						waitingNotifications.add(new SkunkJets.ChangeJetNotification(tokens));
						break;
					case 1:
						waitingNotifications.add(new SkunkJets.AddMissileNotification(tokens));
						break;
					}
				}
			}
		}
	}
}
