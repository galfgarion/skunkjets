import org.lwjgl.util.vector.Vector2f;

public abstract class GameObject {

	public Vector2f position;
	public Vector2f velocity;
	
	public abstract void update(double timeDelta);
	public abstract void draw();
}
