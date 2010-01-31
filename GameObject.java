import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public abstract class GameObject {
	private Vector2f position;
	private Vector2f velocity;
	public int sprite;

	public GameObject(Vector2f position, Vector2f velocity) {
		assert position != null;
		assert velocity != null;
		this.position = position;
		this.velocity = velocity;
	}

	public void setVelocity(Vector2f velocity) {
		assert (velocity != null);
		this.velocity = velocity;
	}

	public Vector2f getPosition() {
		return this.position;
	}

	public Vector2f getVelocity() {
		return this.velocity;
	}

	// Override this
	public void update(double timeDelta) {
		Vector2f moveDelta = new Vector2f(velocity);
		moveDelta.scale((float) timeDelta);
		Vector2f.add(position, moveDelta, position);
	}

	public void draw() {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(position.x, position.y, 0);

			float rotationRads = (float) Math.atan2(getVelocity().y, getVelocity().x);
			GL11.glRotatef((float) (rotationRads * 180 / Math.PI), 0, 0, 1);

			/*
			 * float distance = 1 - (position.y + 1) / 2; distance =
			 * (float)Math.sqrt(distance); GL11.glScalef(distance, distance, 1);
			 */
			// GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			this.innerDraw();

		}
		GL11.glPopMatrix();
	}

	protected abstract void innerDraw();
}
