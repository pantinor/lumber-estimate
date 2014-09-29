/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import org.antinori.stone.StoneType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Basic3DTest extends TestMain {
	public PerspectiveCamera cam;
	public CameraInputController inputController;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	public Environment environment;

	@Override
	public void create() {
		modelBatch = new ModelBatch(new DefaultShaderProvider());
		// modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(-3f, 3f, 3f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal);

//		Vector3 c1 = new Vector3(0, 0, 0);
//		Vector3 c2 = new Vector3(0, 1, 0);
//		Vector3 c3 = new Vector3(1, 0, 0);
//		Vector3 c4 = new Vector3(1, 1, 0);
//
//		Vector3 c5 = new Vector3(0, 0, 1);
//		Vector3 c6 = new Vector3(0, 1, 1);
//		Vector3 c7 = new Vector3(1, 0, 1);
//		Vector3 c8 = new Vector3(1, 1, 1);
//
//		model = createPolygonBox(modelBuilder, Color.GREEN, c1, c2, c3, c4, c5, c6, c7, c8);

		model = createPolygonBox(modelBuilder, StoneType.BERTRAM_3P5_X_11P5_WALL, Color.GREEN);

		
		instance = new ModelInstance(model);
		//instance = createDebugBoxOutline(c1, c2, c3, c4, c5, c6, c7, c8);
		
		BoundingBox bbox = new BoundingBox();
		instance.calculateBoundingBox(bbox);
		instance.transform.setToRotation(Vector3.Y, 35).trn(0, -bbox.min.y, 0);
		

		Gdx.input.setInputProcessor(new InputMultiplexer(this, inputController = new CameraInputController(cam)));
	}
	
	public Model createPolygonBox(ModelBuilder mb, StoneType st, Color color) {
		return createPolygonBox(mb, color, st.getC000(), st.getC010(), st.getC100(), st.getC110(), st.getC001(), st.getC011(), st.getC101(), st.getC111());
	}

	public Model createPolygonBox(ModelBuilder modelBuilder, Color color, Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {
		modelBuilder.begin();
		modelBuilder.part("box", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(color))).box(corner000, corner010, corner100, corner110, corner001, corner011, corner101, corner111);
		return modelBuilder.end();
	}

	@Override
	public void render() {
		inputController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);

		modelBatch.end();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
	}

	public boolean needsGL20() {
		return true;
	}

	public void resume() {
	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	private ModelInstance createDebugBoxOutline(
			Vector3 c000, Vector3 c010, Vector3 c100, Vector3 c110, 
			Vector3 c001, Vector3 c011, Vector3 c101, Vector3 c111) {

		Model model = null;

		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();

		MeshPartBuilder builder = modelBuilder.part("box", GL30.GL_LINES, Usage.Position | Usage.Color, new Material());

		builder.setColor(Color.MAGENTA);

		//bottom
		builder.line(c000, Color.GREEN,		c100, Color.GREEN);
		builder.line(c000, Color.BLUE,		c001, Color.BLUE);
		builder.line(c101, Color.RED,		c100, Color.RED);
		builder.line(c101, Color.MAGENTA,		c001, Color.MAGENTA);

		// top
		builder.line(c010, Color.GREEN,		c110, Color.GREEN);
		builder.line(c010, Color.BLUE,		c011, Color.BLUE);
		builder.line(c111, Color.RED,		c110, Color.RED);
		builder.line(c111, Color.MAGENTA,		c011, Color.MAGENTA);

		// sides
		builder.line(c000, Color.WHITE,		c010, Color.WHITE);
		builder.line(c100, Color.ORANGE,		c110, Color.ORANGE);
		builder.line(c001, Color.GRAY,		c011, Color.GRAY);
		builder.line(c101, Color.YELLOW,		c111, Color.YELLOW);

		model = modelBuilder.end();
		return new ModelInstance(model);
	}
	


}