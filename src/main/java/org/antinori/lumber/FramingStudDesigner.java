package org.antinori.lumber;

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
import com.badlogic.gdx.graphics.GL30;
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
import com.badlogic.gdx.math.Vector3;

public class FramingStudDesigner extends SimpleGame {

    public Environment environment;

    public Map<String, Room> boxes = new HashMap<String, Room>();
    RoomSelectionPanel dialog;
    boolean fullscreen = false;

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "FramingStudDesigner";
        cfg.useGL30 = false;
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

        float width = 23 * 12;
        float length = 18 * 12;
        float height = 10 * 12;

        addRoom(0, 0, 0, width, height, length);
        addRoom(0, 0, 20 * 12, 12 * 12, 10 * 12, 8 * 12);

        createAxes();

        dialog = new RoomSelectionPanel(hud, this, skin);
        hud.addActor(dialog);

        cam.position.set(-250, 150, 0);
        cam.lookAt(0, 150, 0);

    }

    @Override
    public void draw(float delta) {

        cam.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

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

        Room room = new Room(builder, Color.BLUE, x, y, z, width, height, length);
        boxes.put(name, room);

        return name;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (dialog.dropdown.getSelected() != null) {

            String key = dialog.dropdown.getSelected().toString();
            Room room = boxes.get(key);

            if (keycode == Keys.X) {

                room.move(Move.MOVEYMINUS);

            } else if (keycode == Keys.S) {

                room.move(Move.MOVEYPLUS);

            } else if (keycode == Keys.C) {

                room.move(Move.MOVEZMINUS);

            } else if (keycode == Keys.D) {

                room.move(Move.MOVEZPLUS);

            } else if (keycode == Keys.Z) {

                room.move(Move.MOVEXMINUS);

            } else if (keycode == Keys.A) {

                room.move(Move.MOVEXPLUS);

            }
        }

        if (keycode == Keys.NUMPAD_0) {

            cam.position.set(-100, 150, -100);
            cam.lookAt(150, 0, 150);
            cam.update();

        } else if (keycode == Keys.NUMPAD_4) {

            cam.position.set(cam.position.x + 5 * 12, cam.position.y, cam.position.z);
            lookAtSelectedRoom();

        } else if (keycode == Keys.NUMPAD_1) {

            cam.position.set(cam.position.x - 5 * 12, cam.position.y, cam.position.z);
            lookAtSelectedRoom();

        } else if (keycode == Keys.NUMPAD_5) {

            cam.position.set(cam.position.x, cam.position.y + 5 * 12, cam.position.z);
            lookAtSelectedRoom();

        } else if (keycode == Keys.NUMPAD_2) {

            cam.position.set(cam.position.x, cam.position.y - 5 * 12, cam.position.z);
            lookAtSelectedRoom();

        } else if (keycode == Keys.NUMPAD_6) {

            cam.position.set(cam.position.x, cam.position.y, cam.position.z + 5 * 12);
            lookAtSelectedRoom();

        } else if (keycode == Keys.NUMPAD_3) {

            cam.position.set(cam.position.x, cam.position.y, cam.position.z - 5 * 12);
            lookAtSelectedRoom();

        } else if (keycode == Keys.ESCAPE) {

            if (fullscreen) {
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

    private void lookAtSelectedRoom() {

        if (dialog.dropdown.getSelected() != null) {

            String key = dialog.dropdown.getSelected().toString();
            Room room = boxes.get(key);
            Vector3 tmp = new Vector3();
            room.getInstance().transform.getTranslation(tmp);
			//cam.normalizeUp();
            //cam.lookAt(tmp);
            //cam.up.nor();

            cam.update();
        }
    }

    final float GRID_MIN = -12 * 1000;
    final float GRID_MAX = 12 * 1000;
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

    public List<LumberMaterial> getMaterialsList() {

        List<LumberMaterial> list = new ArrayList<LumberMaterial>();

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
                    c++;
                    counts.put(type, c);
                }
                for (Lumber l : wall.getBottomPieces()) {
                    LumberType type = l.getType();
                    int c = counts.get(type);
                    c++;
                    counts.put(type, c);
                }
                for (Lumber l : wall.getVerticalPieces()) {
                    LumberType type = l.getType();
                    int c = counts.get(type);
                    c++;
                    counts.put(type, c);
                }
            }
        }

        float total = 0;
        for (LumberType type : LumberType.values()) {
            int count = counts.get(type);
            if (count < 1) {
                continue;
            }
            float cost = count * type.getCost();
            list.add(new LumberMaterial(type.getName(), type, count, cost));
            total += cost;
        }

        list.add(new LumberMaterial("Total Cost", null, 0, total, Color.YELLOW));

        return list;
    }

}
