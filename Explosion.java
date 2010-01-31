import java.util.*;
import org.lwjgl.util.vector.Vector2f;

public class Explosion extends GameObject 
{
   static int[] img = null;
   int curImg = -1;
   float delay;
   float lifeSpan = 0.4f;
   int index = 0;
   
   public Explosion(Vector2f position) {
      super(position, new Vector2f(0, 0), true); // Explosions always visible
      if (img == null)
      {
         img = new int[8];
         img[0] = ImageLib.getImage("Images/ExplSeq/explosion1.png");
         img[1] = ImageLib.getImage("Images/ExplSeq/explosion2.png");
         img[2] = ImageLib.getImage("Images/ExplSeq/explosion3.png");
         img[3] = ImageLib.getImage("Images/ExplSeq/explosion4.png");
         img[4] = ImageLib.getImage("Images/ExplSeq/explosion5.png");
         img[5] = ImageLib.getImage("Images/ExplSeq/explosion6.png");
         img[6] = ImageLib.getImage("Images/ExplSeq/explosion7.png");
         img[7] = ImageLib.getImage("Images/ExplSeq/explosion8.png");
         curImg = img[0];
      }
   }
   
   @Override
   protected void innerDraw(SkunkJets game) {
      ImageLib.drawImage(curImg, 0.0f, 0.0f, 0.0f);
      // TODO Auto-generated method stub
   }
   
   @Override
   public boolean update(double timeDelta) {
      delay += timeDelta;
      if (delay > lifeSpan*((index+1)/8.0))
      {	  
    	  curImg = img[index++];
      }
      return delay > lifeSpan;
   }

}
