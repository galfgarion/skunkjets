import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Jet {
	
	public Vector2f position;
	public Vector2f velocity;
	
	public Jet() {
		position = new Vector2f(0.0f, 0.0f);
	}
	
	public Jet(float x, float y) {
		position = new Vector2f(x, y);
		velocity = new Vector2f(0.0f, 0.01f);
	}
	
	public Jet(Vector2f position, Vector2f velocity) {
		this.position = position;
		this.velocity = velocity;
	}
	
	public void update() {
		Vector2f.add(position, velocity, position);
	}
	
	public void draw() {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(position.x, position.y, 0);
			//GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(-0.05, -0.05);
				GL11.glVertex2d(0.05, -0.05);
				GL11.glVertex2d(0.05, 0.05);
				GL11.glVertex2d(-0.05, 0.05);
				//System.err.println("at pixel " + (-50 + position.x) + "," + (-50 + position.y));
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
	}

}
