import org.lwjgl.util.vector.Vector2f;

public class Bullet extends GameObject
{
	int img;
	
	public Bullet(Vector2f position, int img, boolean myTeam)
	{
		super(position, new Vector2f(0, 0), myTeam); // myTeam is setting visibility
		this.myTeam = myTeam;
		this.img = img;
		radius = 0.15;
	}
	
	void fire(Vector2f velocity) {
		this.setVelocity(velocity);
	}

	public void innerDraw() {
		ImageLib.drawImage(img, 0f, 0f, -90f, 32, 32);
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
