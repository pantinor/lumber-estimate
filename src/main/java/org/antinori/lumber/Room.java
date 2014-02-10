package org.antinori.lumber;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Room {
	
	private Model model;
	private ModelInstance instance;
	
	private Vector3 center;
	private Color color; 
	
	private List<Wall> walls = new ArrayList<Wall>();

	public Room(ModelBuilder modelBuilder, Color color, float x, float y, float z, float width, float height, float length) {
		
		this.center = new Vector3(x+width/2, y+height/2, z+length/2); //center of the box position
		this.color = color;
				
		model = modelBuilder.createBox(width, height, length, new Material(ColorAttribute.createDiffuse(color)), Usage.Position | Usage.Normal);
		instance = new ModelInstance(model, center);
		
		try {
			Wall w1 = new Wall(length/12, height/12, false, true);
			Wall w2 = new Wall(length/12, height/12, false, true);
			Wall w3 = new Wall(width/12, height/12, true, true);
			Wall w4 = new Wall(width/12, height/12, true, true);
			
			walls.add(w1);
			walls.add(w2);
			walls.add(w3);
			walls.add(w4);
			
			Vector3 corner1 = new Vector3(x, y, z);
			Vector3 corner2 = new Vector3(x + width - (w2.isUse2x6()?5.5f:3.5f), y, z);
			Vector3 corner3 = new Vector3(x + (w1.isUse2x6()?5.5f:3.5f), y, z);
			Vector3 corner4 = new Vector3(x + (w1.isUse2x6()?5.5f:3.5f), y, z+length-(w1.isUse2x6()?5.5f:3.5f));
			
			w1.setCornerPos(corner1);
			w2.setCornerPos(corner2);
			w3.setCornerPos(corner3);
			w4.setCornerPos(corner4);
			
			Vector3 center1 = new Vector3(x+(w1.isUse2x6()?5.5f:3.5f)/2, y+height/2, z+length/2);
			Vector3 center2 = new Vector3(corner2.x+((w2.isUse2x6()?5.5f:3.5f)/2), y+height/2, z+length/2);
			Vector3 center3 = new Vector3(x+(width/2), y+height/2, z+(w3.isUse2x6()?5.5f:3.5f)/2);
			Vector3 center4 = new Vector3(x+(width/2), y+height/2, z+(length)-(w3.isUse2x6()?5.5f:3.5f)/2);
			
			w1.setCenterPos(center1);
			w2.setCenterPos(center2);
			w3.setCenterPos(center3);
			w4.setCenterPos(center4);


			Model w1model = modelBuilder.createBox((w1.isUse2x6()?5.5f:3.5f), height, length, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
			w1.setInstance(new ModelInstance(w1model, center1));
			
			Model w2model = modelBuilder.createBox((w2.isUse2x6()?5.5f:3.5f), height, length, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
			w2.setInstance(new ModelInstance(w2model, center2));
			
			Model w3model = modelBuilder.createBox(width - (w3.isUse2x6()?5.5f:3.5f)*2, height, (w3.isUse2x6()?5.5f:3.5f), new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.Normal);
			w3.setInstance(new ModelInstance(w3model, center3));

			Model w4model = modelBuilder.createBox(width - (w3.isUse2x6()?5.5f:3.5f)*2, height, (w3.isUse2x6()?5.5f:3.5f), new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.Normal);
			w4.setInstance(new ModelInstance(w4model, center4));
			
			addLumberInstances(modelBuilder, w1);
			addLumberInstances(modelBuilder, w2);
			addLumberInstances(modelBuilder, w3);
			addLumberInstances(modelBuilder, w4);
			
			
			System.out.println(w1.toString());
			System.out.println(w2.toString());
			System.out.println(w3.toString());
			System.out.println(w4.toString());
			
			float c = w1.getCost()+w2.getCost()+w3.getCost()+w4.getCost();
			
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			String moneyString = formatter.format(c);
			System.out.println("total cost = " + moneyString);


		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void draw(ModelBatch modelBatch, Environment environment) {
	
		//modelBatch.render(instance, environment);
		
		for (Wall wall : walls) {
			
			//modelBatch.render(wall.getInstance(), environment);

			for (Lumber l : wall.getTopPieces()) {
				modelBatch.render(l.getInstance(), environment);
				modelBatch.render(l.getDebugInstance(), environment);
			}
			
			for (Lumber l : wall.getBottomPieces()) {
				modelBatch.render(l.getInstance(), environment);
				modelBatch.render(l.getDebugInstance(), environment);
			}
			for (Lumber l : wall.getVerticalPieces()) {
				modelBatch.render(l.getInstance(), environment);
				modelBatch.render(l.getDebugInstance(), environment);
			}
		}
		

	}
	
	private void addLumberInstances(ModelBuilder modelBuilder, Wall wall) {
		
		Vector3 wallPos = wall.getCornerPos();
		
		float tempLen = 0;
		for (Lumber l : wall.getTopPieces()) {
			
			float lcx = wall.isSideWall()? wallPos.x + tempLen : wallPos.x;
			float lcy = wallPos.y + (float)wall.getHeightInches() - 1.5f;
			float lcz = wall.isSideWall()? wallPos.z : wallPos.z + tempLen;
			
			tempLen += (float)l.getLength();
			
			Vector3 corner = new Vector3(lcx, lcy, lcz);
			Vector3 dims = getStudDimensions(l.getType(), (float)l.getLength(), wall.isSideWall(), false);
			Vector3 center = new Vector3(corner.x+dims.x/2, corner.y+dims.y/2, corner.z+dims.z/2);
			
			Model model = modelBuilder.createBox(dims.x, dims.y, dims.z, new Material(ColorAttribute.createDiffuse(Color.PINK)), Usage.Position | Usage.Normal);

			instance = new ModelInstance(model, center);
			l.setInstance(instance);
			l.setDebugInstance(createDebugBoxOutline(l.getBbox()));
		}
		
		tempLen = 0;
		for (Lumber l : wall.getBottomPieces()) {
			float lcx = wall.isSideWall()? wallPos.x + tempLen : wallPos.x;
			float lcy = wallPos.y;
			float lcz = wall.isSideWall()? wallPos.z : wallPos.z + tempLen;
			
			tempLen += (float)l.getLength();
			
			Vector3 corner = new Vector3(lcx, lcy, lcz);
			Vector3 dims = getStudDimensions(l.getType(), (float)l.getLength(), wall.isSideWall(), false);
			Vector3 center = new Vector3(corner.x+dims.x/2, corner.y+dims.y/2, corner.z+dims.z/2);
			
			Model model = modelBuilder.createBox(dims.x, dims.y, dims.z, new Material(ColorAttribute.createDiffuse(Color.PINK)), Usage.Position | Usage.Normal);

			instance = new ModelInstance(model, center);
			l.setInstance(instance);
			l.setDebugInstance(createDebugBoxOutline(l.getBbox()));
		}
		
		tempLen = 0;
		for (Lumber l : wall.getVerticalPieces()) {
			
			if (wall.getWidthInches() - tempLen <= 1.5) {
				tempLen = (float) wall.getWidthInches() - 1.5f;
			}

			float lcx = wall.isSideWall()? wallPos.x + tempLen : wallPos.x;
			float lcy = wallPos.y + 1.5f;
			float lcz = wall.isSideWall()? wallPos.z : wallPos.z + tempLen;
			
//			if (!wall.isSideWall()) {
//				System.out.println(l.toString() + " tempLen=" + tempLen + " lcx=" + lcx + " wall width inches=" + wall.getWidthInches());
//			}
			
			Vector3 corner = new Vector3(lcx, lcy, lcz);
			Vector3 dims = getStudDimensions(l.getType(), (float)l.getLength(), wall.isSideWall(), true);
			Vector3 center = new Vector3(corner.x+dims.x/2, corner.y+dims.y/2, corner.z+dims.z/2);
			
			Model model = modelBuilder.createBox(dims.x, dims.y, dims.z, new Material(ColorAttribute.createDiffuse(Color.PINK)), Usage.Position | Usage.Normal);

			instance = new ModelInstance(model, center);
			l.setInstance(instance);
			l.setDebugInstance(createDebugBoxOutline(l.getBbox()));
			
			tempLen += 16;

			
		}
	}
	
	/**
	 * Get the dimensions for the stud in 3D coordinates
	 */
	private Vector3 getStudDimensions(LumberType lt, float len, boolean sideWall, boolean isVertical) {
		float width = 0;
		float height = 0;
		float length = 0;

		if (sideWall) {
			if (isVertical) {
				width = lt.getWidth();
				height = len;
				length = lt.getHeight();
			} else {
				width = len;
				height = lt.getWidth();
				length = lt.getHeight();
			}
		} else {
			if (isVertical) {
				width = lt.getHeight();
				height = len;
				length = lt.getWidth();
			} else {
				width = lt.getHeight();
				height = lt.getWidth();
				length = len;
			}
		}
		return new Vector3(width, height, length);
	}
	
	private ModelInstance createDebugBoxOutline(BoundingBox box) {

		Model model = null;

		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		
		MeshPartBuilder builder = modelBuilder.part("box", GL10.GL_LINES, Usage.Position | Usage.Color, new Material());
		
		builder.setColor(Color.GREEN);
				
		//top
		builder.line(box.min.x, box.max.y, box.min.z, box.max.x, box.max.y, box.min.z); 
		builder.line(box.max.x, box.max.y, box.min.z, box.max.x, box.max.y, box.max.z); 
		builder.line(box.max.x, box.max.y, box.max.z, box.min.x, box.max.y, box.max.z); 
		builder.line(box.min.x, box.max.y, box.max.z, box.min.x, box.max.y, box.min.z); 

		// back
		builder.line(box.min.x, box.min.y, box.min.z, box.max.x, box.min.y, box.min.z); 
		builder.line(box.max.x, box.min.y, box.min.z, box.max.x, box.max.y, box.min.z); 
		builder.line(box.min.x, box.max.y, box.min.z, box.min.x, box.min.y, box.min.z); 

		// front
		builder.line(box.min.x, box.min.y, box.max.z, box.max.x, box.min.y, box.max.z); 
		builder.line(box.max.x, box.min.y, box.max.z, box.max.x, box.max.y, box.max.z); 
		builder.line(box.min.x, box.max.y, box.max.z, box.min.x, box.min.y, box.max.z); 

		// bottom
		builder.line(box.max.x, box.min.y, box.min.z, box.max.x, box.min.y, box.max.z); 
		builder.line(box.min.x, box.min.y, box.max.z, box.min.x, box.min.y, box.min.z);

		model = modelBuilder.end();
		return new ModelInstance(model);
	}

	
	public Model getModel() {
		return model;
	}

	public ModelInstance getInstance() {
		return instance;
	}

	public Vector3 getCenter() {
		return center;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setInstance(ModelInstance instance) {
		this.instance = instance;
	}

	public void setCenter(Vector3 pos) {
		this.center = pos;
	}
	

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void resetColor() {
		this.instance.materials.get(0).set(ColorAttribute.createDiffuse(color));		
	}
	
	public void highlight() {
		this.instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
	}
	
	public void changeColor(Color color) {
		this.instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
	}
	
	


}
