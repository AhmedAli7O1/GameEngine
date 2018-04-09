package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

  public static void main (String[] args) {

    DisplayManager.createDisplay();
    Loader loader = new Loader();


    RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
    RawModel lowpolyTreeModel = OBJLoader.loadObjModel("lowPolyTree", loader);
    RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
    RawModel fernModel = OBJLoader.loadObjModel("fern", loader);

    TexturedModel tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
    TexturedModel lowpolyTree = new TexturedModel(lowpolyTreeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));
    TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
    grass.getTexture().setHasTransparency(true);
    grass.getTexture().setUseFakeLighting(true);
    TexturedModel fern = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("fern")));
    fern.getTexture().setHasTransparency(true);
    fern.getTexture().setUseFakeLighting(true);

    List<Entity> entities = new ArrayList<Entity>();
    Random random = new Random();
    for(int i=0;i<500;i++){
      entities.add(new Entity(tree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
      entities.add(new Entity(lowpolyTree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,0.3f));
      entities.add(new Entity(grass, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
      entities.add(new Entity(fern, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,0.6f));
    }

    Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));

    Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
    Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")));

    Camera camera = new Camera();
    MasterRenderer renderer = new MasterRenderer();

    while(!Display.isCloseRequested()){
      camera.move();

      renderer.processTerrain(terrain);
      renderer.processTerrain(terrain2);
      for(Entity entity:entities){
        renderer.processEntity(entity);
      }
      renderer.render(light, camera);
      DisplayManager.updateDisplay();
    }

    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();

  }

}
