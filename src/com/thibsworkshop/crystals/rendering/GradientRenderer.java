package com.thibsworkshop.crystals.rendering;

import com.thibsworkshop.crystals.RawModel;
import com.thibsworkshop.crystals.TerrainManager;
import com.thibsworkshop.crystals.Window;
import com.thibsworkshop.crystals.tools.Color;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;


public class GradientRenderer extends Renderer {

    private final GradientShader gradientShader;
    private final OrthographicCamera orthographicCamera;




    public GradientRenderer(GradientShader gradientShader, OrthographicCamera orthographicCamera){
        super(gradientShader);
        this.gradientShader = gradientShader;
        this.orthographicCamera = orthographicCamera;

        gradientShader.start();
        gradientShader.loadGradients(new Vector3f(0,0,1),new Vector3f(1,0,0),20,700);
        gradientShader.stop();
    }


    // WARNING: Clearing depth buffer here
    @Override
    public void render() {
        //glClear(GL_DEPTH_BUFFER_BIT);
        gradientShader.start();

        gradientShader.loadRenderingVariables(orthographicCamera.getProjectionViewMatrix());
        gradientShader.loadScale(10.0f);
        gradientShader.loadPosition(new Vector3f(0,0,-0.5f));
        prepareModel(TerrainManager.temperatureModel);
        GL11.glDrawArrays(GL11.GL_POINTS, 0,TerrainManager.model.getVertexCount());

        unbindModel();
        gradientShader.stop();
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
