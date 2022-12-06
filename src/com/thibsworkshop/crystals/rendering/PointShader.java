package com.thibsworkshop.crystals.rendering;

import com.thibsworkshop.crystals.CrystalType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PointShader extends ShaderProgram {

    private int location_projectionViewMatrix;
    private int location_scale;
    private int location_position;

    private int[] location_crystals_color;
    private int[] location_crystals_bloom;

    private static final String VERTEX_FILE = "pointVertexShader.vert";
    private static final String FRAGMENT_FILE = "pointFragmentShader.frag";


    public PointShader() {
        super(VERTEX_FILE, FRAGMENT_FILE,2);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionViewMatrix = super.getUniformLocation("uProjectionViewMatrix");
        location_scale = super.getUniformLocation("uScale");
        location_position = super.getUniformLocation("uPosition");

        location_crystals_color = new int[CrystalType.MAX_CRYSTALS];
        location_crystals_bloom = new int[CrystalType.MAX_CRYSTALS];

        for (int i = 0; i < CrystalType.MAX_CRYSTALS; i++) {
            location_crystals_color[i] = super.getUniformLocation("uCrystals[" + i + "].color");
            location_crystals_bloom[i] = super.getUniformLocation("uCrystals[" + i + "].bloom");
        }
    }

    @Override
    protected void bindAttributes() {

        super.bindAttribute(0, "position");
        super.bindAttribute(1,"crystal");
    }

    public void loadCrystals() {
        CrystalType[] crystalTypes = CrystalType.types;
        for(int i = 0; i < CrystalType.MAX_CRYSTALS; i++) {
            if(crystalTypes[i] != null) {
                CrystalType c = crystalTypes[i];
                super.loadVector(location_crystals_color[i], c.color);
                super.loadFloat(location_crystals_bloom[i], c.bloom);
            }
        }
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
