
public class RocketProjectile extends ProjectileType
{
	public RocketProjectile(boolean myTeam)
	{
		super(myTeam);
		firingRate = 0.5f;
		speed = 0.5f;
		radius = 0.045f;
		img = ImageLib.getImage("Images/turret's missile.png");
		maxBullets = 10;
	}
}
