import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

class Cannon extends GameObject
{
	static final float DEG_TO_RAD = (float) Math.PI / 180.0f;

	private float orientation;
	private float radius;
	private float red, green, blue;

	private double lastFireTime = -10000;

	private ProjectileType curProjectile;
	ArrayList<Bullet> bullets;
	int img;
	
	public Cannon(Vector2f center, float radius, float orientation, boolean myTeam)
	{
		super(center, new Vector2f(0, 0), myTeam);
		this.myTeam = myTeam;
		this.radius = radius;
		this.orientation = orientation; // degrees, 90.0 is pointed up positive y axis
		this.red = 1.0f;
		this.green = 0.0f;
		this.blue = 1.0f;
		visibilityRadius = .3f;
		bullets = new ArrayList<Bullet>();
		if (myTeam)
		{
		   img = ImageLib.getImage("Images/turret blue.png");
		}
		else
		{
		   img = ImageLib.getImage("Images/turret red.png");
		}
	}

	public Cannon setColor(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		return this;
	}

	public void setCurProjectile(ProjectileType curProjectile)
	{
		this.curProjectile = curProjectile;
	}

	public boolean update(double timeDelta) { return false; }

	public double getOrientation()
	{
		return orientation;
	}

	public void setOrientation(float orientation)
	{
		this.orientation = orientation;
	}

	public boolean canFire()
	{
		System.out.println((SkunkJets.mainTimer.getTime() - lastFireTime) + " " + (1.0f / curProjectile.getFiringRate()));
		return ((SkunkJets.mainTimer.getTime() - lastFireTime) >= (1.0f / curProjectile.getFiringRate()))
			&& (curProjectile.count < curProjectile.maxBullets);
	}

	public Bullet fire() {
		assert canFire();
		
		double oldLastFireTime = lastFireTime;
		System.out.println("last fire time: " + lastFireTime);		
		lastFireTime = SkunkJets.mainTimer.getTime();
		System.out.println("now: " + lastFireTime);
		System.out.println("difference: " + (lastFireTime - oldLastFireTime));
		
		float velX = (float) (curProjectile.getSpeed() * Math.cos(orientation * DEG_TO_RAD));
		float velY = (float) (curProjectile.getSpeed() * Math.sin(orientation * DEG_TO_RAD));

		Vector2f velocity = new Vector2f(velX, velY);
		//TODO fix start of bullet
		Bullet bullet = curProjectile.fire(new Vector2f(getPosition()), velocity);
		bullets.add(bullet);
		
		return bullet;
	}

	public void innerDraw(SkunkJets game)
	{
	   int angle = myTeam ? -90 : 90;
	   ImageLib.drawImage(img, 0, 0, orientation + angle, 128, 128);
	   
		/*GL11.glScalef(radius, radius, 1);

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
		GL11.glEnd();*/
	}
}
