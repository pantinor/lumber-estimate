package org.antinori.lumber;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class MaterialsWindow extends Window {
	

	public MaterialsWindow(final Stage stage, FramingStudDesigner main, Skin skin) {
		super("Materials", skin);
		
		defaults().padTop(5).padBottom(5).padLeft(5).padRight(5).left();
				
		List<String> list = main.getMaterialsList();
		for (String l : list) {
			add(new Label(l, skin));
			row();
		}
				
		pack();
		
		DisplayMode dm = Gdx.graphics.getDesktopDisplayMode();

		this.setPosition(dm.width - this.getWidth(), 0);

		stage.addActor(this);
	}

}
