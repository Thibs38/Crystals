package com.thibsworkshop.crystals.tools;

import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;

public class Color {

    public static final Vector4f LIGHT_RED = new Vector4f(1,0.5f,0.5f,1);
    public static final Vector4f RED = new Vector4f(1,0,0,1);
    public static final Vector4f DARK_RED = new Vector4f(0.5f,0,0,1);

    public static final Vector4f LIGHT_GREEN = new Vector4f(0.5f,1,0.5f,1);
    public static final Vector4f GREEN = new Vector4f(0,1,0,1);
    public static final Vector4f DARK_GREEN = new Vector4f(0.5f,0,0,1);

    public static final Vector4f LIGHT_BLUE = new Vector4f(0.5f,0.5f,1,1);
    public static final Vector4f BLUE = new Vector4f(0,0,1,1);
    public static final Vector4f DARK_BLUE = new Vector4f(0,0,0.5f,1);

    public static final Vector4f WHITE = new Vector4f(1,1,1,1);
    public static final Vector4f GREY = new Vector4f(0.5f,0.5f,0.5f,1);
    public static final Vector4f BLACK = new Vector4f(0,0,0,1);


    public static Vector4f rgba(int r, int g, int b, int a){
        return new Vector4f((float)r / 255, (float)g / 255, (float)b / 255, (float)a / 255);
    }
}
