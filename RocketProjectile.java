
public class RocketProjectile extends ProjectileType
{
	public RocketProjectile(boolean myTeam)
	{
		super(myTeam);
		firingRate = 2.5f;
		speed = 0.5f;
		img = ImageLib.getImage("Images/turret's missile.png");
		maxBullets = 10;
	}
}
