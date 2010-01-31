package skunkjets.client;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;


public class HealthBar {
	
	public Vector2f position = new Vector2f(0, 0);
	
	private static final int MAX_HEARTS = 5;
	ArrayList<Heart> hearts = new ArrayList<Heart>();
	
	public HealthBar() {
		for(int i=0; i < MAX_HEARTS; i++) {
			hearts.add(new Heart());
		}
	}
	
	public void draw() {
		GL11.glPushMatrix();
			GL11.glTranslated(-1, -1 + Heart.WIDTH, 0);
			for(Heart heart: hearts) {
				heart.draw();
			}
		GL11.glPopMatrix();	
	}
	
	public int increase() {
		if(hearts.size() < MAX_HEARTS) {
			hearts.add(new Heart());
		}
		return hearts.size();
	}
	
	public int decrease() {
		if(hearts.size() > 0) {
			hearts.remove(hearts.size() - 1);
		}
		
		return hearts.size();
	}

}
