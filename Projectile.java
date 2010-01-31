import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

public abstract class Projectile
{
	protected Vector2f position;
	protected Vector2f velocity;
	
	protected float firingRate;
	protected float speed;
	
	protected int img;

	protected int count;
	protected int maxBullets;

	public Projectile(Vector2f position, Vector2f velocity)
	{
		this.position = position;
		this.velocity = velocity;
		count = 0;
	}

	public Vector2f getPosition()
	{
		return position;
	}

	public Vector2f getVelocity()
	{
		return velocity;
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
		Bullet bullet = new Bullet(position, img);
		bullet.fire(velocity);
		return bullet;
	}
}
