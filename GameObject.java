import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public abstract class GameObject {
	
	private static final boolean DEBUG = false;
	
	private Vector2f position;
	private Vector2f velocity;
	public int sprite;
	
	protected double radius; // for bounding sphere collision

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
	
	public boolean collide(GameObject other) {
		Vector2f distvec = new Vector2f();
		
		if(DEBUG) {
			boolean flag = false;
			
			if(other == null) {
				System.err.println("Other class is null");
				flag = true;
			}
			if(this.position == null) {
				System.err.println(this.getClass() + " position is null");
				flag = true;
			}
			if(other.position == null) {
				System.err.println(other.getClass() + " position is null");
				flag = true;
			}
			if(flag) return false;
		}
		
		if(other == null || this.position == null || other.position == null)
			return false;
		
		Vector2f.sub(this.position, other.position, distvec);
		double dist = distvec.length();
		
		
		boolean collision = dist < this.radius + other.radius;
		if(collision && DEBUG)
			System.err.println("Collision!");
		
		return collision;
		
	}

	public void draw() {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(position.x, position.y, 0);

			float rotationRads = (float) Math.atan2(getVelocity().y, getVelocity().x);
			GL11.glRotatef((float) (rotationRads * 180 / Math.PI), 0, 0, 1);
			
			this.innerDraw();

		}
		GL11.glPopMatrix();
	}

	protected abstract void innerDraw();
}
