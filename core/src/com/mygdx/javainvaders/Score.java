package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Score extends Stage {

    private float fontScale=1f;
    private Label score;
    private int scoreNumber=0;

    Score(Viewport viewport){

        super(viewport);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        addActor(table);

        score = new Label(
                    Integer.toString(scoreNumber),
                    new Label.LabelStyle(
                        new BitmapFont(Gdx.files.internal("pixelart.fnt")),
                        Color.DARK_GRAY
                    )
                );

        //add title
        table.add(score);
    }

    public void addScore(int number){

        scoreNumber+=number;
    }

    public int getScore(){
        return scoreNumber;
    }

    public void reset(){
        scoreNumber=0;
    }

    public void update(){

        score.setText(Integer.toString(scoreNumber));

        super.draw();
        super.act();
    }

    @Override
    public void dispose(){

        super.dispose();
    }
}
