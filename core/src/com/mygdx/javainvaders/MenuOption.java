package com.mygdx.javainvaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

class MenuOption extends TextButton{

    private boolean pressed = false;

    MenuOption(String text, TextButtonStyle textButtonStyle){

        super(text, textButtonStyle);

        addListener(new InputListener(){

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button){

                TextButtonStyle textButtonStyle = getStyle();
                textButtonStyle.fontColor = Color.LIGHT_GRAY;
                setStyle(textButtonStyle);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button){

                TextButtonStyle textButtonStyle = getStyle();
                textButtonStyle.fontColor = Color.WHITE;
                setStyle(textButtonStyle);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){

                press();
                return true;
            }
        });
    }

    private void press(){

        this.pressed = true;
    }

    public boolean getPress(){

        if( pressed ){
            pressed = false;
            return true;
        }
        return false;
    }
}
