package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
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


    TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("pine", loader), new ModelTexture(loader.loadTexture("pine")));
    TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
    TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
//    TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
    TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
    TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));


    grass.getTexture().setHasTransparency(true);
    grass.getTexture().setUseFakeLighting(true);
    flower.getTexture().setHasTransparency(true);
    flower.getTexture().setUseFakeLighting(true);
    lamp.getTexture().setUseFakeLighting(true);
    fern.getTexture().setHasTransparency(true);
    fern.getTexture().setNumberOfRows(2);

    Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

    List<Entity> entities = new ArrayList<Entity>();
    Random random = new Random(676452);

    for(int i = 0; i < 400; i++){
      if (i % 7 == 0) {

//        float grassX = random.nextFloat() * 400 - 200;
//        float grassZ = random.nextFloat() * - 400;
//        float grassY = terrain.getHeightOfTerrain(grassX, grassZ);
//
//        entities.add(new Entity(grass, new Vector3f(grassX, grassY, grassZ), 0, 0,0,1.8f));
//
//        float flowerX = random.nextFloat() * 400 - 200;
//        float flowerZ = random.nextFloat() * - 400;
//        float flowerY = terrain.getHeightOfTerrain(flowerX, flowerZ);
//
//        entities.add(new Entity(flower, new Vector3f(flowerX, flowerY, flowerZ), 0,0,0,2.3f));

      }

      if (i % 3 == 0) {
        float treeX = random.nextFloat()* 800 - 400;
        float treeZ = random.nextFloat() * - 600;
        float treeY = terrain.getHeightOfTerrain(treeX, treeZ);

        entities.add(new Entity(tree, new Vector3f(treeX, treeY, treeZ),0,0,0,1.5f));

//        float bobbleX = random.nextFloat() * 800 - 400;
//        float bobbleZ = random.nextFloat() * - 600;
//        float bobbleY = terrain.getHeightOfTerrain(bobbleX, bobbleZ);
//
//        entities.add(new Entity(bobble, new Vector3f(bobbleX, bobbleY, bobbleZ), 0,random.nextFloat() * 360,0, random.nextFloat() * 0.1f + 0.6f));

        float fernX = random.nextFloat() * 400 - 200;
        float fernZ = random.nextFloat() * - 400;
        float fernY = terrain.getHeightOfTerrain(fernX, fernZ);

        entities.add(new Entity(fern, random.nextInt(4), new Vector3f(fernX, fernY, fernZ), 0, random.nextFloat() * 360,0,0.9f));
      }
    }

    List<Light> lights = new ArrayList<>();
    lights.add(new Light(new Vector3f(0,1000,-7000), new Vector3f(0.6f,0.6f,0.6f)));
    lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
    lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
    lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));

    float lampx = 185;
    float lampz = -293;
    float lampy = terrain.getHeightOfTerrain(lampx, lampz);

    entities.add(new Entity(lamp, new Vector3f(lampx, lampy, lampz), 0, 0, 0, 1));

    lampx = 370;
    lampz = -300;
    lampy = terrain.getHeightOfTerrain(lampx, lampz);

    entities.add(new Entity(lamp, new Vector3f(lampx, lampy, lampz), 0, 0, 0, 1));

    lampx = 293;
    lampz = -305;
    lampy = terrain.getHeightOfTerrain(lampx, lampz);

    entities.add(new Entity(lamp, new Vector3f(lampx, lampy, lampz), 0, 0, 0, 1));

    MasterRenderer renderer = new MasterRenderer(loader);

    TexturedModel bunny = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));

    Player player = new Player(bunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);

    Camera camera = new Camera(player);

    List<GuiTexture> guis = new ArrayList<>();
    GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.7f, 0.85f), new Vector2f(0.25f, 0.25f));
    guis.add(gui);

    GuiRenderer guiRenderer = new GuiRenderer(loader);

    while(!Display.isCloseRequested()){
      camera.move();
      player.move(terrain);
      renderer.processEntity(player);
      renderer.processTerrain(terrain);
      for(Entity entity:entities){
        renderer.processEntity(entity);
      }
      renderer.render(lights, camera);
      guiRenderer.render(guis);
      DisplayManager.updateDisplay();
    }

    guiRenderer.cleanUp();
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();

  }

}
