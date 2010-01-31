import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Jet extends GameObject {
	
	int img;
	boolean myTeam;
	
	public Jet(Vector2f position, Vector2f velocity, boolean myTeam) {
		super(position, velocity);
		this.myTeam = myTeam;
		if (myTeam)
		{
			img = ImageLib.getImage("Images/Jet blue.png");
		}
		else
		{
			img = ImageLib.getImage("Images/Jet red.png");
		}
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
		float angle = myTeam ? 0 : 180;
		ImageLib.drawImage(img, getPosition().x, getPosition().y, angle);
	}

}
