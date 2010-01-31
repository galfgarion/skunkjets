
public class BeamProjectile extends ProjectileType
{
	public BeamProjectile(boolean myTeam)
	{
		super(myTeam);
		firingRate = 2.5f;
		speed = 1.0f;
		radius = 0.015f;
		img = ImageLib.getImage("Images/beam.png");
		maxBullets = 10;
	}
}
