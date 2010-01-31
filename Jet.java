import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Jet extends GameObject {
	
	public Jet(Vector2f position, Vector2f velocity) {
		this.position = position;
		this.velocity = velocity;
	}
	
	@Override
	public void update(double timeDelta) {
		Vector2f moveDelta = new Vector2f(velocity);
		moveDelta.scale((float)timeDelta);
		Vector2f.add(position, moveDelta, position);
	}
	
	public void speedUp() {
		this.velocity.scale(70/69f);
		float speed = this.velocity.length();
		if (speed >= .5f) {
			this.velocity.x *= .5f / speed;
			this.velocity.y *= .5f / speed;
		}
	}
	
	public void slowDown() {
		this.velocity.scale(69/70f);
		float speed = this.velocity.length();
		if (speed < .1f) {
			this.velocity.x *= .1f / speed;
			this.velocity.y *= .1f / speed;
		}
	}
	
	public void turnLeft() {
		float distance = this.velocity.length();
		float angleRads = (float)Math.atan2(this.velocity.y, this.velocity.x);
		angleRads += 2f / 180 * Math.PI;
		this.velocity.x = (float)Math.cos(angleRads) * distance;
		this.velocity.y = (float)Math.sin(angleRads) * distance;
	}
	
	public void turnRight() {
		float distance = this.velocity.length();
		float angleRads = (float)Math.atan2(this.velocity.y, this.velocity.x);
		angleRads -= 2f / 180 * Math.PI;
		this.velocity.x = (float)Math.cos(angleRads) * distance;
		this.velocity.y = (float)Math.sin(angleRads) * distance;
	}
	
	@Override
	public void draw() {
		System.out.println("drawing jet");
		
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
