package com.thibsworkshop.crystals;

public class RawModel {

	private final int vaoID;
	private final int[] vboIDs;
	private final int vertexCount;
	
	public RawModel(int vaoID, int vboIDs[], int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.vboIDs = vboIDs;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVboID(int attributeID){ return vboIDs[attributeID]; }

	public int getVertexCount() {
		return vertexCount;
	}
	
}
