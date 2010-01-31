<<<<<<< HEAD
=======
/* 
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

<<<<<<< HEAD
public class SkunkJets {
	/** Intended display mode */
	private DisplayMode mode;
	
	//JetClient client;
	
	Cannon redCannon;
	Jet jet;
	
	Timer mainTimer = new Timer();
	List<GameObject> gameObjects = new LinkedList<GameObject>();
	
	/**
	 * Creates a FullScreenWindowedTest
	 */
	public SkunkJets() {
	}
	/**
	 * Executes the test
	 */
	public void execute() {
=======
/**
 * 
 * Tests switching between windowed and fullscreen
 * 
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$ $Id$
 */
public class SkunkJets
{
	/** Intended deiplay mode */
	private DisplayMode mode;

	Cannon redCannon;
	private int lastButton = 0;

	Timer mainTimer = new Timer();
	List<GameObject> gameObjects = new LinkedList<GameObject>();

	/**
	 * Creates a FullScreenWindowedTest
	 */
	public SkunkJets()
	{
	}

	/**
	 * Executes the test
	 */
	public void execute()
	{
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
		initialize();
		mainLoop();
		cleanup();
	}

<<<<<<< HEAD
	private void switchMode() throws LWJGLException {
		mode = findDisplayMode(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), Display.getDisplayMode().getBitsPerPixel());
=======
	private void switchMode() throws LWJGLException
	{
		mode = findDisplayMode(Display.getDisplayMode().getWidth(),
			Display.getDisplayMode().getHeight(), Display.getDisplayMode().getBitsPerPixel());
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
		Display.setDisplayModeAndFullscreen(mode);
	}

	/**
	 * Initializes the test
	 */
<<<<<<< HEAD
	private void initialize() {
		try {
			//client = new JetClient();
			
			//find displaymode
=======
	private void initialize()
	{
		try
		{
			// find displaymode
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
			switchMode();
			// start of in windowed mode
			Display.create();
			glInit();

<<<<<<< HEAD
			gameObjects.add(redCannon = new Cannon(0, -1, 1/20f, 90).setColor(1.0f, 0.0f, 0.0f));
			gameObjects.add(jet = new Jet(new Vector2f(0.5f, -1f), new Vector2f(0.2f, 1f)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Runs the main loop of the "test"
	 */
	private void mainLoop() {
		double lastUpdateTime = mainTimer.getTime();
		
		while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
=======
			gameObjects.add(redCannon = new Cannon(0, -1, 1 / 20f, 90).setColor(1.0f, 0.0f, 0.0f));
			gameObjects.add(new Jet(new Vector2f(0.5f, -1f), new Vector2f(0.2f, 1f)));

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
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
			Timer.tick();
			double now = mainTimer.getTime();
			double timeDelta = now - lastUpdateTime;
			lastUpdateTime = now;
<<<<<<< HEAD
			
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
=======

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
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
				}
			}
			// Update window
			Display.update();
		}
	}
<<<<<<< HEAD
	
	private void processMouse() {    // iterate all events, use the last button down
		float x = 2f * Mouse.getX() / mode.getWidth() - 1;
		float y = 2f * Mouse.getY() / mode.getHeight() - 1;
		redCannon.orientation = (float)(Math.atan2(y + 1, x) * 180 / Math.PI);
	}
	
	private void logic(double timeDelta) {
		for (GameObject gameObject : gameObjects)
			gameObject.update(timeDelta);
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
			//quadVelocity.y += 0.1f;
			jet.speedUp();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			//quadVelocity.y -= 0.1f;
			jet.slowDown();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			//redCannon.turnRight(1f);
			//jet.position.x -= 1/20f;
			jet.turnRight();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			//redCannon.turnLeft(1f);
			//quadVelocity.x -= 0.1f;
			//jet.position.x += 1/20f;
			jet.turnLeft();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			//angleRotation += 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
			//angleRotation -= 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			double time = mainTimer.getTime();
			if(redCannon.canFire(time)) {
				GameObject missile = redCannon.fire(time); 
				//connection.sendNewGameObject(missile);
				gameObjects.add(missile);
			}
		}
	}
	/**
	 * Cleans up the test
	 */
	private void cleanup() {
=======

	private void processMouse()
	{ // iterate all events, use the last button down
		while (Mouse.next())
		{
			if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState())
			{
				lastButton = Mouse.getEventButton();
			}
		}

		float x = 2f * Mouse.getX() / mode.getWidth() - 1;
		float y = 2f * Mouse.getY() / mode.getHeight() - 1;
		float dx = x;
		float dy = y + 1;
		float deg = 90;
		
		if (dx != 0)
		{
			deg = (float) Math.toDegrees(Math.atan(dy / dx));
			if (deg < 0)
				deg += 180;
		}
		redCannon.setOrientation(deg);

	}

	/**
	 * Performs the logic
	 */
	private void logic(double timeDelta)
	{
		for (GameObject gameObject : gameObjects)
			gameObject.update(timeDelta);

		/*
		 * angle += angleRotation; if (angle > 90.0f) { angle = 0.0f; }
		 * quadPosition.x += quadVelocity.x; quadPosition.y += quadVelocity.y;
		 * //check colision with vertical border border if (quadPosition.x + 50
		 * >= mode.getWidth() || quadPosition.x - 50 <= 0) { quadVelocity.x *=
		 * -1; } //check collision with horizontal border if (quadPosition.y +
		 * 50 >= mode.getHeight() || quadPosition.y - 50 <= 0) { quadVelocity.y
		 * *= -1; }
		 */
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

		/*
		 * GL11.glPushMatrix(); { GL11.glTranslatef(0f, 0.0f, 0.0f);
		 * GL11.glColor3f(1.0f, 0.0f, 0.0f); GL11.glBegin(GL11.GL_QUADS); {
		 * GL11.glVertex2f(10, 10); GL11.glVertex2f(20, 10); GL11.glVertex2f(20,
		 * 20); GL11.glVertex2f(10, 20); } GL11.glEnd(); } GL11.glPopMatrix();
		 */
		/*
		 * // draw white quad GL11.glPushMatrix(); {
		 * GL11.glTranslatef(quadPosition.x, quadPosition.y, 0);
		 * GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f); GL11.glColor3f(1.0f, 1.0f,
		 * 1.0f); GL11.glBegin(GL11.GL_QUADS); { GL11.glVertex2i(-50, -50);
		 * GL11.glVertex2i(50, -50); GL11.glVertex2i(50, 50);
		 * GL11.glVertex2i(-50, 50); } GL11.glEnd(); } GL11.glPopMatrix();
		 */
	}

	/**
	 * Processes keyboard input
	 */
	private void processKeyboard()
	{
		// check for fullscreen key
		if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			try
			{
				switchMode();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// check for window key
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			try
			{
				mode = new DisplayMode(1440, 900);
				Display.setDisplayModeAndFullscreen(mode);
				glInit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// check for speed changes
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			// quadVelocity.y += 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			// quadVelocity.y -= 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			// redCannon.turnRight(1f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			// redCannon.turnLeft(1f);
			// quadVelocity.x -= 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD))
		{
			// angleRotation += 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT))
		{
			// angleRotation -= 0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			double time = mainTimer.getTime();
			if (redCannon.canFire(time))
			{
				gameObjects.add(redCannon.fire(time));
			}
		}
		// throttle
		/*
		 * if (quadVelocity.x < -MAX_SPEED) { quadVelocity.x = -MAX_SPEED; } if
		 * (quadVelocity.x > MAX_SPEED) { quadVelocity.x = MAX_SPEED; } if
		 * (quadVelocity.y < -MAX_SPEED) { quadVelocity.y = -MAX_SPEED; } if
		 * (quadVelocity.y > MAX_SPEED) { quadVelocity.y = MAX_SPEED; } if
		 * (angleRotation < 0.0f) { angleRotation = 0.0f; } if (angleRotation >
		 * MAX_SPEED) { angleRotation = MAX_SPEED; }
		 */
	}

	/**
	 * Cleans up the test
	 */
	private void cleanup()
	{
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
		Display.destroy();
	}

	/**
	 * Retrieves a displaymode, if one such is available
	 * 
	 * @param width
	 *            Required width
	 * @param height
	 *            Required height
	 * @param bpp
	 *            Minimum required bits per pixel
	 * @return
	 */
<<<<<<< HEAD
	private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for (int i = 0; i < modes.length; i++) {
			if (modes[i].getWidth() == width && modes[i].getHeight() == height && modes[i].getBitsPerPixel() >= bpp && modes[i].getFrequency() <= 60) {
=======
	private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException
	{
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for (int i = 0; i < modes.length; i++)
		{
			if (modes[i].getWidth() == width && modes[i].getHeight() == height
				&& modes[i].getBitsPerPixel() >= bpp && modes[i].getFrequency() <= 60)
			{
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
				return modes[i];
			}
		}
		return Display.getDesktopDisplayMode();
	}
<<<<<<< HEAD
	/**
	 * Initializes OGL
	 */
	private void glInit() {
		int width = mode.getWidth();
		int height = mode.getHeight();
		float ratio = (float)width / height;
		
=======

	/**
	 * Initializes OGL
	 */
	private void glInit()
	{
		int width = mode.getWidth();
		int height = mode.getHeight();
		float ratio = (float) width / height;

>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
		// Go into orthographic projection mode.
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(-ratio, ratio, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, mode.getWidth(), mode.getHeight());
<<<<<<< HEAD
		//set clear color to black
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//sync frame (only works on windows)
		Display.setVSyncEnabled(true);
	}
	/**
	 * Test entry point
	 */
	public static void main(String[] args) {
		System.out.println("Change between fullscreen and windowed mode, by pressing F and W respectively");
		System.out.println("Move quad using arrowkeys, and change rotation using +/-");
=======
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
>>>>>>> e68d4d775d6343989df1da34e5d868792b0c9115
		SkunkJets fswTest = new SkunkJets();
		fswTest.execute();
		System.exit(0);
	}
}