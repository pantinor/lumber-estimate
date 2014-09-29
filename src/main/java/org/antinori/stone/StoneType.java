package org.antinori.stone;

import com.badlogic.gdx.math.Vector3;

public enum StoneType {
	
	LUXORA_3P8_X_8P6_WALL("Luxora 3.8-in x 8.6-in Wall", 
			new Vector3(-4,-2,-4), new Vector3(-4,-2,4.6f), new Vector3(3.5f,-2,-2.75f), new Vector3(3.5f,-2,3.35f),
			new Vector3(-4,1.8f,-4), new Vector3(-4,1.8f,4.6f), new Vector3(3.5f,1.8f,-2.75f), new Vector3(3.5f,1.8f,3.35f), 4.55f),
														
	LUXORA_6_X_16_WALL("Luxora 6-in x 16-in Wall", 
			new Vector3(-5,-3,-7), new Vector3(-5,-3,7), new Vector3(5,-3,-9), new Vector3(5,-3,7),
			new Vector3(-5,3,-7), new Vector3(-5,3,7), new Vector3(5,3,-9), new Vector3(5,3,7), 4.55f),
													
	LUXORA_6_X_6_WALL("Luxora 6-in x 6-in Wall", 
			new Vector3(-5,-3,-2), new Vector3(-5,-3,2), new Vector3(5,-3,-2), new Vector3(5,-3,4),
			new Vector3(-5,3,-2), new Vector3(-5,3,2), new Vector3(5,3,-2), new Vector3(5,3,4), 4.55f),
												
	LUXORA_3_CAP("Luxora 3-in Cap", 
			new Vector3(0,0,0), new Vector3(0,0,12f), new Vector3(11f,0,1f), new Vector3(11f,0,11f),
			new Vector3(0,3f,0), new Vector3(0,3f,12f), new Vector3(11f,3f,1f), new Vector3(11f,3f,11f), 4.55f),
									
	BERTRAM_3P5_X_11P5_WALL("Bertram 3.5-in x 11.5-in Wall", 
			new Vector3(0,0,0), new Vector3(0,0,11.5f), new Vector3(7.5f,0,0f), new Vector3(7.5f,0,11.5f),
			new Vector3(0,3.5f,0), new Vector3(0,3.5f,11.5f), new Vector3(7.5f,3.5f,0f), new Vector3(7.5f,3.5f,11.5f), 4.55f);
	
	private String name;
	private float cost;
	
	private Vector3 c000;//bottom front left
	private Vector3 c001;//bottom front right
	private Vector3 c100;//bottom back left
	private Vector3 c101;//bottom back right

	private Vector3 c010;//top front left
	private Vector3 c011;//top front right
	private Vector3 c110;//top back left
	private Vector3 c111;//top back right
		
	private StoneType(String name, Vector3 c000, Vector3 c001, Vector3 c100, Vector3 c101, 
			Vector3 c010, Vector3 c011, Vector3 c110, Vector3 c111, float cost) {
		this.name = name;
		this.c000 = c000;
		this.c010 = c010;
		this.c100 = c100;
		this.c110 = c110;
		this.c001 = c001;
		this.c011 = c011;
		this.c101 = c101;
		this.c111 = c111;
		this.cost = cost;
	}


	public String getName() {
		return name;
	}


	public Vector3 getC000() {
		return c000;
	}


	public Vector3 getC010() {
		return c010;
	}


	public Vector3 getC100() {
		return c100;
	}


	public Vector3 getC110() {
		return c110;
	}


	public Vector3 getC001() {
		return c001;
	}


	public Vector3 getC011() {
		return c011;
	}


	public Vector3 getC101() {
		return c101;
	}


	public Vector3 getC111() {
		return c111;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setC000(Vector3 c000) {
		this.c000 = c000;
	}


	public void setC010(Vector3 c010) {
		this.c010 = c010;
	}


	public void setC100(Vector3 c100) {
		this.c100 = c100;
	}


	public void setC110(Vector3 c110) {
		this.c110 = c110;
	}


	public void setC001(Vector3 c001) {
		this.c001 = c001;
	}


	public void setC011(Vector3 c011) {
		this.c011 = c011;
	}


	public void setC101(Vector3 c101) {
		this.c101 = c101;
	}


	public void setC111(Vector3 c111) {
		this.c111 = c111;
	}
		

	public float getCost() {
		return cost;
	}
	
	

}
