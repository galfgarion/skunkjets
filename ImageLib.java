import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;

public class ImageLib {
   
   // Image library
   public static final int SIZE_FLOAT = 4;
   public static final int SIZE_INT = 4;
   static Hashtable OpenGLextensions;
   
   static Hashtable<Integer,GLImage> Images = new Hashtable<Integer,GLImage>();
   static int keyValCurrent;
   
   // This class is to present a small level of abstraction between the
   // game management code and the display of each 2D sprite
   public static int getImage(String filename)
   {
      GLImage img = loadImage(filename);
      Images.put(new Integer(keyValCurrent++), img);
      return keyValCurrent - 1;
   }
   
   public static int drawImage(int imgVal, float x, float y, float angle)
   {
      Integer imgKey = new Integer(imgVal);
      GLImage img;
      if (!Images.containsKey(imgKey)) return -1;
      
      img = (GLImage) Images.get(imgKey);
      float width = ((float)img.w) * 2 /Display.getDisplayMode().getHeight();
      float height = ((float)img.h * 2 /Display.getDisplayMode().getHeight());
      drawImage(img, x, y, width, height, angle);
      return 0;
   }
   
   // BAD METHOD***
   public static int drawImage(int imgVal, float x, float y, float angle, int w, int h)
   {
      Integer imgKey = new Integer(imgVal);
      GLImage img;
      if (!Images.containsKey(imgKey)) return -1;
      
      img = (GLImage) Images.get(imgKey);
      drawImage(img, x, y, w, h, angle);
      return 0;
   }
   
   /*This Code courtesy of potatolang.org
    * Full link: http://potatoland.org/code/gl/ */
   public static GLImage loadImage(String imgFilename) {
      GLImage img = new GLImage(imgFilename);
      if (img.isLoaded()) {
          return img;
      }
      return null;
  }
   
   private static void drawImage(GLImage img, float x, float y, float w, float h, float r) {
      // if image has no texture, convert the image to a texture
      if (img.textureHandle <= 0) {
         img.textureHandle = makeTexture(img);
      }
      // preserve settings
      //pushAttribOrtho();
      GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TEXTURE_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
      // set color to white
      //GL11.glColor4f(1,1,1,1);   // don't force color to white (may want to tint image)
        // activate the image texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,img.textureHandle);
        // draw a textured quad
        GL11.glPushMatrix();
        {
           GL11.glNormal3f(0.0f, 0.0f, 1.0f); // normal faces positive Z
           GL11.glTranslatef((float)x, (float)y, 0);
           GL11.glRotatef(r, 0.0f, 0.0f, 1.0f);
           GL11.glBegin(GL11.GL_QUADS);
           {
              GL11.glTexCoord2f(0f, 0f);
              GL11.glVertex3f( (float)(-w/2), (float)(-h/2), (float)0);
              GL11.glTexCoord2f(1f, 0f);
              GL11.glVertex3f( (float)(w/2), (float)(-h/2), (float)0);
              GL11.glTexCoord2f(1f, 1f);
              GL11.glVertex3f( (float)(w/2), (float)(h/2), (float)0);
              GL11.glTexCoord2f(0f, 1f);
              GL11.glVertex3f( (float)(-w/2), (float)(h/2), (float)0);
           }
           GL11.glEnd();
        }
        GL11.glPopMatrix();
        // return to previous settings
        //popAttrib();
        GL11.glPopAttrib();
    }
   
   private static int makeTexture(GLImage textureImg)
   {
       if ( textureImg != null ) {
           if (isPowerOf2(textureImg.w) && isPowerOf2(textureImg.h)) {
               return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
           }
           else {
               System.err.println("GLApp.makeTexture(GLImage) Warning: not a power of two: " + textureImg.w + "," + textureImg.h);
               textureImg.convertToPowerOf2();
               return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
           }
       }
       return 0;
   }
   
   private static boolean isPowerOf2(int n) {
      if (n == 0) { return false; }
        return (n & (n - 1)) == 0;
    }
   
   private static int makeTexture(ByteBuffer pixels, int w, int h, boolean anisotropic)
   {
       // get a new empty texture
       int textureHandle = allocateTexture();
       // preserve currently bound texture, so glBindTexture() below won't affect anything)
       GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
       // 'select' the new texture by it's handle
       GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
       // set texture parameters
       GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
       GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
       GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
       GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);

       // make texture "anisotropic" so it will minify more gracefully
     if (anisotropic && extensionExists("GL_EXT_texture_filter_anisotropic")) {
        // Due to LWJGL buffer check, you can't use smaller sized buffers (min_size = 16 for glGetFloat()).
        FloatBuffer max_a = allocFloats(16);
        // Grab the maximum anisotropic filter.
        GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max_a);
        // Set up the anisotropic filter.
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, max_a.get(0));
     }

       // Create the texture from pixels
       GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
           0,                   // level of detail
           GL11.GL_RGBA8,       // internal format for texture is RGB with Alpha
           w, h,                // size of texture image
           0,                // no border
           GL11.GL_RGBA,        // incoming pixel format: 4 bytes in RGBA order
           GL11.GL_UNSIGNED_BYTE,  // incoming pixel data type: unsigned bytes
           pixels);          // incoming pixels

       // restore previous texture settings
       GL11.glPopAttrib();

       return textureHandle;
   }
   
   private static int allocateTexture()
   {
       IntBuffer textureHandle = allocInts(1);
       GL11.glGenTextures(textureHandle);
       return textureHandle.get(0);
   }
   
   private static IntBuffer allocInts(int howmany) {
      return ByteBuffer.allocateDirect(howmany * SIZE_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
    }
   
   private static FloatBuffer allocFloats(int howmany) {
      return ByteBuffer.allocateDirect(howmany * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
   
   private static boolean extensionExists(String extensionName) {
      if (OpenGLextensions == null) {
         String[] GLExtensions = GL11.glGetString(GL11.GL_EXTENSIONS).split(" ");
         OpenGLextensions = new Hashtable();
         for (int i=0; i < GLExtensions.length; i++) {
            OpenGLextensions.put(GLExtensions[i].toUpperCase(),"");
         }
      }
      return (OpenGLextensions.get(extensionName.toUpperCase()) != null);
    }
}
