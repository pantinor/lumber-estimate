package org.antinori.stone;

import com.badlogic.gdx.graphics.Color;

public class StoneMaterial {
	
	private String name;
	private StoneType type;
	private int count;
	private float cost;
	private Color color = Color.WHITE;
	
	public StoneMaterial(String name, StoneType type, int count, float cost) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.cost = cost;
	}
	
	public StoneMaterial(String name, StoneType type, int count, float cost, Color color) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.cost = cost;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	public StoneType getType() {
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
	public void setType(StoneType type) {
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
