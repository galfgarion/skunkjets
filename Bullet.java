import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Bullet extends GameObject {
	private static float maxFiringRate = 1;
	
	public Bullet(Vector2f position, Vector2f velocity) {
		super(position, velocity);
	}
	
	void fire(Vector2f velocity) {
		this.setVelocity(velocity);
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
				// System.err.println("at pixel " + (-50 + position.x) + "," +
				// (-50 + position.y));
			}
			GL11.glEnd();
	}


	public void update(double timeDelta)
	{
		// TODO Auto-generated method stub
		Vector2f moveDelta = new Vector2f(getVelocity());
		moveDelta.scale((float) timeDelta);
		Vector2f.add(getPosition(), moveDelta, getPosition());
	}
}
