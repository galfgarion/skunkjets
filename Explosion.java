import java.util.*;
import org.lwjgl.util.vector.Vector2f;

public class Explosion extends GameObject 
{

   int img = ImageLib.getImage("Images/flair explode.png");
   float delay;
   
   public Explosion(Vector2f position) {
      super(position, new Vector2f(0, 0));
   }
   
   @Override
   protected void innerDraw() {
      ImageLib.drawImage(img, getPosition().x, getPosition().y, 0.0f);
      // TODO Auto-generated method stub
   }
   
   @Override
   public boolean update(double timeDelta) {
      delay += timeDelta;
      return delay > 1f;
   }

}
