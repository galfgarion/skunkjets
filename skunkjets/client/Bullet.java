package skunkjets.client;
import org.lwjgl.util.vector.Vector2f;

public class Bullet extends GameObject
{
	public int img;
	public String type;
	
	public Bullet(Vector2f position, int img, boolean myTeam, int id, String type) {
		super(position, new Vector2f(0, 0), myTeam, id); // myTeam is setting visibility
		this.myTeam = myTeam;
		this.img = img;
		this.type = type;
		radius = 0.15;
	}
	
	void fire(Vector2f velocity) {
		this.setVelocity(velocity);
	}

	public void innerDraw() {
		ImageLib.drawImage(img, 0f, 0f, -90f, 50, 50);
	}

	public boolean update(double timeDelta)
	{
		// TODO Auto-generated method stub
		Vector2f moveDelta = new Vector2f(getVelocity());
		moveDelta.scale((float) timeDelta);
		Vector2f.add(getPosition(), moveDelta, getPosition());
      if (Math.abs(getPosition().x) > halfRangeX || Math.abs(getPosition().y) > halfRangeY) return true;
      return false;
	}
}
