package com.thibsworkshop.crystals;

import org.joml.Vector2i;
import org.joml.Vector4f;

public class CrystalType {

    private static abstract class CrystalTypeTransfer {
        public CrystalType successor;

        public CrystalTypeTransfer(CrystalType crystalSuccessor){
            this.successor = crystalSuccessor;
        }

        public abstract boolean transfer(float value);
    }

    public static class CrystalTypeTemperatureTransfer extends CrystalTypeTransfer {

        public Vector2i temperatureRange;

        public CrystalTypeTemperatureTransfer(CrystalType successor, Vector2i temperatureRange){
            super(successor);
            this.temperatureRange = temperatureRange;
        }

        @Override
        public boolean transfer(float temperature) {
            return temperature >= temperatureRange.x && temperature <= temperatureRange.y;
        }
    }

    public static class CrystalTypeTimeTransfer extends CrystalTypeTransfer {

        public float lifeTime;

        public CrystalTypeTimeTransfer(CrystalType successor, float lifeTime){
            super(successor);
            this.lifeTime = lifeTime;
        }

        @Override
        public boolean transfer(float time) {
            return time > this.lifeTime;
        }
    }

    public static final int MAX_CRYSTALS = 256;
    public static CrystalType[] types = new CrystalType[MAX_CRYSTALS];
    public static final short VOID = 0;
    public static final short AIR = 1;
    public static final short SAND = 2;
    public static final short GLASS = 3;
    public static final short LIQUID_GLASS = 4;
    public static final short WOOD = 5;
    public static final short FIRE = 6;
    public static final short SMOKE = 7;
    public static final short ASH = 8;


    public short id;
    public String name;
    public float density;
    public Vector4f color;
    public CrystalTypeTemperatureTransfer temperatureTransfer;
    public CrystalTypeTimeTransfer timeTransfer;
    public boolean gravity;
    public boolean liquid;
    public float defaultTemperature;
    public float thermalConductivity;
    public float bloom;

    public CrystalType(short id, String name, float density, Vector4f color, boolean gravity, boolean liquid, float defaultTemperature, float thermalConductivity, int bloom) {
        this.id = id;
        this.name = name;
        this.density = density;
        this.color = color;
        this.gravity = gravity;
        this.liquid = liquid;
        this.defaultTemperature = defaultTemperature;
        this.thermalConductivity = thermalConductivity;
        this.bloom = bloom;
    }

    public void addTemperatureTransfer(CrystalType successor, Vector2i range){
        this.temperatureTransfer = new CrystalTypeTemperatureTransfer(successor,range);
    }

    public void addTimeTransfer(CrystalType successor, int lifeTime){
        this.timeTransfer = new CrystalTypeTimeTransfer(successor,lifeTime);
    }

    public static void init(){
        /*types[0] = new CrystalType((short) 0, "air", true, 0.1f, Color.BLACK, -1, AIR, 0);
        types[1] = new CrystalType((short) 1, "dirt", false,1.1f,  Color.DARK_GREEN,-1,AIR,  1000,0);
        types[2] = new CrystalType((short) 2, "grass",false,1.1f,  Color.GREEN,     -1,AIR,  1000,10);
        types[3] = new CrystalType((short) 3, "water",true, 1.0f,  Color.LIGHT_BLUE,-1,AIR,  false,0);
        types[4] = new CrystalType((short) 4, "wood", false,0.1f,  Color.DARK_RED,  -1,AIR,  true,0);
        types[5] = new CrystalType((short) 5, "fire", true, 0.09f, Color.RED,       20,SMOKE,false,0);
        types[6] = new CrystalType((short) 6, "smoke",true, 0.05f, Color.GREY,      -1,AIR,  false,0);*/

    }
}
