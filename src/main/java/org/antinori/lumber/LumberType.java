package org.antinori.lumber;

public enum LumberType {
	
	//studs
	
	
	//prices taken from home depot website
	
	LUM_2x4x8 ("2x4x8", 1.5f,	3.5f,	8,	2.57f),
	LUM_2x4x10("2x4x10", 1.5f,	3.5f,	10,	4.19f),
	LUM_2x4x12("2x4x12", 1.5f,	3.5f,	12,	4.55f),
	LUM_2x4x16("2x4x16", 1.5f,	3.5f,	16,	6.89f),
	
	LUM_2x6x8 ("2x4x8", 1.5f,	5.5f,	8,	4.78f),
	LUM_2x6x10("2x4x10", 1.5f,	5.5f,	10,	6.12f),
	LUM_2x6x12("2x4x12", 1.5f,	5.5f,	12,	8.27f),
	LUM_2x6x16("2x4x16", 1.5f,	5.5f,	16,	9.77f);
	
	private String name;
	private float width;
	private float height;
	private float length;
	private float cost;
		
	private LumberType(String name, float w, float h, float l, float c) {
		this.setName(name);
		this.width = w;
		this.height = h;
		this.length = l;
		this.cost = c;
	}

	public float getCost() {
		return cost;
	}

	public float getLength() {
		return 12f * length;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
