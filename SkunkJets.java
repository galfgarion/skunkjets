import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
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

public class SkunkJets
{
	/** Intended display mode */
	private DisplayMode mode;

	// JetClient client;

	Cannon redCannon;
	Jet jet;

	public static Timer mainTimer = new Timer();
	ProjectileType rocket = new RocketProjectile();
	ProjectileType beam = new BeamProjectile();
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
	public SkunkJets()
	{
		this.ip_g = "localhost";
		this.port_g = 1234;
	}

	public SkunkJets(int port_g, String ip_g)
	{
		this.ip_g = ip_g;
		this.port_g = port_g;
	}

	/**
	 * Executes the test
	 */
	public void execute()
	{
		initialize();
		mainLoop();
		cleanup();
	}

	private void switchMode() throws LWJGLException
	{
		mode = findDisplayMode(Display.getDisplayMode().getWidth(), Display
				.getDisplayMode().getHeight(), Display.getDisplayMode()
				.getBitsPerPixel());
		Display.setDisplayModeAndFullscreen(mode);
	}

	/**
	 * Initializes the test
	 */
	private void initialize()
	{
		// connectToServer();
		try
		{
			switchMode();
			Display.create();
			glInit();

			
			gameObjects.add(redCannon = new Cannon(new Vector2f(0, -1), 1 / 20f, 90).setColor(1.0f, 0.0f, 0.0f));
			redCannon.setCurProjectile(rocket);
			gameObjects.add(jet = new Jet(new Vector2f(0.5f, -1f), new Vector2f(0.2f, 0.1f), true));

			// TODO testing
			gameObjects.add(new Jet(new Vector2f(0, 0), new Vector2f(0f, 0f), false));

			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void connectToServer()
	{
		try
		{
			jetSocket = new Socket(ip_g, port_g);
			out = new PrintWriter(jetSocket.getOutputStream(), true);
			in = new Scanner(jetSocket.getInputStream());
		}
		catch (UnknownHostException e)
		{
			System.err.println("Don't know about host: Jet Socket. So Exiting");
			System.exit(-1);
		}
		catch (IOException e)
		{
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
	private void mainLoop()
	{
		double lastUpdateTime = mainTimer.getTime();

		while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested())
		{
			int lastButton = -1;

			// iterate all events, use the last button down
			while (Mouse.next())
			{
				if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState())
				{
					lastButton = Mouse.getEventButton();
				}
			}

			if (lastButton != -1)
			{
				System.out.println(lastButton);

				if (lastButton == 0) {
					double time = mainTimer.getTime();
					if (redCannon.canFire())
					{
						GameObject missile = redCannon.fire();
						// connection.sendNewGameObject(missile);
						gameObjects.add(missile);
					}
				}
				else if (lastButton == 1)
				{
					
				}
			}

			Timer.tick();
			double now = mainTimer.getTime();
			double timeDelta = now - lastUpdateTime;
			lastUpdateTime = now;

			if (Display.isVisible())
			{
				// check keyboard input
				processKeyboard();
				processMouse();
				// do "game" logic, and render it
				logic(timeDelta);
				render();
			}
			else
			{
				// no need to render/paint if nothing has changed (ie. window
				// dragged over)
				if (Display.isDirty())
				{
					render();
				}
				// don't waste cpu time, sleep more
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException inte)
				{

				}
			}
			// Update window
			Display.update();
		}
	}

	private void processMouse()
	{ // iterate all events, use the last button
		// down
		float x = 2f * Mouse.getX() / mode.getWidth() - 1;
		float y = 2f * Mouse.getY() / mode.getHeight() - 1;
		redCannon
				.setOrientation((float) (Math.atan2(y + 1, x) * 180 / Math.PI));
	}

	private void logic(double timeDelta)
	{
		for (GameObject gameObject : gameObjects)
			gameObject.update(timeDelta);

		ArrayList<GameObject> destroyedObjects = new ArrayList<GameObject>();
		for (int i = 0; i < gameObjects.size(); i++)
		{
			for (int j = i + 1; j < gameObjects.size(); j++)
			{
				if (gameObjects.get(i) != redCannon
						&& gameObjects.get(j) != redCannon
						&& gameObjects.get(i).collide(gameObjects.get(j)))
				{
					destroyedObjects.add(gameObjects.get(i));
					destroyedObjects.add(gameObjects.get(j));
				}
			}
		}

		// explode the objects
		for (GameObject obj : destroyedObjects)
		{
			gameObjects.remove(obj);
		}
	}

	private void render()
	{
		// clear background
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		GL11.glColor3f(0f, 0f, 0.5f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-2 / 3f, -1f);
		GL11.glVertex2f(2 / 3f, -1f);
		GL11.glVertex2f(2 / 3f, 1f);
		GL11.glVertex2f(-2 / 3f, 1f);
		GL11.glEnd();

		for (GameObject gameObject : gameObjects)
		{
			gameObject.draw();
		}
	}

	private void processKeyboard()
	{
		// check for speed changes
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			jet.speedUp();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			jet.slowDown();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			jet.turnRight();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			jet.turnLeft();
		}

		// check weapon switch
		if (Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			redCannon.setCurProjectile(rocket);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			redCannon.setCurProjectile(beam);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_3))
		{

		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_4))
		{

		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_5))
		{

		}
	}

	/**
	 * Cleans up the test
	 */
	private void cleanup()
	{
		Display.destroy();
	}

	private DisplayMode findDisplayMode(int width, int height, int bpp)
			throws LWJGLException
	{
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for (int i = 0; i < modes.length; i++)
		{
			if (modes[i].getWidth() == width && modes[i].getHeight() == height
					&& modes[i].getBitsPerPixel() >= bpp
					&& modes[i].getFrequency() <= 60)
			{
				return modes[i];
			}
		}
		return Display.getDesktopDisplayMode();
	}

	private void glInit()
	{
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
	public static void main(String[] args)
	{
		SkunkJets fswTest = (args.length == 2) ? new SkunkJets(Integer
				.valueOf(args[0]), args[1]) : new SkunkJets();
		fswTest.execute();
		System.exit(0);
	}

}

class ReadIn extends Thread
{
	SkunkJets jc;

	public ReadIn(SkunkJets jc)
	{
		this.jc = jc;
	}

	public void run()
	{
		while (true)
		{
			if (jc.in.hasNextLine())
			{
				System.out.println(jc.in.nextLine());
			}
		}
	}
}
