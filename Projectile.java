
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public abstract class Projectile extends GameObject {
	private static float maxFiringRate = 1;
	protected float firingRate;
	protected int count;
	protected int img;
	ArrayList<Bullet> bullets;

	public Projectile(Vector2f position, Vector2f velocity)
	{
		super(position, velocity);
		count = 0;
	}

	// max rounds per second
	public float getFiringRate() {
		return firingRate;
	}
	
	public void innerDraw()
	{
			GL11.glColor3f(1.0f, 1.0f, 1.0f);

			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(-0.05, -0.05);
				GL11.glVertex2d(0.05, -0.05);
				GL11.glVertex2d(0.05, 0.05);
				GL11.glVertex2d(-0.05, 0.05);
			}
			GL11.glEnd();
		
	}

	// max rounds per second
	public float maxFiringRate() {
		return maxFiringRate;
	}
}
