package org.antinori.lumber;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Lumber {
	
	private LumberType type;
	
	//inches
	private double length;
	
	private ModelInstance instance;
	private BoundingBox bbox;
	private ModelInstance debugInstance;

	
	public Lumber(LumberType lt, double inches) {
		this.type = lt;
		this.length = inches;
	}
	
	public LumberType getType() {
		return type;
	}
	public void setType(LumberType type) {
		this.type = type;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
	

	public ModelInstance getInstance() {
		return instance;
	}

	public BoundingBox getBbox() {
		return bbox;
	}

	public void setInstance(ModelInstance instance) {
		this.instance = instance;
		this.setBbox(instance.calculateBoundingBox(new BoundingBox()));
	}

	public void setBbox(BoundingBox bbox) {
		this.bbox = bbox;
	}


	public ModelInstance getDebugInstance() {
		return debugInstance;
	}

	public void setDebugInstance(ModelInstance debugInstance) {
		this.debugInstance = debugInstance;
		this.debugInstance.transform.setToTranslation(instance.transform.getTranslation(new Vector3()));
	}

	
	@Override
	public String toString() {
				
		int feet = (int)length / 12;
		int leftover = (int)length % 12;
		
		return String.format("%s\t%s", type, "" + feet + "'" + leftover + "\"");
	}

}
