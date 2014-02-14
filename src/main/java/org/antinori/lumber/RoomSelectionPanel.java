package org.antinori.lumber;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class RoomSelectionPanel extends Window {
	
	static int tfWidth = 35;
	static int btnWidth = 35;
	
	final private FramingStudDesigner main;
	
	final private TextField tfx ;
	final private TextField tfy ;
	final private TextField tfz ;
	final private TextField tfw ;
	final private TextField tfh ;
	final private TextField tfl ;
	
	final public SelectBox dropdown;
	
	private MaterialsWindow materials;
			
	public RoomSelectionPanel(final Stage stage, FramingStudDesigner main, Skin skin) {
				
		super("Edit", skin);
		
		this.main = main;
		
				
		defaults().padTop(5).padBottom(5).padLeft(5).padRight(5).left();

		
		final CheckBox showRooms = new CheckBox(" Show Rooms", skin);
		final CheckBox showWalls = new CheckBox(" Show Walls", skin);
		final CheckBox showStuds = new CheckBox(" Show Studs", skin);
		final CheckBox showDebugStuds = new CheckBox(" Show Stud Outlines", skin);
		
		final CheckBox showMaterials = new CheckBox(" Show Materials", skin);

		
		showRooms.setChecked(Room.roomIsVisible);
		showWalls.setChecked(Room.wallIsVisible);
		showStuds.setChecked(Room.studIsVisible);
		showDebugStuds.setChecked(Room.studDebugIsVisible);

		
		showRooms.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showRooms.isChecked()) {
					Room.roomIsVisible = true;
				} else {
					Room.roomIsVisible = false;
				}
			}
		});
		
		showWalls.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showWalls.isChecked()) {
					Room.wallIsVisible = true;
				} else {
					Room.wallIsVisible = false;
				}
			}
		});
		
		showStuds.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showStuds.isChecked()) {
					Room.studIsVisible = true;
				} else {
					Room.studIsVisible = false;
				}
			}
		});
		
		showDebugStuds.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showDebugStuds.isChecked()) {
					Room.studDebugIsVisible = true;
				} else {
					Room.studDebugIsVisible = false;
				}
			}
		});
		
		showMaterials.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (showMaterials.isChecked()) {
					materials = new MaterialsWindow(RoomSelectionPanel.this.main.hud, RoomSelectionPanel.this.main, RoomSelectionPanel.this.main.skin);
				} else {
					materials.remove();
				}
			}
		});
		
		tfx = createTextField("", skin);
		tfy = createTextField("", skin);
		tfz = createTextField("", skin);
		tfw = createTextField("", skin);
		tfh = createTextField("", skin);
		tfl = createTextField("", skin);
		
		
		Button addRoom = createButton("Add Room", skin, createAddRoomListener());
		Button deleteRoom = createButton("Delete Room", skin, createDeleteRoomListener());
		
		Button mxm = createButton("X -", skin, createMoveListener(Keys.Z));
		Button mxp = createButton("X +", skin, createMoveListener(Keys.A));
		Button mym = createButton("Y -", skin, createMoveListener(Keys.X));
		Button myp = createButton("Y +", skin, createMoveListener(Keys.S));
		Button mzm = createButton("Z -", skin, createMoveListener(Keys.C));
		Button mzp = createButton("Z +", skin, createMoveListener(Keys.D));
		
		String[] names = main.boxes.keySet().toArray(new String[0]);
		
		dropdown = new SelectBox(names, skin);
		
		
		dropdown.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				String selected = dropdown.getSelection();

				Room room = RoomSelectionPanel.this.main.boxes.get(selected);
				Vector3 tmp = new Vector3();
				room.getInstance().transform.getTranslation(tmp);

				RoomSelectionPanel.this.main.cam.position.set(tmp.x-250,tmp.y+150,tmp.z-250);
				RoomSelectionPanel.this.main.cam.lookAt(tmp);

				RoomSelectionPanel.this.main.cam.update();
			}
		});
		
		
		
		

		add(showRooms).colspan(2);
		add(showWalls).colspan(2);
		row();
		
		add(showDebugStuds).colspan(2);
		add(showStuds).colspan(2);
		row();
		
		add(showMaterials).colspan(4);
		row();
		
		add(new Label("Position:",skin)).space(3);
		add(tfx).maxWidth(tfWidth).width(tfWidth);
		add(tfy).maxWidth(tfWidth).width(tfWidth);
		add(tfz).maxWidth(tfWidth).width(tfWidth);
		row();
		
		add(new Label("Dimensions:",skin)).space(3);
		add(tfw).maxWidth(tfWidth).width(tfWidth);
		add(tfh).maxWidth(tfWidth).width(tfWidth);
		add(tfl).maxWidth(tfWidth).width(tfWidth);
		row();
		
		add(addRoom).colspan(4);
		row();
		
		add(new Label("Select Room:",skin)).fillX().colspan(4);
		row();
		
		add(dropdown).fillX().colspan(4);
		row();
		
		add().space(3).colspan(1);
		add(mxp).maxWidth(btnWidth).width(btnWidth);
		add(myp).maxWidth(btnWidth).width(btnWidth);
		add(mzp).maxWidth(btnWidth).width(btnWidth);
		row();
		
		add(deleteRoom);//.colspan(1);
		add(mxm).maxWidth(btnWidth).width(btnWidth);
		add(mym).maxWidth(btnWidth).width(btnWidth);
		add(mzm).maxWidth(btnWidth).width(btnWidth);
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
	
	private InputListener createAddRoomListener() {
		InputListener listener = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				try {
				
					float tx = Float.parseFloat(tfx.getText());
					float ty = Float.parseFloat(tfy.getText());
					float tz = Float.parseFloat(tfz.getText());
					float tw = Float.parseFloat(tfw.getText());
					float th = Float.parseFloat(tfh.getText());
					float tl = Float.parseFloat(tfl.getText());
	
					main.addRoom(tx,ty,tz,tw*12,th*12,tl*12);
	
					String[] names = main.boxes.keySet().toArray(new String[0]);
					dropdown.setItems(names);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return false;
			}
		};
		
		return listener;
	}
	
	private InputListener createDeleteRoomListener() {
		InputListener listener = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				String selected = dropdown.getSelection();

				main.boxes.remove(selected);
				
				String[] names = main.boxes.keySet().toArray(new String[0]);
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

	

}
