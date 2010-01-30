
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

class Cannon implements GameObject {
	
	static final float DEG_TO_RAD =  (float) Math.PI / 180.0f;
	
	private float centerX, centerY, orientation, radius;
	private float red, green, blue;
	private float projectileSpeed = 0.5f;
	
	private double lastFireTime = 0;
	
	private Projectile curProjectile = new Projectile(null, null);

	public Cannon(float centerX, float centerY, float radius, float orientation) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.orientation = orientation; // degrees, 0.0 is pointed up positive y axis
		this.red = 1.0f;
		this.green = 0.0f;
		this.blue = 1.0f;
	}

	public Cannon setColor(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		return this;
	}
	
	public void turnLeft(float changeInDegrees) {
		orientation += changeInDegrees;
	}
	
	public void turnRight(float changeInDegrees) {
		orientation -= changeInDegrees;
	}
	
	public void update(double timeDelta) { }

	private static void drawCircle() {
		final int numVertices = 60;

		Vector2f vertices[] = new Vector2f[numVertices];

		for (int i = 0; i < numVertices; i++) {
			vertices[i] = new Vector2f();
			vertices[i].x = (float) Math.cos(i * 2 * Math.PI / numVertices);
			vertices[i].y = (float) Math.sin(i * 2 * Math.PI / numVertices);
			System.out.println(i * 360.0f / numVertices);
		}

		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for (int i = 0; i < numVertices; i++) {
			float x = vertices[i].x;
			float y = vertices[i].y;
			System.out.println("vertexxing at " + x + "," + y);
			GL11.glVertex2f(x, y);
		}
		GL11.glEnd();
	}
	
	public double getOrientation() {
		return orientation;
	}
	
	public boolean canFire(double time) {
		
		return (time - lastFireTime >= 1.0f / curProjectile.maxFiringRate());
		
	}
	
	public Projectile fire(double time) {
		
		lastFireTime = time;
		
		float velX = (float) (projectileSpeed * Math.cos(orientation * DEG_TO_RAD));
		float velY = (float) (projectileSpeed * Math.sin(orientation * DEG_TO_RAD));
		
		Vector2f velocity = new Vector2f(velX, velY);
		
		Projectile bullet = new Projectile(new Vector2f(centerX, centerY), velocity);
		
		return bullet;
	}

	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslated(centerX, centerY, 0);
		GL11.glScalef(radius, radius, 1);
		
		GL11.glColor3f(red, green, blue);
		drawCircle();

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		float barrelX = (float) Math.cos(Math.PI / 8);
		float barrelY = (float) Math.sin(Math.PI / 8);

		GL11.glRotatef(orientation, 0, 0, 1);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(barrelX, barrelY);
		GL11.glVertex2f(barrelX + 1, barrelY);
		GL11.glVertex2f(barrelX + 1, -barrelY);
		GL11.glVertex2f(barrelX, -barrelY);
		GL11.glEnd();

		GL11.glPopMatrix();
	}
}
