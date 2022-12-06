package com.thibsworkshop.crystals;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class TerrainInteractor {

    TerrainManager terrainManager;
    int penSize = 1;
    short selectedCrystal = CrystalType.WOOD;

    public TerrainInteractor(TerrainManager terrainManager){
        this.terrainManager = terrainManager;
    }

    public void update(){

        int mousePosX = (int)Math.round((Input.getMousePosition().x) / Window.mainWindow.getWidth() * terrainManager.width-0.5f);
        int mousePosY = (int)Math.round((Input.getMousePosition().y) / Window.mainWindow.getHeight() * terrainManager.height-0.5f) + terrainManager.height;

        if(Input.isKeyDown(GLFW_KEY_UP)){
            penSize+=1;
        } else if(Input.isKeyDown(GLFW_KEY_DOWN)){
            penSize-=1;
        }

        if(penSize == 0)
            penSize = 1;
        else if (penSize == 6)
            penSize = 5;

        if(Input.isKeyDown(GLFW_KEY_1))
            selectedCrystal = 1;
        else if(Input.isKeyDown(GLFW_KEY_2))
            selectedCrystal = 2;
        else if(Input.isKeyDown(GLFW_KEY_3))
            selectedCrystal = 3;
        else if(Input.isKeyDown(GLFW_KEY_4))
            selectedCrystal = 4;
        else if(Input.isKeyDown(GLFW_KEY_5))
            selectedCrystal = 5;
        else if(Input.isKeyDown(GLFW_KEY_6))
            selectedCrystal = 6;
        else if(Input.isKeyDown(GLFW_KEY_7))
            selectedCrystal = 7;
        else if(Input.isKeyDown(GLFW_KEY_8))
            selectedCrystal = 8;

        /*else if(Input.isKeyDown(GLFW_KEY_6))
            selectedCrystal = 2;
        else if(Input.isKeyDown(GLFW_KEY_7))
            selectedCrystal = 2;
        else if(Input.isKeyDown(GLFW_KEY_8))
            selectedCrystal = 2;
        else if(Input.isKeyDown(GLFW_KEY_9))
            selectedCrystal = 2;*/

        if(Input.isKeyHold(GLFW_MOUSE_BUTTON_LEFT)) {
            for (int i = -(penSize - 1); i <= (penSize - 1); i++) {
                for (int j = -(penSize - 1); j <= (penSize - 1); j++) {
                    if (i * i + j * j <= penSize * penSize) {
                        CrystalType type = CrystalType.types[selectedCrystal];
                        terrainManager.setTemperature(i + mousePosX, j + mousePosY, type.defaultTemperature);
                        terrainManager.addCrystal(i + mousePosX, j + mousePosY, selectedCrystal);
                    }
                }
            }
        }
        else if (Input.isKeyHold(GLFW_MOUSE_BUTTON_RIGHT)){
            for (int i = -(penSize-1); i <= (penSize-1); i++) {
                for (int j = -(penSize-1); j <= (penSize-1); j++) {
                    if(i*i + j*j <= penSize*penSize)
                        terrainManager.addCrystal(i+mousePosX, j+mousePosY, CrystalType.AIR);
                }
            }
        }
    }
}
