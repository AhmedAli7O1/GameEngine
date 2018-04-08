package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    public static void createDisplay() {
      try {

        ContextAttribs attribs = new ContextAttribs(3, 2);
        attribs.withForwardCompatible(true);
        attribs.withProfileCore(true);

        Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
        Display.create(new PixelFormat(), attribs);
        Display.setTitle("First Display");

        GL11.glViewport(0, 0, WIDTH, HEIGHT);

      } catch (LWJGLException e) {
        e.printStackTrace();
      }
    }

    public static void updateDisplay() {
      Display.sync(FPS_CAP);
      Display.update();
    }

    public static void closeDisplay() {
      Display.destroy();
    }

}
