
public class BeamProjectile extends ProjectileType
{
	public BeamProjectile()
	{
		firingRate = 2.5f;
		speed = 1.0f;
		img = ImageLib.getImage("Images/beam.png");
		maxBullets = 10;
	}
}
