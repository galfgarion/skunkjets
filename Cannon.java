import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

class Cannon extends GameObject
{
	static final float DEG_TO_RAD = (float) Math.PI / 180.0f;

	private float orientation;
	private float radius;
	private float red, green, blue;

	private double lastFireTime = 0;

	private ProjectileType curProjectile = new RocketProjectile();
	ArrayList<Bullet> bullets;

	public Cannon(Vector2f center, float radius, float orientation)
	{
		super(center, new Vector2f(0, 0));
		this.radius = radius;
		this.orientation = orientation; // degrees, 90.0 is pointed up positive y axis
		this.red = 1.0f;
		this.green = 0.0f;
		this.blue = 1.0f;
		bullets = new ArrayList<Bullet>();
	}

	public Cannon setColor(float red, float green, float blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		return this;
	}

	public void update(double timeDelta)
	{
	}

	public double getOrientation()
	{
		return orientation;
	}

	public void setOrientation(float orientation)
	{
		this.orientation = orientation;
	}

	public boolean canFire(double time)
	{
		return ((time - lastFireTime) >= (1.0f / curProjectile.getFiringRate()))
			&& (curProjectile.count < curProjectile.maxBullets);
	}

	public Bullet fire(double time)
	{
		lastFireTime = time;
		float velX = (float) (curProjectile.getSpeed() * Math.cos(orientation * DEG_TO_RAD));
		float velY = (float) (curProjectile.getSpeed() * Math.sin(orientation * DEG_TO_RAD));

		Vector2f velocity = new Vector2f(velX, velY);
		//TODO fix start of bullet
		Bullet bullet = curProjectile.fire(new Vector2f(getPosition()), velocity);
		bullets.add(bullet);
		return bullet;
	}

	private static void drawCircle() {
		final int numVertices = 60;

		Vector2f vertices[] = new Vector2f[numVertices];

		for (int i = 0; i < numVertices; i++) {
			vertices[i] = new Vector2f();
			vertices[i].x = (float) Math.cos(i * 2 * Math.PI / numVertices);
			vertices[i].y = (float) Math.sin(i * 2 * Math.PI / numVertices);
		}

		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for (int i = 0; i < numVertices; i++) {
			GL11.glVertex2f(vertices[i].x, vertices[i].y);
		}
		GL11.glEnd();
	}

	public void innerDraw()
	{
		GL11.glScalef(radius, radius, 1);

		GL11.glColor3f(red, green, blue);
		drawCircle();

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		float barrelX = (float) Math.cos(Math.PI / 8);
		float barrelY = (float) Math.sin(Math.PI / 8);
		//System.out.println("barrelX: " + barrelX + ", barrelY: " + barrelY);

		GL11.glRotatef(orientation, 0, 0, 1);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(barrelX, barrelY);
		GL11.glVertex2f(barrelX + 1, barrelY);
		GL11.glVertex2f(barrelX + 1, -barrelY);
		GL11.glVertex2f(barrelX, -barrelY);
		GL11.glEnd();
	}
}
