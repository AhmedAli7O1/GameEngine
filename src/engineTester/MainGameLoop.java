package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

  public static void main (String[] args) {

    DisplayManager.createDisplay();
    Loader loader = new Loader();

    //****************** Terrain Texture ********************

    TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
    TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
    TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
    TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

    TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
    TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

    //*******************************************************


    TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
    TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
    TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
    TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
    TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));

    grass.getTexture().setHasTransparency(true);
    grass.getTexture().setUseFakeLighting(true);
    flower.getTexture().setHasTransparency(true);
    flower.getTexture().setUseFakeLighting(true);
    fern.getTexture().setHasTransparency(true);

    Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

    List<Entity> entities = new ArrayList<Entity>();
    Random random = new Random(676452);

    for(int i = 0; i < 400; i++){
      if (i % 7 == 0) {

        float grassX = random.nextFloat() * 400 - 200;
        float grassZ = random.nextFloat() * -400;
        float grassY = terrain.getHeightOfTerrain(grassX, grassZ);

        entities.add(new Entity(grass, new Vector3f(grassX, grassY, grassZ),0,0,0,1.8f));

        float flowerX = random.nextFloat() * 400 - 200;
        float flowerZ = random.nextFloat() * - 400;
        float flowerY = terrain.getHeightOfTerrain(flowerX, flowerZ);

        entities.add(new Entity(flower, new Vector3f(flowerX, flowerY, flowerZ), 0,0,0,2.3f));

      }

      if (i % 3 == 0) {

        float treeX = random.nextFloat()*800 - 400;
        float treeZ = random.nextFloat() * - 600;
        float treeY = terrain.getHeightOfTerrain(treeX, treeZ);

        entities.add(new Entity(tree, new Vector3f(treeX, treeY, treeZ),0,0,0,random.nextFloat() * 1 + 4));

        float bobbleX = random.nextFloat() * 800 - 400;
        float bobbleZ = random.nextFloat() * - 600;
        float bobbleY = terrain.getHeightOfTerrain(bobbleX, bobbleZ);

        entities.add(new Entity(bobble, new Vector3f(bobbleX, bobbleY, bobbleZ), 0,random.nextFloat() * 360,0, random.nextFloat() * 0.1f + 0.6f));

        float fernX = random.nextFloat() * 400 - 200;
        float fernZ = random.nextFloat() * - 400;
        float fernY = terrain.getHeightOfTerrain(fernX, fernZ);

        entities.add(new Entity(fern, new Vector3f(fernX, fernY, fernZ), 0, random.nextFloat() * 360,0,0.9f));
      }
    }

    Light light = new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,1));

    MasterRenderer renderer = new MasterRenderer();

    TexturedModel bunny = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));

    Player player = new Player(bunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);

    Camera camera = new Camera(player);

    while(!Display.isCloseRequested()){
      camera.move();
      player.move(terrain);
      renderer.processEntity(player);
      renderer.processTerrain(terrain);
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
