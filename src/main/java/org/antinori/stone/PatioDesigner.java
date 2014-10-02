package org.antinori.stone;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antinori.lumber.Move;
import org.antinori.lumber.SimpleGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;

public class PatioDesigner extends SimpleGame {
	
	public Environment environment;
	
	public Map<String, StoneInstance> modelInstances = new HashMap<String, StoneInstance>();
	StoneSelectionDialog dialog;
	boolean fullscreen = false;
	
	ModelInstance patio = null;
	
	private static int[] PRESSED_KEYS = {Keys.LEFT,Keys.RIGHT};
	public static BitmapFont ftFont;
		
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "PatioDesigner";
		cfg.width = 1800;
		cfg.height = 1100;
		new LwjglApplication(new PatioDesigner(), cfg);
	}
	

	@Override
	public void init() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();

		FileHandle fontFile = Gdx.files.classpath("skin/arial.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 8;
		parameter.flip = true;
		parameter.genMipMaps = true;
		ftFont = generator.generateFont(parameter);
		generator.dispose();
		ftFont.setColor(Color.RED);

		ModelBuilder builder = new ModelBuilder();
		final Material material = new Material(ColorAttribute.createDiffuse(Color.ORANGE));
		final long attributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates;
		final Model cylinder = builder.createCylinder(12f*16f, 2f, 12f*16f, 32, material, attributes);
		patio = new ModelInstance(cylinder, 0, 0, 0);
		
		addBlock(StoneType.BERTRAM_3P5_X_11P5_WALL,0,0,0);
					
		createAxes();
		
		dialog = new StoneSelectionDialog(hud, this, skin);
		hud.addActor(dialog);
		
		cam.position.set(-150, 15, 0);
		cam.lookAt(0,15,0);
		
	}

	@Override
	public void draw(float delta) {
		
		cam.update();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		
		hud.act(Gdx.graphics.getDeltaTime());

		modelBatch.begin(cam);
		
		modelBatch.render(patio, environment);
				
		for (StoneInstance i : modelInstances.values()) {
			modelBatch.render(i.getInstance(), environment);
			if (StoneInstance.showDebug) {
				modelBatch.render(i.getDebugInstance(), environment);
			}
		}
		
        modelBatch.render(axesInstance);

		modelBatch.end();
		
		for (int k : PRESSED_KEYS) {
			if (Gdx.input.isKeyPressed(k)) keyDown(k);
		}
							
	}
	
	public String addBlock(StoneType st, float tx, float ty, float tz) {
		String name = getNextIndex();
		ModelBuilder builder = new ModelBuilder();
		Model model = createPolygonBox(builder, st, Color.BLUE, name);
		ModelInstance instance = new ModelInstance(model, tx, ty, tz);
		StoneInstance si = new StoneInstance(instance, st);
		modelInstances.put(name, si);
		return name;
	}
	
	public String getNextIndex() {
		int i = 1;
		do {
			if (!modelInstances.containsKey(""+i)) {
				break;
			}
			i++;
		} while (true);
		return ""+i;
	}
	
	public Model createPolygonBox(ModelBuilder mb, StoneType st, Color color, String label) {
		return createPolygonBox(mb, color, label, st.getC000(), st.getC010(), st.getC100(), st.getC110(), st.getC001(), st.getC011(), st.getC101(), st.getC111());
	}

	public Model createPolygonBox(ModelBuilder modelBuilder, Color color, String label, Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {
		Texture texture = drawText(label);
		TextureAttribute textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, texture);
		Material material = new Material(ColorAttribute.createDiffuse(color), textureAttribute);
		modelBuilder.begin();
		modelBuilder.part("box", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates, material).box(corner000, corner010, corner100, corner110, corner001, corner011, corner101, corner111);
		Model model = modelBuilder.end();
		model.manageDisposable(texture);
		return model;
	}
	
	@Override
	public boolean keyDown (int keycode) {
		
		if (dialog.dropdown.getSelected() != null) {
		
			String key = dialog.dropdown.getSelected().toString();
			StoneInstance si = modelInstances.get(key);
	
			if (keycode == Keys.X) {
				si.move(Move.MOVEYMINUS,.05f);
			} else if (keycode == Keys.S) {
				si.move(Move.MOVEYPLUS,.05f);
			} else if (keycode == Keys.C) {
				si.move(Move.MOVEZMINUS,.05f);
			} else if (keycode == Keys.D) {
				si.move(Move.MOVEZPLUS,.05f);
			} else if (keycode == Keys.Z) {
				si.move(Move.MOVEXMINUS,.05f);
			} else if (keycode == Keys.A) {
				si.move(Move.MOVEXPLUS,.05f);
				
			} else if (keycode == Keys.G) {
				si.move(Move.MOVEYPLUS,2);
			} else if (keycode == Keys.N) {
				si.move(Move.MOVEZMINUS,2);
			} else if (keycode == Keys.H) {
				si.move(Move.MOVEZPLUS,2);
			} else if (keycode == Keys.V) {
				si.move(Move.MOVEXMINUS,2);
			} else if (keycode == Keys.F) {
				si.move(Move.MOVEXPLUS,2);
			} else if (keycode == Keys.B) {
				si.move(Move.MOVEYMINUS,2);
				
			} else if (keycode == Keys.LEFT) {
				si.turn(true);
			} else if (keycode == Keys.RIGHT) {
				si.turn(false);
			}
		}
			
		if (keycode == Keys.NUMPAD_0) {
			cam.position.set(-150, 15, 0);
			cam.up.set(new Vector3(0,1,0));
			cam.lookAt(0,15,0);
		}
			

		if (keycode > Keys.NUMPAD_0 && keycode < Keys.NUMPAD_9) {
			lookAtSelectedBlock(keycode);
		}
			
		if (keycode == Keys.ESCAPE) {
			
			if(fullscreen) {
				Gdx.graphics.setDisplayMode(1280, 768, false);
				hud.getViewport().update(1280, 768, false);
				fullscreen = false;
			} else {
				DisplayMode desktopDisplayMode = Gdx.graphics.getDesktopDisplayMode();
				Gdx.graphics.setDisplayMode(desktopDisplayMode.width, desktopDisplayMode.height, true);
				hud.getViewport().update(desktopDisplayMode.width, desktopDisplayMode.height, false);
				fullscreen = true;
			}
		}
		return false;
	}
	
	private void lookAtSelectedBlock(int keycode) {
		
		if (dialog.dropdown.getSelected() != null) {
			
			String key = dialog.dropdown.getSelected().toString();
			StoneInstance si = modelInstances.get(key);
			
			Vector3 tmp = new Vector3();
			si.getInstance().transform.getTranslation(tmp);
			
			switch(keycode) {
			case Keys.NUMPAD_2: 
				cam.position.set(tmp.x-20,tmp.y,tmp.z);
				break;
			case Keys.NUMPAD_8: 
				cam.position.set(tmp.x+20,tmp.y,tmp.z);
				break;			
			case Keys.NUMPAD_4: 
				cam.position.set(tmp.x,tmp.y,tmp.z-20);
				break;
			case Keys.NUMPAD_6: 
				cam.position.set(tmp.x,tmp.y,tmp.z+20);
				break;
			case Keys.NUMPAD_5: 
				cam.position.set(tmp.x,tmp.y+50,tmp.z);
				break;
			}
			
			cam.up.set(new Vector3(0,1,0));

			cam.lookAt(tmp);
			
		}
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
		MeshPartBuilder builder = modelBuilder.part("grid", GL30.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
		builder.setColor(Color.LIGHT_GRAY);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		// axes
		builder = modelBuilder.part("axes", GL30.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 500, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 500, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 500);
		axesModel = modelBuilder.end();
		axesInstance = new ModelInstance(axesModel);
	}
	
	public List<StoneMaterial> getMaterialsList() {
		
		List<StoneMaterial> list = new ArrayList<StoneMaterial>();
		
		Map<StoneType, Integer> counts = new HashMap<StoneType, Integer>();
		for (StoneType type : StoneType.values()) {
			counts.put(type, 0);
		}

		for (StoneInstance si : modelInstances.values()) {
			StoneType type = si.getType();
			int c = counts.get(type);
			c ++;
			counts.put(type, c);
		}
		
		float total = 0;
		for (StoneType type : StoneType.values()) {
			int count = counts.get(type);
			if (count < 1) continue;
			float cost = count * type.getCost();
			list.add(new StoneMaterial(type.getName(), type, count, cost));
			total += cost;
		}

		list.add(new StoneMaterial("Total Cost", null, 0, total, Color.YELLOW));
				
		return list;
	}
	
	/**
	 * Draw text to a texture that can be applied to the box faces for easy identification and selection with a label.
	 */
	public Texture drawText(String... text) {
		
		float height = ftFont.getLineHeight() * (text.length + 1);
		float width = 0;
		float xoffset = 0;
		float yoffset = 0;
		float temp;
		for (int i = 0; i < text.length; i++) {
			temp = ftFont.getBounds(text[i]).width;
			if (temp > width)
				width = temp;
		}
		
		SpriteBatch sb = new SpriteBatch();
		FrameBuffer fB = new FrameBuffer(Format.RGBA4444, (int) width, (int) height, false);
		fB.begin();
		Gdx.gl.glClearColor(1f, 1f, 1f, 0.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		sb.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		sb.begin();
		for (int line = 0; line < text.length; line++) {
			ftFont.draw(sb, text[line], xoffset, yoffset + (line + 1) * ftFont.getLineHeight());
		}
		sb.end();
		fB.end();
		
		return fB.getColorBufferTexture();
	}
	
	
	

}