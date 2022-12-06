package com.thibsworkshop.crystals;

import com.thibsworkshop.crystals.CrystalType;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Crystal {

    private static abstract class CrystalTransfer {

        public abstract void update(float value);
    }

    private static class CrystalTemperatureTransfer extends CrystalTransfer {


        @Override
        public void update(float value) {
        }
    }

    private static class CrystalTimeTransfer extends CrystalTransfer {

        public float lifeTime;

        public CrystalTimeTransfer(float lifeTime){
            this.lifeTime = lifeTime;
        }

        @Override
        public void update(float value) {
            lifeTime-=1;
        }
    }


    public CrystalType type;

    public Vector2f velocity;
    public Vector2f position; // Buffered relative position for next frame

    public CrystalTimeTransfer timeTransfer;
    public CrystalTemperatureTransfer temperatureTransfer;

    public float temperature;

    // For example, if velocity is < 1, this increases and when reaching 1 the crystal moves and the buffer
    // go back to 0.

    public Crystal(CrystalType type, float temperature) {
        this.type = type;
        this.velocity = new Vector2f(0);
        this.position = new Vector2f(0);
        this.temperature = temperature;

        if(type.timeTransfer != null){
            this.timeTransfer = new CrystalTimeTransfer(type.timeTransfer.lifeTime);
        }
        if(type.temperatureTransfer != null){
            this.temperatureTransfer = new CrystalTemperatureTransfer();
        }
    }

    public boolean checkTemperatureTransfer(){
        if(temperatureTransfer == null)
            return false;
        temperatureTransfer.update(temperature);
        return type.temperatureTransfer.transfer(temperature);
    }

    public boolean checkTimeTransfer(){
        if(timeTransfer == null)
            return false;
        timeTransfer.update(timeTransfer.lifeTime);
        return type.timeTransfer.transfer(timeTransfer.lifeTime);
    }
}
