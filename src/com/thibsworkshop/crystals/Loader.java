package com.thibsworkshop.crystals;

import com.thibsworkshop.crystals.tools.BufferTools;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

//TODO: Rearenge Loader class, maybe make subclasses and remove the static prefix

public class Loader {
	
	private static final List<Integer> vaos = new ArrayList<>();
	private static final List<Integer> vbos = new ArrayList<>();
	private static final List<Integer> textures = new ArrayList<>();

	/**
	 * Initializes loaders
	 */
	public static void init() {
		//Block.blocks = JsonLoader.loadBlocks();
		//String[] biomeScripts = JsonLoader.loadBiomes();
		//Biome.biomes = GroovyLoader.loadBiomes(biomeScripts);
	}
	
	public static RawModel loadToVAO(float[] positions, short[] crystals) {
		
		int vaoID = createVAO();
		int[] vboIDs = new int[2];
		vboIDs[0] = storeDataInAttributeList(0,2, positions);
		vboIDs[1] = storeDataInAttributeList(1,1, crystals);
		unbindVAO();
		return new RawModel(vaoID,vboIDs,positions.length/2);
	}

	public static RawModel loadToVAO(float[] positions, float[] temperatures) {

		int vaoID = createVAO();
		int[] vboIDs = new int[2];
		vboIDs[0] = storeDataInAttributeList(0,2, positions);
		vboIDs[1] = storeDataInAttributeList(1,1, temperatures);
		unbindVAO();
		return new RawModel(vaoID,vboIDs,positions.length/2);
	}

	public static void modifyVAO(RawModel rawModel, float[] positions, short[] crystals){
		modifyAttributeData(rawModel.getVboID(0),positions);
		modifyAttributeData(rawModel.getVboID(1),crystals);
	}

	public static void modifyVAO(RawModel rawModel, float[] positions, float[] temperatures){
		modifyAttributeData(rawModel.getVboID(0),positions);
		modifyAttributeData(rawModel.getVboID(1),temperatures);
	}

	private static void modifyAttributeData(int vboID, float[] data){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0,data);
	}

	private static void modifyAttributeData(int vboID, int[] data){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0,data);
	}

	private static void modifyAttributeData(int vboID, short[] data){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0,data);
	}


	/*
	public static Texture loadTexture(String fileName) {
		Texture texture = TextureLoader.loadTexture("PNG", "Program/res/textures/"+fileName+".png");
		if(texture == null) return null;
		textures.add(texture.getID());
		return texture;
	}
	*/
	public static void cleanUp() {
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for(int tex:textures) {
			GL11.glDeleteTextures(tex);
		}
	}
	
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();

		vaos.add(vaoID);

		GL30.glBindVertexArray(vaoID);

		return vaoID;
	}
	
	private static int storeDataInAttributeList(int attributeNumber, int coordsSize,float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
		//FloatBuffer buffer = BufferTools.storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordsSize, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	private static int storeDataInAttributeList(int attributeNumber, int coordsSize,short[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_DYNAMIC_DRAW);
		GL30.glVertexAttribIPointer(attributeNumber, coordsSize, GL11.GL_SHORT, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private static int storeDataInAttributeList(int attributeNumber, int coordsSize,int[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
		//IntBuffer buffer = BufferTools.storeDataInIntBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL30.glVertexAttribIPointer(attributeNumber, coordsSize, GL11.GL_UNSIGNED_INT, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private static int bindIndicesBuffer(int[] indices) {
		int iboID = GL15.glGenBuffers();
		vbos.add(iboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboID);
		IntBuffer buffer = BufferTools.storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
		return iboID;
		}
	
}
