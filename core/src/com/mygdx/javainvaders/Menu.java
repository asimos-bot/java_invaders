package com.mygdx.javainvaders;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

enum Option{

  none,
  play,
  exit
};

public class Menu extends Stage {

    private boolean active;
    private int leftOffset = 1;

    private float fontScale = 0.4f;
    private BitmapFont font;

    private Option selectedOption = Option.play;
    private Option choosenOption = Option.none;

    private String[] options = { "play", "exit" };

    Menu(String titleContent, Viewport viewport){

        super(viewport);
        active = true;

        //get font
        font = new BitmapFont(Gdx.files.internal("pixelart.fnt"));
        font.getData().setScale(fontScale);

        addActor(
                new Label(
                        titleContent,
                        new Label.LabelStyle(
                            font,
                            Color.WHITE
                        )
                )
        );

        //set title bounds
        getActors().get(0).setPosition(leftOffset,viewport.getCamera().viewportHeight - getActors().get(0).getHeight(), Align.topLeft);
    }

    boolean isActive(){
        return active;
    }

    public Option getChoosenOption(){ return choosenOption; }

    void update(){
        super.setDebugAll(true);
        super.draw();
    }
}
