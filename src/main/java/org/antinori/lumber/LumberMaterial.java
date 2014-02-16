package org.antinori.lumber;

import com.badlogic.gdx.graphics.Color;

public class LumberMaterial {
	
	private String name;
	private LumberType type;
	private int count;
	private float cost;
	private Color color = Color.BLACK;
	
	public LumberMaterial(String name, LumberType type, int count, float cost) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.cost = cost;
	}
	
	public LumberMaterial(String name, LumberType type, int count, float cost, Color color) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.cost = cost;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	public LumberType getType() {
		return type;
	}
	public int getCount() {
		return count;
	}
	public float getCost() {
		return cost;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(LumberType type) {
		this.type = type;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	

}
