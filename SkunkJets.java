
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

public class SkunkJets {
	/** Intended display mode */
	private DisplayMode mode;
	
	//JetClient client;
	
	Cannon redCannon;
	Jet jet;
	
	Timer mainTimer = new Timer();
	LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
	
	/**
	 * Creates a FullScreenWindowedTest
	 */
	public SkunkJets() {
	}
	/**
	 * Executes the test
	 */
	public void execute() {
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
			
		try
		{
			// find displaymode
			switchMode();
			// start of in windowed mode
			Display.create();
			glInit();

			gameObjects.add(redCannon = new Cannon(new Vector2f(0, -1), 1 / 20f, 90).setColor(1.0f, 0.0f, 0.0f));
			gameObjects.add(jet = new Jet(new Vector2f(0.5f, -1f), new Vector2f(0.2f, 1f)));
			
			// TODO testing
			gameObjects.add(new Jet(new Vector2f(0, 0), new Vector2f(0,0)));

			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
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
		    while(Mouse.next()) {
		      if(Mouse.getEventButton() != -1 && Mouse.getEventButtonState()) {
		        lastButton = Mouse.getEventButton();
		      }
		    }
		    
		    if (lastButton != -1) {
		    	System.out.println(lastButton);
		    	
		    	//if (lastButton == 1) {
					double time = mainTimer.getTime();
					if(redCannon.canFire(time)) {
						GameObject missile = redCannon.fire(time); 
						//connection.sendNewGameObject(missile);
						gameObjects.add(missile);
					}
		    	//}
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
				logic(timeDelta);
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
			// Update window
			Display.update();
		}
	}
	
	private void processMouse() {    // iterate all events, use the last button down
		float x = 2f * Mouse.getX() / mode.getWidth() - 1;
		float y = 2f * Mouse.getY() / mode.getHeight() - 1;
		redCannon.setOrientation((float)(Math.atan2(y + 1, x) * 180 / Math.PI));
	}
	
	private void logic(double timeDelta) {
		for (GameObject gameObject : gameObjects)
			gameObject.update(timeDelta);
		
		ArrayList<GameObject> destroyedObjects = new ArrayList<GameObject>();
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
			gameObjects.remove(obj);
		}
	}
	
	private void render() {
		//clear background
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glColor3f(0f, 0f, 0.5f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-2/3f, -1f);
		GL11.glVertex2f(2/3f, -1f);
		GL11.glVertex2f(2/3f, 1f);
		GL11.glVertex2f(-2/3f, 1f);
		GL11.glEnd();
		
		for (GameObject gameObject : gameObjects) {
			gameObject.draw();
		}
	}
	
	private void processKeyboard() {
		//check for fullscreen key
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			try {
				switchMode();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//check for window key
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			try {
				mode = new DisplayMode(1440, 900);
				Display.setDisplayModeAndFullscreen(mode);
				glInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//check for speed changes
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			jet.speedUp();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			jet.slowDown();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			jet.turnRight();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			jet.turnLeft();
		}
	}
	
	/**
	 * Cleans up the test
	 */
	private void cleanup() {
		Display.destroy();
	}

	private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException
	{
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for (int i = 0; i < modes.length; i++)
		{
			if (modes[i].getWidth() == width && modes[i].getHeight() == height
				&& modes[i].getBitsPerPixel() >= bpp && modes[i].getFrequency() <= 60)
			{
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
		//set clear color to black
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//sync frame (only works on windows)
		Display.setVSyncEnabled(true);
	}
	/**
	 * Test entry point
	 */
	public static void main(String[] args) {
		SkunkJets fswTest = new SkunkJets();
		fswTest.execute();
		System.exit(0);
	}

}