import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

//test
public class Bullet extends GameObject
{
	private Vector2f position;
	private Vector2f velocity;
	
	
	public Bullet(Vector2f position)
	{
		this.position = position;
		this.velocity = new Vector2f(0, 0);
	}
	
	void fire(Vector2f velocity)
	{
		this.velocity = velocity;
	}

	public void draw()
	{
		// TODO Auto-generated method stub
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(position.x, position.y, 0);
			// GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);

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
		GL11.glPopMatrix();
	}


	public void update(double timeDelta)
	{
		// TODO Auto-generated method stub
		Vector2f moveDelta = new Vector2f(velocity);
		moveDelta.scale((float) timeDelta);
		Vector2f.add(position, moveDelta, position);
	}
}
