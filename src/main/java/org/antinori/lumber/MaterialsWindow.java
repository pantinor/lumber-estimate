package org.antinori.lumber;

import java.text.NumberFormat;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class MaterialsWindow extends Window {

    public MaterialsWindow(final Stage stage, FramingStudDesigner main, Skin skin) {
        super("Materials", skin);

        BitmapFont customFont = new BitmapFont(Gdx.files.classpath("skin/Arial_12.fnt"), false);
        customFont.setColor(Color.BLUE);

        defaults().padTop(2).padBottom(2).padLeft(5).padRight(5).left();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        List<LumberMaterial> list = main.getMaterialsList();
        for (LumberMaterial l : list) {
            add(new Label(l.getName(), skin, "default-font", l.getColor()));
            add(new Label(l.getCount() == 0 ? "" : l.getCount() + "", skin, "default-font", l.getColor()));

            String pc = l.getType() == null ? "" : "(" + formatter.format(l.getType().getCost()) + ")";

            add(new Label(pc, skin, "default-font", l.getColor()));
            add(new Label(formatter.format(l.getCost()), skin, "default-font", l.getColor()));
            row();
        }

        pack();

        DisplayMode dm = Gdx.graphics.getDisplayMode();

        this.setPosition(dm.width - this.getWidth(), 0);

        stage.addActor(this);
    }

}
