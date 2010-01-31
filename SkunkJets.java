import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Map.Entry;
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
	private static final int MAX_JETS = 5;
	private static final int TIME_BETWEEN_JET_SPAWN = 5;

	/** Intended display mode */
	private DisplayMode mode;
	
	public float lowerXBound = -2/3f;
	public float upperXBound = 2/3f;
	public float lowerYBound = -1f;
	public float upperYBound = 1f;
	
	//JetClient client;
	
	Cannon redCannon;

	public static Timer mainTimer = new Timer();
	ProjectileType rocket = new RocketProjectile();
	ProjectileType beam = new BeamProjectile();
	LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
	
	LinkedList<Explosion> explosions = new LinkedList<Explosion>();
	ArrayList<Jet> jets = new ArrayList<Jet>();
	Jet jet;
	int curJet = 0;

	private Socket jetSocket;
	PrintWriter out;
	Scanner in;
	private String ip_g;
	private int port_g;
	private ReadIn ri;

	private static float lastJetSpawnTime = 0;

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

	public static float p2w_x(int x)
	{
		return (float) (((2 * x) + 1.0 - Display.getDisplayMode().getWidth()) / Display.getDisplayMode().getHeight());
	}

	public static float p2w_y(int y)
	{
		return (float) (((2 * y) + 1.0 - Display.getDisplayMode().getHeight()) / Display.getDisplayMode().getHeight());
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
		mode = findDisplayMode(Display.getDisplayMode().getWidth(),
			Display.getDisplayMode().getHeight(), Display.getDisplayMode().getBitsPerPixel());
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
			
			Jet newJet = new Jet(new Vector2f(-0.5f, -1f), new Vector2f(0.0f, 0.05f), true);
			jets.add(newJet);
			gameObjects.add(newJet);
			jet = newJet;

			// TODO testing
			gameObjects.add(new Jet(new Vector2f(0, 0.5f), new Vector2f(0f, 0f), false));
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
			System.err.println("Couldn't get I/O for the connection to: Jet Socket. So Exiting");
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
					if(canSpawnJet()) {
						Jet newJet = new Jet(new Vector2f(p2w_x(Mouse.getX()), -1.0f), new Vector2f(0, 0.2f), true);
						jets.add(newJet);
						gameObjects.add(newJet);
						lastJetSpawnTime = mainTimer.getTime();
					}
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

	private boolean canSpawnJet()
	{
		return ((SkunkJets.mainTimer.getTime() - lastJetSpawnTime ) >= TIME_BETWEEN_JET_SPAWN)
		&& (jets.size() < MAX_JETS);
	}

	private void processMouse()
	{ // iterate all events, use the last button
		// down
		float x = 2f * Mouse.getX() / mode.getWidth() - 1;
		float y = 2f * Mouse.getY() / mode.getHeight() - 1;
		redCannon.setOrientation((float) (Math.atan2(y + 1, x) * 180 / Math.PI));
	}
	
	private void logic(double timeDelta) {
	   ArrayList<GameObject> destroyedObjects = new ArrayList<GameObject>();
	   ArrayList<Explosion> destroyedExplosions = new ArrayList<Explosion>();
		for (GameObject gameObject : gameObjects)
		{
			if (gameObject.update(timeDelta)) destroyedObjects.add(gameObject);
		}
		
		for(int i = 0; i < gameObjects.size(); i++) {
			for(int j=i + 1; j < gameObjects.size(); j++) {
				if(gameObjects.get(i) != redCannon && gameObjects.get(j) != redCannon &&
						gameObjects.get(i).collide(gameObjects.get(j))) {
					destroyedObjects.add(gameObjects.get(i));
					destroyedObjects.add(gameObjects.get(j));
				}
			}
		}

		// explode the objects
		for(GameObject obj : destroyedObjects) {
         explosions.add(new Explosion(obj.getPosition()));
			gameObjects.remove(obj);
		}
		
		for (Explosion obj : explosions)
		{
		   if (obj.update(timeDelta)) destroyedExplosions.add(obj);
		}
		
		for (Explosion obj : destroyedExplosions)
		   explosions.remove(obj);
	}

	private void render()
	{
		// clear background
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		GL11.glColor3f(0f, 0f, 0.5f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(lowerXBound, lowerYBound);
		GL11.glVertex2f(upperXBound, lowerYBound);
		GL11.glVertex2f(upperXBound, upperYBound);
		GL11.glVertex2f(lowerXBound, upperYBound);
		GL11.glEnd();

		for (GameObject gameObject : gameObjects)
		{
			gameObject.draw();
		}
		for (Explosion obj : explosions)
		   obj.draw();
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

		if (Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			curJet++;
			if (curJet >= jets.size())
			{
				curJet = 0;
			}
			jet = jets.get(curJet);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			curJet--;
			if (curJet < 0)
			{
				curJet = jets.size() - 1;
			}
			jet = jets.get(curJet);
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
		
		//check for fullscreen key
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			try {
				switchMode();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		HashMap<Integer, Integer> slotKeys = new HashMap<Integer, Integer>();
		slotKeys.put(Keyboard.KEY_Z, 1);
		slotKeys.put(Keyboard.KEY_X, 2);
		slotKeys.put(Keyboard.KEY_C, 3);
		slotKeys.put(Keyboard.KEY_V, 4);
		slotKeys.put(Keyboard.KEY_B, 5);
		slotKeys.put(Keyboard.KEY_N, 6);
		slotKeys.put(Keyboard.KEY_M, 7);
		slotKeys.put(Keyboard.KEY_COMMA, 8);
		slotKeys.put(Keyboard.KEY_PERIOD, 9);
		slotKeys.put(Keyboard.KEY_SLASH, 10);
		
		for(Entry<Integer, Integer> entry: slotKeys.entrySet()) {
			if(Keyboard.isKeyDown(entry.getKey())) {
				spawnJet(entry.getValue());
			}
		}
		/*
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			//gameObjects.add(new Jet(new Vector2f(0, 0), new Vector2f(0, 0)));
			spawnJet(Keyboard.KEY_A);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			//gameObjects.add(new Jet(new Vector2f(0, 0), new Vector2f(0, 0)));
			spawnJet(Keyboard.KEY_S);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			//gameObjects.add(new Jet(new Vector2f(0, 0), new Vector2f(0, 0)));
			spawnJet(Keyboard.KEY_D);
		}
		*/
		
	}
	
	private void spawnJet(int slot) {
		double sspaceLeft = -(float)mode.getWidth() / mode.getHeight();
		double sspaceWidth = 2.0 * (float)mode.getWidth() / mode.getHeight();
		double posx = slot * (sspaceWidth / 10f) + sspaceLeft;
	
		Vector2f position = new Vector2f((float) posx, 1);
		Vector2f velocity = new Vector2f(0.0f, -0.2f);
		
		//position = new Vector2f(0, 1);
		//velocity = new Vector2f(0, 0);
		
		System.err.println("spawnJet");
	
		gameObjects.add(new Jet(position, velocity, false));
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
		
      GL11.glEnable(GL11.GL_TEXTURE_2D);
		//sync frame (only works on windows)
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
