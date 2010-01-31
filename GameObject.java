import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public abstract class GameObject {
   public float halfRangeX = (2/3f) + 0.0001f;
   public float halfRangeY = (1.0f) + 0.0001f;
	
	private static final boolean DEBUG = false;
	
	private Vector2f position;
	private Vector2f velocity;
	public int sprite;
	
	protected double radius; // for bounding sphere collision
	public double visibilityRadius; // for shroud testing
	protected boolean visible; // whether or not the object should be seen
	protected boolean myTeam;
	
	public GameObject(Vector2f position, Vector2f velocity, boolean vis) {
		assert position != null;
		assert velocity != null;
		this.position = position;
		this.velocity = velocity;
		this.visible  = vis;
		this.myTeam = false; // default to false (some objects are not owned, e.g. Tornadoes)
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
	// Note: true means out of bounds, false means still good
	public boolean update(double timeDelta) {
		Vector2f moveDelta = new Vector2f(velocity);
		moveDelta.scale((float) timeDelta);
		Vector2f.add(position, moveDelta, position);
		if (Math.abs(position.x) > halfRangeX || Math.abs(position.y) > halfRangeY) return true;
		return false;
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

	public boolean isVisible(GameObject other)
	{
		Vector2f distvec = new Vector2f();
		
		if(other == null || this.position == null || other.position == null)
			return false;
		
		Vector2f.sub(this.position, other.position, distvec);
		double dist = distvec.length();
		
		
		boolean collision = dist < this.visibilityRadius + other.visibilityRadius;
		if(collision)
			System.err.println("I see you!!!");
		
		return collision;
		
	}	

   protected static void drawCircle() {
      final int numVertices = 60;

      Vector2f vertices[] = new Vector2f[numVertices];

      for (int i = 0; i < numVertices; i++) {
         vertices[i] = new Vector2f();
         vertices[i].x = (float) Math.cos(i * 2 * Math.PI / numVertices);
         vertices[i].y = (float) Math.sin(i * 2 * Math.PI / numVertices);
      }

      GL11.glBegin(GL11.GL_TRIANGLE_FAN);
      for (int i = 0; i < numVertices; i++) {
         GL11.glVertex2f(vertices[i].x, vertices[i].y);
      }
      GL11.glEnd();
   }

	public void draw(SkunkJets game) {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(position.x, position.y, 0);

			float rotationRads = (float) Math.atan2(getVelocity().y, getVelocity().x);
			GL11.glRotatef((float) (rotationRads * 180 / Math.PI), 0, 0, 1);
			
			this.innerDraw(game);

		}
		GL11.glPopMatrix();
	}

	protected abstract void innerDraw(SkunkJets game);
}
