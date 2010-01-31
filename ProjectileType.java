
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

public abstract class ProjectileType
{
	protected float firingRate;
	protected float speed;
	
	protected int img;

	protected int count;
	protected int maxBullets;

	public ProjectileType()
	{
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

	public Bullet fire(Vector2f position, Vector2f velocity)
	{
		Bullet bullet = new Bullet(position, img);
		bullet.fire(velocity);
		return bullet;
	}
}
