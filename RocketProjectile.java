import org.lwjgl.util.vector.Vector2f;


public class RocketProjectile extends ProjectileType
{

	public RocketProjectile(Vector2f position, Vector2f velocity)
	{
		firingRate = 0.5f;
		speed = 0.5f;
		img = ImageLib.getImage("Images/missile.png");
		maxBullets = 10;
	}

	public void innerDraw() {
		
	}
	
}
