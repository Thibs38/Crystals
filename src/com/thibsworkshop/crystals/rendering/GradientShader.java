package com.thibsworkshop.crystals.rendering;

import com.thibsworkshop.crystals.CrystalType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GradientShader extends ShaderProgram {

    private int location_projectionViewMatrix;
    private int location_scale;
    private int location_position;
    private int location_gradientMin;
    private int location_gradientValueMin;
    private int location_gradientMax;
    private int location_gradientValueMax;

    private static final String VERTEX_FILE = "gradientVertexShader.vert";
    private static final String FRAGMENT_FILE = "gradientFragmentShader.frag";


    public GradientShader() {
        super(VERTEX_FILE, FRAGMENT_FILE,2);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionViewMatrix = super.getUniformLocation("uProjectionViewMatrix");
        location_scale = super.getUniformLocation("uScale");
        location_position = super.getUniformLocation("uPosition");

        location_gradientMin = super.getUniformLocation("uGradientMin");
        location_gradientMax = super.getUniformLocation("uGradientMax");
        location_gradientValueMin = super.getUniformLocation("uGradientValueMin");
        location_gradientValueMax = super.getUniformLocation("uGradientValueMax");
    }

    @Override
    protected void bindAttributes() {

        super.bindAttribute(0, "position");
        super.bindAttribute(1,"crystal");
    }

    public void loadGradients(Vector3f c1, Vector3f c2, int v1, int v2) {
        super.loadVector(location_gradientValueMin,c1);
        super.loadVector(location_gradientValueMax,c2);
        super.loadInteger(location_gradientMin,v1);
        super.loadInteger(location_gradientMax,v2);
    }

    public void loadRenderingVariables(Matrix4f projectionView){
        super.loadMatrix(location_projectionViewMatrix,projectionView);
    }

    public void loadScale(float scale){
        super.loadFloat(location_scale, scale);
    }
    public void loadPosition(Vector3f position){
        super.loadVector(location_position,position);
    }


}
