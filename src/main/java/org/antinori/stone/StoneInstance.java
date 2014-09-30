package org.antinori.stone;

import org.antinori.lumber.Move;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class StoneInstance {
	
	private ModelInstance instance;
	private ModelInstance debugInstance;
	private StoneType type;
	private Color color = Color.BLUE; 

	public static boolean showDebug = true;
	
	public StoneInstance() {
	}

	public StoneInstance(ModelInstance instance, StoneType type) {
		super();
		this.instance = instance;
		this.type = type;

		createDebugBoxOutline(instance.transform);
	}
	
	public ModelInstance getInstance() {
		return instance;
	}
	public ModelInstance getDebugInstance() {
		return debugInstance;
	}
	public StoneType getType() {
		return type;
	}
	public void setInstance(ModelInstance instance) {
		this.instance = instance;
	}
	public void setDebugInstance(ModelInstance debugInstance) {
		this.debugInstance = debugInstance;
	}
	public void setType(StoneType type) {
		this.type = type;
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
	
	
	private void createDebugBoxOutline(Matrix4 center) {

		Model model = null;

		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();

		MeshPartBuilder builder = modelBuilder.part("box", GL30.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());

		builder.setColor(Color.GREEN);

		//bottom
		builder.line(type.getC000(), type.getC100());
		builder.line(type.getC000(), type.getC001());
		builder.line(type.getC101(), type.getC100());
		builder.line(type.getC101(), type.getC001());

		// top
		builder.line(type.getC010(), type.getC110());
		builder.line(type.getC010(), type.getC011());
		builder.line(type.getC111(), type.getC110());
		builder.line(type.getC111(), type.getC011());

		// sides
		builder.line(type.getC000(), type.getC010());
		builder.line(type.getC100(), type.getC110());
		builder.line(type.getC001(), type.getC011());
		builder.line(type.getC101(), type.getC111());

		model = modelBuilder.end();
		debugInstance = new ModelInstance(model, center);
	}
	
	public void turn(boolean turnLeft) {
		turn(this.instance, turnLeft);
		turn(this.debugInstance, turnLeft);
	}
	
	private void turn(ModelInstance inst, boolean turnLeft) {
		inst.transform.rotate(new Vector3(0, 1, 0), turnLeft? 1 : -1);
	}
	
	public void move(Move move, float inc) {
		move(move, this.instance,inc);
		move(move, this.debugInstance,inc);
	}
	
	private void move(Move move, ModelInstance inst, float inc) {
		
		Vector3 tmp = new Vector3();
		inst.transform.getTranslation(tmp);
		
		switch (move) {
		case MOVEXMINUS:
			inst.transform.setTranslation(tmp.x-inc,tmp.y,tmp.z);
			break;
		case MOVEXPLUS:
			inst.transform.setTranslation(tmp.x+inc,tmp.y,tmp.z);
			break;
		case MOVEYMINUS:
			inst.transform.setTranslation(tmp.x,tmp.y-inc,tmp.z);			
			break;
		case MOVEYPLUS:
			inst.transform.setTranslation(tmp.x,tmp.y+inc,tmp.z);
			break;
		case MOVEZMINUS:
			inst.transform.setTranslation(tmp.x,tmp.y,tmp.z-inc);
			break;
		case MOVEZPLUS:
			inst.transform.setTranslation(tmp.x,tmp.y,tmp.z+inc);
			break;
		default:
			break;
		}

	}
	
	

}
