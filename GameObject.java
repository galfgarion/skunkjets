import org.lwjgl.util.vector.Vector2f;

public abstract class GameObject {

	public Vector2f position;
	public Vector2f velocity;

	// Override this
	public void update(double timeDelta) {
		Vector2f moveDelta = new Vector2f(velocity);
		moveDelta.scale((float)timeDelta);
		Vector2f.add(position, moveDelta, position);
	}
	
	public abstract void draw();
}
