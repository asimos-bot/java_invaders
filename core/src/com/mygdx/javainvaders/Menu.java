package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Menu extends Stage {

    private float fontScale = 0.4f;
    private BitmapFont font;
    private Skin skin;

    private Label title;
    private MenuOption playButton, exitButton;
    private Viewport viewport;

    Menu(String titleContent, Viewport viewport){

        super(viewport);
        this.viewport = viewport;

        //generate skin (the thing that store the styles used in our table)
        //NOTE: skin keep track of its resources by using the given name and also the type!
        skin = new Skin();

        font = new BitmapFont(Gdx.files.internal("pixelart.fnt"));
        font.getData().setScale(fontScale);

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        //pixmap.fill();
        skin.add("white", new Texture(pixmap));

        //add font to skin
        skin.add("default", font);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        addActor(table);

        //textButtonStyle (for buttons)
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        //LabelStyle
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.RED);
        skin.add("default", labelStyle);

        //add title
        title = new Label(titleContent, skin);
        table.add(title);
        table.row().fill();

        //add play option and its listeners
        playButton = new MenuOption("play", new TextButton.TextButtonStyle(textButtonStyle));
        table.add(playButton);
        table.row().fill();

        //add exit option
        exitButton = new MenuOption("exit", new TextButton.TextButtonStyle(textButtonStyle));
        table.add(exitButton);
    }

    GameState update(){
        //setDebugAll(true);

        super.draw();
        super.act(); //poll events in actors

        //check if game state need updating
        if( playButton.getPress() ){

            return GameState.pre_playing;

        }else if( exitButton.getPress() ){
            Gdx.app.exit();
        }
        return GameState.start_menu;
    }

    @Override
    public void dispose(){
        super.dispose();
        skin.dispose();
    }
}
