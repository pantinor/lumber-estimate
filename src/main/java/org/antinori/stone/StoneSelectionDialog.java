package org.antinori.stone;


import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class StoneSelectionDialog extends Window {
	
	static int tfWidth = 35;
	static int btnWidth = 35;
	
	final private PatioDesigner main;
	
	final private TextField tfx ;
	final private TextField tfy ;
	final private TextField tfz ;
	
	final public SelectBox typeDropdown;
	final public SelectBox dropdown;

	private MaterialsWindow materials;
	private PointsWindow points;
			
	public StoneSelectionDialog(final Stage stage, PatioDesigner main, Skin skin) {
				
		super("Edit", skin);
		
		this.main = main;
		
				
		defaults().padTop(5).padBottom(5).padLeft(5).padRight(5).left();

		final CheckBox showDebug = new CheckBox(" Show Outlines", skin);
		final CheckBox showMaterials = new CheckBox(" Show Materials", skin);
		final CheckBox showPoints = new CheckBox(" Show Points", skin);

		showDebug.setChecked(true);
		showMaterials.setChecked(false);
		showPoints.setChecked(false);

	
		showDebug.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showDebug.isChecked()) {
					StoneInstance.showDebug = true;
				} else {
					StoneInstance.showDebug = false;
				}
			}
		});
		
		showMaterials.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showMaterials.isChecked()) {
					materials = new MaterialsWindow(StoneSelectionDialog.this.main.hud, StoneSelectionDialog.this.main, StoneSelectionDialog.this.main.skin);
				} else {
					if (materials != null) materials.remove();
				}
			}
		});
		
		showPoints.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showPoints.isChecked()) {
					points = new PointsWindow(StoneSelectionDialog.this.main.hud, StoneSelectionDialog.this.main, StoneSelectionDialog.this.main.skin);
				} else {
					if (points != null) points.remove();
				}
			}
		});
		
		tfx = createTextField("", skin);
		tfy = createTextField("", skin);
		tfz = createTextField("", skin);
		
		Button add = createButton("Add", skin, createAddListener());
		Button delete = createButton("Delete", skin, createDeleteListener());
		
		Button mxm = createButton("X -", skin, createMoveListener(Keys.Z));
		Button mxp = createButton("X +", skin, createMoveListener(Keys.A));
		Button mym = createButton("Y -", skin, createMoveListener(Keys.X));
		Button myp = createButton("Y +", skin, createMoveListener(Keys.S));
		Button mzm = createButton("Z -", skin, createMoveListener(Keys.C));
		Button mzp = createButton("Z +", skin, createMoveListener(Keys.D));
		
		
		typeDropdown = new SelectBox(skin);
		typeDropdown.setItems(StoneType.values());
		
		String[] names = main.modelInstances.keySet().toArray(new String[0]);
		dropdown = new SelectBox(skin);
		dropdown.setItems(names);
		dropdown.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				String selected = dropdown.getSelected().toString();

				StoneInstance si = StoneSelectionDialog.this.main.modelInstances.get(selected);
				Vector3 tmp = new Vector3();
				si.getInstance().transform.getTranslation(tmp);

				StoneSelectionDialog.this.main.cam.position.set(tmp.x-25,tmp.y+15,tmp.z-25);
				StoneSelectionDialog.this.main.cam.lookAt(tmp);

				StoneSelectionDialog.this.main.cam.update();
			}
		});		
		
		add(showDebug).colspan(2);
		row();
		
		add(showMaterials).colspan(4);
		row();
		
		add(showPoints).colspan(4);
		row();
		
		
		Table addBlockTable = new Table();
		addBlockTable.defaults().padTop(2).padBottom(2).padLeft(2).padRight(2).left();
		
		addBlockTable.add(new Label("Block Type:",skin)).space(3);
		addBlockTable.add(typeDropdown).fillX().colspan(4);
		addBlockTable.row();
		
		addBlockTable.add(new Label("Pos (X,Y,Z) inches:",skin)).space(3);
		addBlockTable.add(tfx).maxWidth(tfWidth).width(tfWidth);
		addBlockTable.add(tfy).maxWidth(tfWidth).width(tfWidth);
		addBlockTable.add(tfz).maxWidth(tfWidth).width(tfWidth);
		addBlockTable.row();
		
		
		addBlockTable.add(add).maxWidth(75).width(75).colspan(4);
		
		add(addBlockTable).colspan(4);
		row();

		
		add(new Label("Select Instance:",skin)).fillX().colspan(4);
		row();
		
		add(dropdown).fillX().colspan(4);
		row();
		
		add().space(3).colspan(1);
		add(mxp).maxWidth(btnWidth).width(btnWidth);
		add(myp).maxWidth(btnWidth).width(btnWidth);
		add(mzp).maxWidth(btnWidth).width(btnWidth);
		row();
		
		add(delete);
		add(mxm).maxWidth(btnWidth).width(btnWidth);
		add(mym).maxWidth(btnWidth).width(btnWidth);
		add(mzm).maxWidth(btnWidth).width(btnWidth);
		row();
		
		
		
		Button load = createButton("Load", skin, createLoadListener());
		Button save = createButton("Save", skin, createSaveListener());
		Table bottom = new Table();
		bottom.defaults().padTop(2).padBottom(2).padLeft(5).padRight(5).left();
		bottom.add(load).maxWidth(75).width(75);
		bottom.add(save).maxWidth(75).width(75);
		add(bottom).colspan(4);
		row();
		
				
		pack();


		stage.addActor(this);
		

	}
	
	private TextField createTextField(String text, Skin skin) {
		InputListener bl = new InputListener();
		TextField btn = new TextField(text, skin);
		btn.addListener(bl);

		return btn;	
	}
	
	private TextButton createButton(String text, Skin skin, InputListener listener) {
		TextButton btn = new TextButton(text, skin);
		btn.addListener(listener);
		return btn;	
	}
	
	private InputListener createAddListener() {
		InputListener listener = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				try {
					
					if (StringUtils.isBlank(tfx.getText()) || StringUtils.isBlank(tfy.getText()) || StringUtils.isBlank(tfz.getText())) return false;
				
					float tx = Float.parseFloat(tfx.getText());
					float ty = Float.parseFloat(tfy.getText());
					float tz = Float.parseFloat(tfz.getText());
	
					main.addBlock(StoneType.valueOf(typeDropdown.getSelected().toString()), tx, ty, tz);
	
					String[] names = main.modelInstances.keySet().toArray(new String[0]);
					dropdown.setItems(names);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return false;
			}
		};
		
		return listener;
	}
	
	private InputListener createDeleteListener() {
		InputListener listener = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				String selected = dropdown.getSelected().toString();

				main.modelInstances.remove(selected);
				
				String[] names = main.modelInstances.keySet().toArray(new String[0]);
				dropdown.setItems(names);
				
				return false;
			}
		};
		
		return listener;
	}
	
	private InputListener createMoveListener(final int key) {
		InputListener listener = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				main.keyDown(key);
				return false;
			}
		};
		
		return listener;
	}
	
	private InputListener createSaveListener() {
		InputListener listener = new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				try {
					FileHandle file = Gdx.files.local("stone-project.save");
					StringBuffer sb = new StringBuffer();
					for (String name : main.modelInstances.keySet()) {
						StoneInstance si = main.modelInstances.get(name);
						BoundingBox box = si.getInstance().calculateBoundingBox(new BoundingBox());
						Vector3 dims = box.getDimensions();
//						sb.append(name + "," + 
//									(si.getCenter().x - dims.x/2) + "," + 
//									(si.getCenter().y - dims.y/2) + "," + 
//									(si.getCenter().z - dims.z/2) + "," + 
//									dims.x + "," + 
//									dims.y + "," + 
//									dims.z + "\n");
					}
					file.writeString(sb.toString(), false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		};

		return listener;
	}

	private InputListener createLoadListener() {
		InputListener listener = new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				try {
					FileHandle file = Gdx.files.local("stone-project.save");
					String text = file.readString();
					String[] rooms = text.split("\\r?\\n");
					for (String room : rooms) {
						String[] params = room.split("[,]");
						float tx = Float.parseFloat(params[1]);
						float ty = Float.parseFloat(params[2]);
						float tz = Float.parseFloat(params[3]);
						main.addBlock(StoneType.LUXORA_3P8_X_8P6_WALL, tx, ty, tz);
					}
					
					String[] names = main.modelInstances.keySet().toArray(new String[0]);
					dropdown.setItems(names);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		};

		return listener;
	}

	

}
