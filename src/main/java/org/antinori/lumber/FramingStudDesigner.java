package org.antinori.lumber;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
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
	
	public List<Room> boxes = new ArrayList<Room>();
		
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

		
		ModelBuilder builder = new ModelBuilder();
		
		float width = 23*12;
		float length = 18*12;
		float height = 10*12;
		
		Room box = new Room(builder, Color.BLUE, 0,0,0, width, height, length);
		boxes.add(box);
		
		Room box2 = new Room(builder, Color.BLUE, 0,0,20*12, 12*12, 10*12, 8*12);
		boxes.add(box2);
							
		createAxes();
		
	}

	@Override
	public void draw(float delta) {
		
		cam.update();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
				
		for (Room box : boxes) {
			box.draw(modelBatch, environment);
		}

		
        modelBatch.render(axesInstance);

		modelBatch.end();
		

		
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


}