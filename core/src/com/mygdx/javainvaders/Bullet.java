package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends SpaceEntity {

    static float throttle = 10f;
    protected int accelCount = 0;

    Bullet(World world, float initialX, float initialY){
        super(world, initialX, initialY);
        body.setAngularDamping(500);
        body.setLinearDamping(0.0f);

        float[] vertices = { 0,-3, 9,-3, 12,0, 9,3, 0,3 };
        setFixtures(vertices, 0.1f, 0.5f, 0.8f);
    }

    protected boolean pewpewdeath(){
        if (body.getWorldCenter().x > Gdx.graphics.getWidth())
            return true;

        if (body.getWorldCenter().x < 0)
            return true;

        //if spaceship goes past the top border
        if (body.getWorldCenter().y > Gdx.graphics.getHeight())
            return true;

        //if spaceship goes past the bottom border
        if (body.getWorldCenter().y < 0)
            return true;

        return false;
    }



}
