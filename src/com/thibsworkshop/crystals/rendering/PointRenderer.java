package com.thibsworkshop.crystals.rendering;

import com.thibsworkshop.crystals.Main;
import com.thibsworkshop.crystals.RawModel;
import com.thibsworkshop.crystals.TerrainManager;
import com.thibsworkshop.crystals.Window;
import com.thibsworkshop.crystals.tools.Maths;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class PointRenderer extends Renderer {

    private final PointShader pointShader;
    private final OrthographicCamera orthographicCamera;




    public PointRenderer(PointShader pointShader, OrthographicCamera orthographicCamera){
        super(pointShader);
        this.pointShader = pointShader;
        this.orthographicCamera = orthographicCamera;

        pointShader.start();
        pointShader.loadCrystals();
        pointShader.stop();
    }


    // WARNING: Clearing depth buffer here
    @Override
    public void render() {
        pointShader.start();

        pointShader.loadRenderingVariables(orthographicCamera.getProjectionViewMatrix());
        Vector3f center = new Vector3f(Window.mainWindow.getWidth() / 2,Window.mainWindow.getHeight() / 2,-1);
        pointShader.loadScale(10.0f);
        pointShader.loadPosition(new Vector3f(0,0,-1));
        prepareModel(TerrainManager.model);
        GL11.glDrawArrays(GL11.GL_POINTS, 0,TerrainManager.model.getVertexCount());


        unbindModel();
        pointShader.stop();
    }





    private final Vector3f tempPos = new Vector3f();
    /*private void loadTransformation(GameObject object){
        tempPos.set(object.transform.getPosition());
        Chunk.shiftPositionFromCamera(tempPos, object.transform.chunkPos, Player.player.camera.transform.chunkPos);
        Matrix4f transformation = Maths.createTransformationMatrix(tempPos, object.transform.getScale());
        pointShader.loadTransformation(transformation);
    }*/

    private void prepareModel(RawModel rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }

}
