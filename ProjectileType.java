
import org.lwjgl.util.vector.Vector2f;

public abstract class ProjectileType
{
	protected float firingRate;
	protected float speed;
	
	protected int img;
	protected boolean myTeam;
	protected int count;
	protected int maxBullets;

	public ProjectileType(boolean myTeam)
	{
		count = 0;
		this.myTeam = myTeam;
	}

	// max rounds per second
	public float getFiringRate()
	{
		return firingRate;
	}
	
	public float getSpeed()
	{
		return speed;
	}

	public Bullet fire(Vector2f position, Vector2f velocity)
	{
		Bullet bullet = new Bullet(position, img, this.myTeam);
		bullet.fire(velocity);
		return bullet;
	}
}
