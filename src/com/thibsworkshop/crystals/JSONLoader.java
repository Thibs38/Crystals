package com.thibsworkshop.crystals;

import com.thibsworkshop.crystals.tools.Utility;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.thibsworkshop.crystals.CrystalType;


public class JSONLoader {

    private static final String BLOCK_PATH = "res/data/crystals.json";

    private static class TempCrystalTemperatureTransfer{
        int crystalID;
        int successor;
        Vector2i range;

        public TempCrystalTemperatureTransfer(int crystalID, int successor, Vector2i range) {
            this.crystalID = crystalID;
            this.successor = successor;
            this.range = range;
        }
    }

    private static class TempCrystalTimeTransfer{
        int crystalID;
        int successor;
        int lifeTime;

        public TempCrystalTimeTransfer(int crystalID, int successor, int lifeTime) {
            this.crystalID = crystalID;
            this.successor = successor;
            this.lifeTime = lifeTime;
        }
    }

    /**
     * Loads all the blocks specified in the Json file
     * @return An array of all the blocks
     */
    static CrystalType[] loadCrystalTypes() {
        String content = "";
        CrystalType[] crystals = new CrystalType[CrystalType.MAX_CRYSTALS];
        try {
            content = Utility.readFile(BLOCK_PATH, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            System.err.println("Couldn't find file " + BLOCK_PATH);
            e.printStackTrace();
            return null;
        }

        JSONArray jsonArray = new JSONArray(content);
        TempCrystalTemperatureTransfer[] tempTempTransfers = new TempCrystalTemperatureTransfer[jsonArray.length()];
        TempCrystalTimeTransfer[] tempTimeTransfers = new TempCrystalTimeTransfer[jsonArray.length()];

        for(int i = 0; i< jsonArray.length(); i++) {
            JSONObject crystal = jsonArray.getJSONObject(i);

            short id = (short)crystal.getInt("id");
            String name = crystal.getString("name");
            float density = (float)crystal.getDouble("density");

            JSONArray colors = crystal.getJSONArray("color");
            Vector4f color = new Vector4f(
                    (float)colors.getDouble(0),
                    (float)colors.getDouble(1),
                    (float)colors.getDouble(2),
                    (float)colors.getDouble(3)
            );

            boolean gravity = crystal.getBoolean("gravity");
            boolean liquid = crystal.getBoolean("liquid");
            float defaultTemperature = crystal.getInt("defaultTemperature");
            float thermalConductivity = crystal.getFloat("thermalConductivity");
            int bloom = crystal.getInt("bloom");

            crystals[id] = new CrystalType(id,name,density,color,gravity,liquid,defaultTemperature,thermalConductivity,bloom);

            try{
                JSONObject temperatureTransfer = crystal.getJSONObject("temperatureTransfer");
                JSONArray rangeArray = temperatureTransfer.getJSONArray("range");
                Vector2i range = new Vector2i(rangeArray.getInt(0),rangeArray.getInt(1));
                int successor = temperatureTransfer.getInt("successor");
                tempTempTransfers[i] = new TempCrystalTemperatureTransfer(id,successor,range);
            } catch (JSONException ignored) { }

            try{
                JSONObject timeTransfer = crystal.getJSONObject("timeTransfer");
                int lifeTime = timeTransfer.getInt("lifeTime");
                int successor = timeTransfer.getInt("successor");
                tempTimeTransfers[i] = new TempCrystalTimeTransfer(id,successor,lifeTime);
            } catch (JSONException ignored) { }

        }

        for (TempCrystalTemperatureTransfer tempTempTransfer : tempTempTransfers) {
            if(tempTempTransfer == null)
                continue;
            crystals[tempTempTransfer.crystalID].addTemperatureTransfer(crystals[tempTempTransfer.successor], tempTempTransfer.range);
        }
        for (TempCrystalTimeTransfer tempTimeTransfer : tempTimeTransfers) {
            if(tempTimeTransfer == null)
                continue;
            crystals[tempTimeTransfer.crystalID].addTimeTransfer(crystals[tempTimeTransfer.successor], tempTimeTransfer.lifeTime);
        }

        return crystals;

    }
}
