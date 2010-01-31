import org.lwjgl.util.vector.Vector2f;


public class RocketProjectile extends Projectile {
	public RocketProjectile(Vector2f position, Vector2f velocity) {
		super(position, velocity);
		firingRate = 0.5f;
		//img = ;
	}
}
