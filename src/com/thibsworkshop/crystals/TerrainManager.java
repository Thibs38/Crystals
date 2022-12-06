package com.thibsworkshop.crystals;


import com.thibsworkshop.crystals.tools.Maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFW.*;

public class TerrainManager {


    public short[] grid;
    public Crystal[] crystalGrid;
    public float[] positions;
    public float[] temperatures;

    public int width;

    public int height;

    public static RawModel model;
    public static RawModel temperatureModel;

    private final float gravity = 0.1f;

    public TerrainManager(){
        width = Window.mainWindow.getWidth()/10;
        height = Window.mainWindow.getHeight()/10;
        grid = new short[width * height];
        crystalGrid = new Crystal[width * height];
        positions = new float[width * height * 2];
        temperatures = new float[width * height];
        for (int i = 0; i < grid.length; i++) {
            addCrystal(i,CrystalType.AIR);
            //grid[i] = (short)(Math.random()*3+1);
            positions[i * 2] = i % width;
            positions[i * 2 + 1] =  i / width;
            temperatures[i] = 20;
            //if(i < width*2){
            //    System.out.println(positions[i*2] + " " + positions[i*2+1]);
            //}
        }
        //addCrystal((height-1)*width + width/2+10, CrystalType.GRASS);
        //addCrystal((height-1)*width + width/2+10+1, CrystalType.DIRT);
        model = Loader.loadToVAO(positions,grid);
        temperatureModel = Loader.loadToVAO(positions,temperatures);
    }


    long lastTime = 0;
    public boolean update(){
        long time = Time.getMilliTime();

        if(time - lastTime < 100)
            return false;

        //OPTIMIZE Find better way to shuffle
        List<Integer> newList = IntStream.rangeClosed(0, 9215).boxed().collect(Collectors.toList());
        Collections.shuffle( newList ) ;

        for(int j = 0; j < grid.length; j++){
            int i = newList.get(j);
            Crystal c = crystalGrid[i];
            /*if(c.checkTimeTransfer()) {
                addCrystal(i, c.type.timeTransfer.successor);
                c = crystalGrid[i];
            }else if(c.checkTemperatureTransfer()) {
                addCrystal(i, c.type.temperatureTransfer.successor);
                c = crystalGrid[i];
            }*/

            if(c.type.liquid)
                processFluid(i);
            else if (c.type.gravity)
                processGravity(i);

            diffuseTemperature(i);
        }

        Loader.modifyVAO(model, positions, grid);
        Loader.modifyVAO(temperatureModel,positions, temperatures);
        return true;
    }

    //FIXME: Relative temperature diffuse based on thermal conductivity
    public void diffuseTemperature(int pos){
        float temp = temperatures[pos];
        float TC = crystalGrid[pos].type.thermalConductivity;
        boolean left = pos % width == 0; // Crystal touching left side of grid
        boolean right = pos % width == width - 1; // Crystal touching right side of grid
        boolean bottom = pos < width;
        boolean top = pos >= width*height-width;

        int posLeft = pos -1;
        int posRight = pos + 1;
        int posBottom = pos - width;
        int posTop = pos + width;

        //if(crystalGrid[pos].type.id == CrystalType.FIRE)
        //    System.out.println(temperatures[pos]);
            //System.out.println((pos%width) + ", " + (pos/width) + " " + (posRight%width) + ", " + (posRight/width) + " " + (posLeft%width) + ", " + (posLeft/width) + " " + (posBottom%width) + ", " + (posBottom/width) + " " + (posTop%width) + ", " + (posTop/width));

        float leftTransfer = 0;
        float rightTransfer = 0;
        float bottomTransfer = 0;
        float topTransfer = 0;

        if(!left) {
            float TCLeft = crystalGrid[posLeft].type.thermalConductivity;
            leftTransfer = (temp - temperatures[posLeft]) * TC * TCLeft * 0.1f; //Delta time
        }
        if(!right) {
            float TCRight = crystalGrid[posRight].type.thermalConductivity;
            rightTransfer = (temp - temperatures[posRight]) * TC * TCRight * 0.1f;
        }
        if(!bottom){
            float TCBottom = crystalGrid[posBottom].type.thermalConductivity;
            bottomTransfer = (temp - temperatures[posBottom]) * TC * TCBottom * 0.1f;
        }
        if(!top){
            float TCTop = crystalGrid[posTop].type.thermalConductivity;
            topTransfer = (temp - temperatures[posTop]) * TC * TCTop * 0.1f;
        }

        temperatures[pos] = temperatures[pos] - topTransfer - bottomTransfer - leftTransfer - rightTransfer;
        if(!left) {
            temperatures[posLeft] += leftTransfer;
            //crystalGrid[posLeft].temperature += leftTransfer;
        }
        if(!right) {
            temperatures[posRight] += rightTransfer;
            //crystalGrid[posRight].temperature += rightTransfer;
        }
        if(!bottom){
            temperatures[posBottom] += bottomTransfer;
            //crystalGrid[posBottom].temperature += bottomTransfer;
        }
        if(!top){
            temperatures[posTop] += topTransfer;
            //crystalGrid[posTop].temperature += topTransfer;
        }
    }

    public void setTemperature(int x, int y, float temperature){
        int pos = Math.min(Math.max(y,0),height-1) * width + Math.min(Math.max(x,0),width-1);
        if(grid[pos] == CrystalType.AIR) {
            temperatures[pos] = temperature;
        }
    }

    public void addCrystal(int x, int y, short crystalType){
        int pos = Math.min(Math.max(y,0),height-1) * width + Math.min(Math.max(x,0),width-1);
        if(grid[pos] == CrystalType.AIR || crystalType == CrystalType.AIR){
            grid[pos] = crystalType;
            crystalGrid[pos] = new Crystal(CrystalType.types[crystalType], temperatures[pos]);
        }
    }

