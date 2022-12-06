package com.thibsworkshop.crystals.rendering;

import com.thibsworkshop.crystals.Timing;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

	public static final int MAX_LIGHT = 16;
	
	public static final Vector3f SKY_COLOR = new Vector3f(0.529f,0.808f,0.922f);





	public static String debugName = "Rendering";

	float currentLayer = 0;
	float nextLayer = 0;

	public MasterRenderer() {

		glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z,1);


		Timing.add(debugName, new String[]{
			"Entities",
			"Terrain"
		});
	}



	//FIXME
	// Add timing again
	
	public void render(ArrayList<Renderer> renderers) {

		//float fogDistance = Config.chunkViewDist * Chunk.F_CHUNK_SIZE * 2;
		prepare();

		for(Renderer renderer : renderers){
			renderer.render();
		}


	}

	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
	}
	



}
