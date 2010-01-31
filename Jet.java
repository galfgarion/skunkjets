import java.util.Vector;

// lol

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Jet extends GameObject {
	
	public Jet(Vector2f position, Vector2f velocity) {
		super(position, velocity);
	}
	
	@Override
	public void update(double timeDelta) {
		Vector2f moveDelta = new Vector2f(getVelocity());
		moveDelta.scale((float)timeDelta);
		Vector2f.add(getPosition(), moveDelta, getPosition());
	}
	
	public void speedUp() {
		this.getVelocity().scale(70/69f);
		float speed = this.getVelocity().length();
		if (speed >= .5f) {
			this.getVelocity().x *= .5f / speed;
			this.getVelocity().y *= .5f / speed;
		}
	}
	
	public void slowDown() {
		this.getVelocity().scale(69/70f);
		float speed = this.getVelocity().length();
		if (speed < .1f) {
			this.getVelocity().x *= .1f / speed;
			this.getVelocity().y *= .1f / speed;
		}
	}
	
	public void turnLeft() {
		float distance = this.getVelocity().length();
		float angleRads = (float)Math.atan2(this.getVelocity().y, this.getVelocity().x);
		angleRads += 2f / 180 * Math.PI;
		this.getVelocity().x = (float)Math.cos(angleRads) * distance;
		this.getVelocity().y = (float)Math.sin(angleRads) * distance;
	}
	
	public void turnRight() {
		float distance = this.getVelocity().length();
		float angleRads = (float)Math.atan2(this.getVelocity().y, this.getVelocity().x);
		angleRads -= 2f / 180 * Math.PI;
		this.getVelocity().x = (float)Math.cos(angleRads) * distance;
		this.getVelocity().y = (float)Math.sin(angleRads) * distance;
	}
	
	@Override
	public void innerDraw() {
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

}
