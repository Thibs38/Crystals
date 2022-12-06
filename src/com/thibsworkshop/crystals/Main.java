package com.thibsworkshop.crystals;

import com.thibsworkshop.crystals.rendering.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;


public class Main {


    private Window window;
    private static GLFWErrorCallback errorCallback;


    private void init() {

        if(!glfwInit()){
            System.err.println("GLFW Failed to initialize");
        }

        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        window = new Window(1280,720,false);
        //window = new Window(1920,1080,true);

        Time.init();
        Loader.init();
        CrystalType.init();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL32.GL_PROGRAM_POINT_SIZE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void loop() {

        CrystalType.types = JSONLoader.loadCrystalTypes();
        TerrainManager terrainManager = new TerrainManager();
        TerrainInteractor terrainInteractor = new TerrainInteractor(terrainManager);

        OrthographicCamera camera = new OrthographicCamera(1);
        Camera.main = camera;

        Input input = new Input(window);



        //TerrainManager terrainManager = new TerrainManager(terrainInfo);

        //GameObjectManager gameObjectManager = new GameObjectManager();

        MasterRenderer renderer = new MasterRenderer();

        PointShader pointShader = new PointShader();
        GradientShader gradientShader = new GradientShader();
        PointRenderer pointRenderer = new PointRenderer(pointShader,camera);
        GradientRenderer gradientRenderer = new GradientRenderer(gradientShader,camera);
        //pointRenderer.linkGameObjectManager(gameObjectManager);
        ArrayList<Renderer> renderers = new ArrayList<>();
        renderers.add(pointRenderer);

        //GameEntity chick = new GameEntity(chickModel,1);
        //chick.transform.setPosition(0,200,0);
        /*GameEntity chick1 = new GameEntity(chickModel,1);
        chick1.transform.setPosition(6,205,5);
        GameEntity chick2 = new GameEntity(chickModel,1);
        chick2.transform.setPosition(7,210,5);
        GameEntity chick3 = new GameEntity(chickModel,1);
        chick3.transform.setPosition(8,215,5);
        GameEntity chick4 = new GameEntity(chickModel,1);
        chick4.transform.setPosition(9,220,5);*/

        //gameObjectManager.processEntity(chick);

        //gameObjectManager.processEntity(player);
        //Timing.enable(TerrainManager.debugName);
        //Timing.enable(MasterRenderer.debugName);

        float wait = 0.5f;
        float time = Time.getTime() + wait;
        boolean done = false;

        boolean manualMode = false;
        //player.enableGravity(false);
        while ( !window.shouldWindowClose()) {
            Time.update();
            input.updateInput();
            terrainInteractor.update();
            if(Input.isKeyDown(GLFW_KEY_F2)){
                if(manualMode){
                    manualMode = false;
                    System.out.println("Disabled manual mode");
                }else{
                    manualMode = true;
                    System.out.println("Enabled manual mode");
                }
            }
            if(manualMode){
                if(Input.isKeyDown(GLFW_KEY_SPACE)){
                    terrainManager.update();
                }
            }else{
                terrainManager.update();
            }
            /*if(!done && Time.getTime() > time) {
                if(player.getMode() == Player.Mode.Survival)
                    player.enableGravity(true);
                Binary.readVoxFile("Program/res/data/vox/island.vox");
                done = true;
            }

            if(Input.isKeyDown(GLFW_KEY_ENTER)){
                Debug.printVector(player.transform.chunkPos);
            }

            player.move();
            //Debug.printVector(player.transform.getPosition());
            //Game Logic

            //Debug.printVector(player.transform.forward());

            if(Input.isKeyHold(GLFW_KEY_UP)){
                sun.rotate(new Vector3f(0.025f,0,0));
            }
            if(Input.isKeyHold(GLFW_KEY_DOWN))
                sun.rotate(new Vector3f(-0.025f,0,0));

            //System.out.println(camera.getViewMatrix());

            //if(Input.isKeyDown(GLFW_KEY_ENTER))
            //Timing.print(MasterRenderer.debugName,"Terrain",5);
            //Timing.print(TerrainManager.debugName,"Refreshing",5);

            gameObjectManager.update();
            if(player.getMode() == Player.Mode.Survival)
                camera.update(player.transform,1.5f);
            else if (player.getMode() == Player.Mode.Spectator)
                camera.update(camera.transform,0);
            terrainManager.refreshChunks();*/

            if(Input.isKeyDown(GLFW_KEY_F1)){
                if(renderers.contains(gradientRenderer)) {
                    renderers.remove(gradientRenderer);
                    System.out.println("Disabled temperature map");
                }else{
                    renderers.add(gradientRenderer);
                    System.out.println("Enabled temperature map");
                }
            }

            renderer.render(renderers);
            window.updateWindow();
        }

        for(Renderer r : renderers)
            r.cleanUp();
        //terrainManager.cleanUp();
    }

    public void end(){

        window.closeWindow();
        Loader.cleanUp();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void run() {

        init();
        loop();
        end();

    }

    public static void main(String[] args) {
        new Main().run();
    }
}
