import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

//test
public class Bullet extends GameObject
{
	private Vector2f position;
	private Vector2f velocity;
	int img;
	
	public Bullet(Vector2f position, int img)
	{
		this.position = position;
		this.img = img;
		this.velocity = new Vector2f(0, 0);
	}

	public static float w2p_x(float x)
	{
		return (float) (Display.getDisplayMode().getWidth()/2 * x);
	}
	
	public static float w2p_y(float y)
	{
		return (float) (Display.getDisplayMode().getHeight()/2 * y);// + Display.getDisplayMode().getHeight());
	}
	
	void fire(Vector2f velocity)
	{
		this.velocity = velocity;
	}

	public void draw()
	{
		//ImageLib.drawImage(img, w2p_x(position.x), w2p_y(position.y), (float)Math.atan2(velocity.y, velocity.x));
		ImageLib.drawImage(img, position.x, position.y, (float)Math.atan2(velocity.y, velocity.x));
		//ImageLib.drawImage(img, Display.getDisplayMode().getWidth()/2, 0, (float)Math.atan2(velocity.y, velocity.x));
		 /* GL11.glPushMatrix();
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
		 }
		 GL11.glEnd();
		 }
		 GL11.glPopMatrix(); */
	}

	public void update(double timeDelta)
	{
		// TODO Auto-generated method stub
		Vector2f moveDelta = new Vector2f(velocity);
		moveDelta.scale((float) timeDelta);
		Vector2f.add(position, moveDelta, position);
	}
}
