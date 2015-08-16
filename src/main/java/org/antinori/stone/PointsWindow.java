package org.antinori.stone;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class PointsWindow extends Window {

    public PointsWindow(final Stage stage, PatioDesigner main, Skin skin) {
        super("Points", skin);

        defaults().padTop(2).padBottom(2).padLeft(5).padRight(5).left();

        String key = main.dialog.dropdown.getSelected().toString();

        StoneInstance si = main.modelInstances.get(key);

        Map<String, Vector3> vertices = collectModelNodeVertexPositions(si);

		//Map<Integer, Vector3> vertices = new HashMap<Integer, Vector3>();
        //collectModelNodeVertexPositions(node, vertices, new Vector3(1,1,1), position);
        add(new Label(si.getType().toString(), skin, "default-font", Color.BLACK));
        row();
        for (String k : vertices.keySet()) {
            add(new Label(k, skin, "default-font", Color.BLACK));
            add(new Label("" + vertices.get(k), skin, "default-font", Color.BLACK));
            row();
        }

        pack();

        DisplayMode dm = Gdx.graphics.getDesktopDisplayMode();

        this.setPosition(dm.width - this.getWidth(), 0);

        stage.addActor(this);
    }

    private Map<String, Vector3> collectModelNodeVertexPositions(StoneInstance si) {

        Map<String, Vector3> vertices = new HashMap<String, Vector3>();

        String id = si.getInstance().model.nodes.get(0).id;
        Node node = si.getInstance().getNode(id);

        Matrix4 transform = node.globalTransform;
        Vector3 positionOffset = si.getInstance().transform.getTranslation(new Vector3());

        Vector3 tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC000().x, si.getType().getC000().y, si.getType().getC000().z).add(positionOffset).mul(transform);
        vertices.put("c000", tmpPosition);

        tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC001().x, si.getType().getC001().y, si.getType().getC001().z).add(positionOffset).mul(transform);
        vertices.put("c001", tmpPosition);

        tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC100().x, si.getType().getC100().y, si.getType().getC100().z).add(positionOffset).mul(transform);
        vertices.put("c100", tmpPosition);

        tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC101().x, si.getType().getC101().y, si.getType().getC101().z).add(positionOffset).mul(transform);
        vertices.put("c101", tmpPosition);

        tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC010().x, si.getType().getC010().y, si.getType().getC010().z).add(positionOffset).mul(transform);
        vertices.put("c010", tmpPosition);

        tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC011().x, si.getType().getC011().y, si.getType().getC011().z).add(positionOffset).mul(transform);
        vertices.put("c011", tmpPosition);

        tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC110().x, si.getType().getC110().y, si.getType().getC110().z).add(positionOffset).mul(transform);
        vertices.put("c110", tmpPosition);

        tmpPosition = new Vector3();
        tmpPosition.set(si.getType().getC111().x, si.getType().getC111().y, si.getType().getC111().z).add(positionOffset).mul(transform);
        vertices.put("c111", tmpPosition);

        return vertices;

    }

    private void collectModelNodeVertexPositions(Node node, Map<Integer, Vector3> vertexMap, Vector3 scaleFactor, Vector3 positionOffset) {

        final Matrix4 transform = node.globalTransform;

        for (int i = 0; i < node.parts.size; ++i) {

            NodePart nodePart = node.parts.get(i);
            MeshPart meshPart = nodePart.meshPart;
            ShortBuffer indices = meshPart.mesh.getIndicesBuffer();
            FloatBuffer vertices = meshPart.mesh.getVerticesBuffer();
            final int strideInFloats = meshPart.mesh.getVertexSize() / (Float.SIZE / 8);

            for (int j = 0; j < meshPart.numVertices; ++j) {
                int index = indices.get(meshPart.indexOffset + j);
                int offset = index * strideInFloats;

                Vector3 tmpPosition = new Vector3();

                tmpPosition.set(vertices.get(offset), vertices.get(offset + 1), vertices.get(offset + 2))
                        .add(positionOffset)
                        .scl(scaleFactor)
                        .mul(transform);

                if (!vertexMap.containsValue(tmpPosition)) {
                    vertexMap.put(index, tmpPosition);
                }
            }
        }

    }

}
