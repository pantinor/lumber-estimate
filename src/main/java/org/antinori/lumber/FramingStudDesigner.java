package org.antinori.lumber;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class FramingStudDesigner extends SimpleGame {
	
	public Environment environment;
	
	public Map<String, Room> boxes = new HashMap<String, Room>();
	RoomSelectionPanel dialog;
	boolean fullscreen = false;

		
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "FramingStudDesigner";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 768;
		new LwjglApplication(new FramingStudDesigner(), cfg);
	}
	

	@Override
	public void init() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		float width = 23*12;
		float length = 18*12;
		float height = 10*12;
		
		addRoom(0,0,0, width, height, length);
		addRoom(0,0,20*12, 12*12, 10*12, 8*12);
					
		createAxes();
		
		dialog = new RoomSelectionPanel(hud, this, skin);
		hud.addActor(dialog);
		
	}

	@Override
	public void draw(float delta) {
		
		cam.update();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		hud.act(Gdx.graphics.getDeltaTime());

		modelBatch.begin(cam);
				
		for (Room box : boxes.values()) {
			box.draw(modelBatch, environment);
		}
		
        modelBatch.render(axesInstance);

		modelBatch.end();
				
	}
	
	public String addRoom(float x, float y, float z, float width, float height, float length) {
		int i = 1;
		String name = "Room " + i;
		do {
			if (!boxes.containsKey(name)) {
				break;
			}
			name = "Room " + (i++);
		} while (true);
		
		ModelBuilder builder = new ModelBuilder();

		Room room = new Room(builder, Color.BLUE, x,y,z, width, height, length);
		boxes.put(name, room);
		
		return name;
	}
	
	@Override
	public boolean keyDown (int keycode) {
		if (keycode == Keys.X) {
			
			Room room = boxes.get(dialog.dropdown.getSelection());
			room.move(Room.Move.MOVEYMINUS);
			
		} else if (keycode == Keys.S) {
			
			Room room = boxes.get(dialog.dropdown.getSelection());
			room.move(Room.Move.MOVEYPLUS);
			
		} else if (keycode == Keys.C) {
			
			Room room = boxes.get(dialog.dropdown.getSelection());
			room.move(Room.Move.MOVEZMINUS);
			
		} else if (keycode == Keys.D) {
			
			Room room = boxes.get(dialog.dropdown.getSelection());
			room.move(Room.Move.MOVEZPLUS);
			
		} else if (keycode == Keys.Z) {
			
			Room room = boxes.get(dialog.dropdown.getSelection());
			room.move(Room.Move.MOVEXMINUS);
			
		} else if (keycode == Keys.A) {
			
			Room room = boxes.get(dialog.dropdown.getSelection());
			room.move(Room.Move.MOVEXPLUS);
			
		} else if (keycode == Keys.ESCAPE) {
			
			if(fullscreen) {
				Gdx.graphics.setDisplayMode(1280, 768, false);
				hud.setViewport(1280, 768, false);
				fullscreen = false;
			} else {
				DisplayMode desktopDisplayMode = Gdx.graphics.getDesktopDisplayMode();
				Gdx.graphics.setDisplayMode(desktopDisplayMode.width, desktopDisplayMode.height, true);
				hud.setViewport(desktopDisplayMode.width, desktopDisplayMode.height, false);
				fullscreen = true;
			}
		}
		return false;
	}

	
	final float GRID_MIN = -12*1000;
	final float GRID_MAX = 12*1000;
	final float GRID_STEP = 12;
	public Model axesModel;
	public ModelInstance axesInstance;

	private void createAxes() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		// grid
		MeshPartBuilder builder = modelBuilder.part("grid", GL10.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.LIGHT_GRAY);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		// axes
		builder = modelBuilder.part("axes", GL10.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 500, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 500, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 500);
		axesModel = modelBuilder.end();
		axesInstance = new ModelInstance(axesModel);
	}
	
	public List<String> getMaterialsList() {
		
		List<String> list = new ArrayList<String>();
		
		Map<LumberType, Integer> counts = new HashMap<LumberType, Integer>();
		for (LumberType type : LumberType.values()) {
			counts.put(type, 0);
		}

		for (String name : boxes.keySet()) {
			Room room = boxes.get(name);
			for (Wall wall : room.getWalls()) {
				for (Lumber l : wall.getTopPieces()) {
					LumberType type = l.getType();
					int c = counts.get(type);
					c ++;
					counts.put(type, c);
				}
				for (Lumber l : wall.getBottomPieces()) {
					LumberType type = l.getType();
					int c = counts.get(type);
					c ++;
					counts.put(type, c);
				}
				for (Lumber l : wall.getVerticalPieces()) {
					LumberType type = l.getType();
					int c = counts.get(type);
					c ++;
					counts.put(type, c);
				}
			}
		}
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		float total = 0;
		for (LumberType type : LumberType.values()) {
			int count = counts.get(type);
			if (count < 1) continue;
			float cost = count * type.getCost();
			String moneyString = formatter.format(cost);
			list.add(type.getName() + "  Count: " + count + "  Cost: " + moneyString);
			total += cost;
		}

		String moneyString = formatter.format(total);
		
		list.add("Total Cost: " + moneyString);
		
		return list;
	}


}