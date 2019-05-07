package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {

    private boolean active;
    private MenuOption title;
    private MenuOption[] options;

    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();

    //title section will ocupy a quarter of the screen (includes gap between top and the options below) and the options the rest

    //the title itself will ocuppy three fifths of the title section

    Menu(String titleContent){

        float sideOffset = 10;

        active = false;
        String[] optionsContent = { "play" };

        //title

        float titleSectionHeight = Gdx.graphics.getHeight()/ 4;

        title = new MenuOption(
                titleContent,
                font,
                sideOffset,
                Gdx.graphics.getHeight() - titleSectionHeight/5,
                0,
                3*titleSectionHeight/5
        );
    }

    boolean isActive(){
        return active;
    }

    void update(){

        batch.begin();

        title.update(batch, false);

        batch.end();
    }

    void dispose(){

        font.dispose();
        batch.dispose();
    }
}
