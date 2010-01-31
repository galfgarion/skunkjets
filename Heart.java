import org.lwjgl.opengl.GL11;


public class Heart {
	
	
	public final float OFFSET = 0.05f;
	public static final double WIDTH = 0.1;

	public void draw() {
	
			GL11.glTranslated(OFFSET + WIDTH, 0, 0);
	
			GL11.glColor3f(1.0f, 0, 0);

			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(-WIDTH/2, -WIDTH/2);
				GL11.glVertex2d(WIDTH/2, -WIDTH/2);
				GL11.glVertex2d(WIDTH/2, WIDTH/2);
				GL11.glVertex2d(-WIDTH/2, WIDTH/2);
			}
			GL11.glEnd();
			
	}
		

}
