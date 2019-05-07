package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuOption {

    private String content;
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();
    private float fontScaleX = 1;
    private float fontScaleY = 1;
    public float x, y, width, height, initialScreenWidth, initialScreenHeight;

    MenuOption(String content, BitmapFont font, float x, float y, float initialWidth, float initialHeight) {

        this.content = content;
        this.font = font;

        layout.setText(font, content);

        fontScaleX = initialWidth / layout.width;
        fontScaleY = initialHeight / layout.height;

        //in case height was not given
        if (initialHeight <= 0 && initialWidth != 0) {
            fontScaleY = fontScaleX;
            initialHeight = layout.height * fontScaleY;
        }
        //in case width was not given
        else if (initialWidth <= 0 && initialHeight != 0) {
            fontScaleX = fontScaleY;
            initialWidth = layout.width * fontScaleX;
        }
        this.x = x;
        this.y = y;
        this.width = initialWidth;
        this.height = initialHeight;
        this.initialScreenWidth = Gdx.graphics.getWidth();
        this.initialScreenHeight = Gdx.graphics.getHeight();
    }

    void draw(SpriteBatch batch, boolean focus){

        //batch.begin() and batch.end() should be called outside this method
        font.getData().setScale( fontScaleX, fontScaleY );
        font.setColor( focus ? Color.RED : Color.WHITE );

        layout.setText(font, content);
        this.width = layout.width;
        this.height = layout.height;

        font.draw(batch, content, x, y );
    }

    void update(SpriteBatch batch, boolean focus){

        draw( batch, focus );
    }
}
