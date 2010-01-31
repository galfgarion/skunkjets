package skunkjets.client;

import org.lwjgl.util.vector.Vector2f;

public abstract class ProjectileType
{
	protected float firingRate;
	protected float speed;
	
	protected int img;
	protected int count;
	protected int maxBullets;

	public ProjectileType() {
		count = 0;
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

	public Bullet fire(Vector2f position, Vector2f velocity, boolean myTeam, int id, String type)
	{
		Bullet bullet = new Bullet(position, img, myTeam, id, type);
		bullet.fire(velocity);
		return bullet;
	}
}
