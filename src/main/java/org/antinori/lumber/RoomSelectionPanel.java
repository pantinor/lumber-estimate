package org.antinori.lumber;

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
		
		//add().space(3).colspan(4);
		//row();
		
		Table addRoomTable = new Table();
		addRoomTable.defaults().padTop(2).padBottom(2).padLeft(2).padRight(2).left();
		
		addRoomTable.add(new Label("Pos (X,Y,Z) inches:",skin)).space(3);
		addRoomTable.add(tfx).maxWidth(tfWidth).width(tfWidth);
		addRoomTable.add(tfy).maxWidth(tfWidth).width(tfWidth);
		addRoomTable.add(tfz).maxWidth(tfWidth).width(tfWidth);
		addRoomTable.row();
		
		addRoomTable.add(new Label("Dim (W,H,L) feet:",skin)).space(3);
		addRoomTable.add(tfw).maxWidth(tfWidth).width(tfWidth);
		addRoomTable.add(tfh).maxWidth(tfWidth).width(tfWidth);
		addRoomTable.add(tfl).maxWidth(tfWidth).width(tfWidth);
		addRoomTable.row();
		
		addRoomTable.add(addRoom).maxWidth(75).width(75).colspan(4);
		
		add(addRoomTable).colspan(4);
		row();
		
		//add().space(3).colspan(4);
		//row();
		
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
	
	private InputListener createSaveListener() {
		InputListener listener = new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				try {
					FileHandle file = Gdx.files.local("lumber-project.save");
					StringBuffer sb = new StringBuffer();
					for (String name : main.boxes.keySet()) {
						Room room = main.boxes.get(name);
						BoundingBox box = room.getInstance().calculateBoundingBox(new BoundingBox());
						Vector3 dims = box.getDimensions();
						sb.append(name + "," + 
									(room.getCenter().x - dims.x/2) + "," + 
									(room.getCenter().y - dims.y/2) + "," + 
									(room.getCenter().z - dims.z/2) + "," + 
									dims.x + "," + 
									dims.y + "," + 
									dims.z + "\n");
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
					FileHandle file = Gdx.files.local("lumber-project.save");
					String text = file.readString();
					String[] rooms = text.split("\\r?\\n");
					for (String room : rooms) {
						String[] params = room.split("[,]");
						float tx = Float.parseFloat(params[1]);
						float ty = Float.parseFloat(params[2]);
						float tz = Float.parseFloat(params[3]);
						float tw = Float.parseFloat(params[4]);
						float th = Float.parseFloat(params[5]);
						float tl = Float.parseFloat(params[6]);
						main.addRoom(tx, ty, tz, tw, th, tl);
					}
					
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

	

}