    private void addCrystal(int pos, short crystalType){
        if(grid[pos] == CrystalType.AIR || crystalType == CrystalType.AIR){
            grid[pos] = crystalType;
            crystalGrid[pos] = new Crystal(CrystalType.types[crystalType], temperatures[pos]);
        }
    }

    private void addCrystal(int pos, CrystalType type){
        grid[pos] = type.id;
        crystalGrid[pos] = new Crystal(type, temperatures[pos]);
    }

    /**
     * Swaps two Crystals in the grid.
     * @param a Crystal A's position
     * @param b Crystal B's position
     * @return b
     */
    private int swapCrystals(int a, int b){
        short temp = grid[a];
        grid[a] = grid[b];
        grid[b] = temp;
        Crystal tempC = crystalGrid[a];
        crystalGrid[a] = crystalGrid[b];
        crystalGrid[b] = tempC;

        float tempTemp = temperatures[a];
        temperatures[a] = temperatures[b];
        temperatures[b] = tempTemp;
        return b;
    }

    /**
     * Determines if Crystal a can swap with Crystal b.
     * @param a Crystal a
     * @param b Crystal b
     * @return True if they can, false otherwise.
     */
    private boolean canSwap(CrystalType a, CrystalType b){
        // To swap a with b, b must be affected by gravity, and it's density must be lower than a's.
        return b.gravity && b.density < a.density;
    }

    private int direction = 1;
    private void processGravity(int pos){
        Crystal crystal = crystalGrid[pos];
        crystal.velocity.y += gravity;
        crystal.position.y += crystal.velocity.y;
        int movement = (int)crystal.position.y;
        for(int i = 0; i < movement; i++){

            if(pos < width) {
                crystal.velocity.y = 0;
                crystal.position.y = 0;
                break;
            }

            boolean bottomClear = canSwap(crystal.type,crystalGrid[pos-width].type);
            if(bottomClear) {
                pos = swapCrystals(pos,pos - width);
                crystal.position.y -= Maths.sign(movement);
                continue;
            }

            boolean left = pos % width == 0; // Crystal touching left side of grid
            boolean right = pos % width == width - 1; // Crystal touching right side of grid
            boolean bottomRightClear = !right && canSwap(crystal.type,crystalGrid[pos - width + 1].type);
            boolean bottomLeftClear = !left && canSwap(crystal.type,crystalGrid[pos - width - 1].type);
            boolean rightClear = !right && canSwap(crystal.type,crystalGrid[pos + 1].type);
            boolean leftClear = !left && canSwap(crystal.type,crystalGrid[pos - 1].type);
            if((left || direction == 1 || !bottomLeftClear) && bottomRightClear && rightClear) {
                pos = swapCrystals(pos, pos - width + 1);
                direction *= -1;
                crystal.position.y -= Maths.sign(movement);
                continue;
            }
            if ((right || direction == -1 || !bottomRightClear) && bottomLeftClear && leftClear){
                pos = swapCrystals(pos,pos - width - 1);
                direction *= -1;
                crystal.position.y -= Maths.sign(movement);
                continue;
            }
            crystal.velocity.y = 0;
            crystal.position.y = 0;
            break;
        }
    }

    private void processFluid(int pos){
        Crystal crystal = crystalGrid[pos];
        crystal.velocity.y += gravity;
        crystal.position.y += crystal.velocity.y;
        int movement = (int)crystal.position.y;
        for(int i = 0; i < movement; i++) {

            if (pos < width) { // Bottom layer
                crystal.velocity.y = 0;
                crystal.position.y = 0;
                break;
            }

            // Bottom movement
            boolean bottomClear = canSwap(crystal.type,crystalGrid[pos - width].type);
            if (bottomClear) {
                pos = swapCrystals(pos, pos - width);
                crystal.position.y -= Maths.sign(movement);
                continue;
            }

            // Lateral movement
            boolean left = pos % width == 0; // Crystal touching left side of grid
            boolean right = pos % width == width - 1; // Crystal touching right side of grid
            boolean rightClear = !right && canSwap(crystal.type,crystalGrid[pos + 1].type);
            boolean leftClear = !left && canSwap(crystal.type,crystalGrid[pos - 1].type);

            if ((left || direction == 1 || !leftClear) && rightClear) {
                pos = swapCrystals(pos, pos + 1);
                direction *= -1;
                crystal.position.y -= Maths.sign(movement);
                continue;
            }
            if ((right || direction == -1 || !rightClear) && leftClear) {
                pos = swapCrystals(pos, pos - 1);
                direction *= -1;
                crystal.position.y -= Maths.sign(movement);
                continue;
            }

            // Bottom corner movement

/*
            boolean bottomRightClear = !right && canSwap(crystal.type,crystalGrid[pos - width + 1].type);
            boolean bottomLeftClear = !left && canSwap(crystal.type,crystalGrid[pos - width - 1].type);

            if ((left || direction == 1 || !bottomLeftClear) && bottomRightClear) {
                pos = swapCrystals(pos, pos - width + 1);
                direction *= -1;
                crystal.position.y -= Maths.sign(movement);
                continue;
            }
            if ((right || direction == -1 || !bottomRightClear) && bottomLeftClear) {
                pos = swapCrystals(pos, pos - width - 1);
                direction *= -1;
                crystal.position.y -= Maths.sign(movement);
                continue;
            }
*/


            crystal.velocity.y = 0;
            crystal.position.y = 0;
            break;

        }
    }


}
