package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DeathScreen extends Stage {

    private Label title;
    private Label scoreLabel;
    private MenuOption playAgainButton;
    private MenuOption menuButton;
    private BitmapFont font;
    private float fontScale = 0.4f;

    private Skin skin;

    DeathScreen(int score, Viewport viewport){

        super(viewport);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        addActor(table);

        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        //pixmap.fill();
        skin.add("white", new Texture(pixmap));

        font = new BitmapFont(Gdx.files.internal("pixelart.fnt"));
        font.getData().setScale(fontScale);

        //add font to skin
        skin.add("default", font);

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

        //add score
        scoreLabel = new Label(Integer.toString(score), skin);
        table.add(scoreLabel);
        table.row().fill();

        //add title
        title = new Label("game over", skin);
        table.add(title);
        table.row().fill();

        //add play option and its listeners
        playAgainButton = new MenuOption("play again", new TextButton.TextButtonStyle(textButtonStyle));
        table.add(playAgainButton);
        table.row().fill();

        //add exit option
        menuButton = new MenuOption("menu", new TextButton.TextButtonStyle(textButtonStyle));
        table.add(menuButton);
    }

    GameState update(){
        //setDebugAll(true);
        super.draw();
        super.act(); //poll events in actors

        //check if game state need updating
        if( playAgainButton.getPress() ){
            dispose();
            return GameState.pre_playing;

        }else if( menuButton.getPress() ){
            dispose();
            return GameState.pre_start_menu;
        }
        return GameState.death_end;
    }

    @Override
    public void dispose(){
        super.dispose();
        skin.dispose();
    }
}
