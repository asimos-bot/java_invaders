package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends SpaceEntity {

    static float throttle = 500f;
    protected int accelCount = 0;
    private float initHealth;

    Bullet(World world, float initialX, float initialY){
        super(world, initialX, initialY);
        initHealth = this.health;
        body.setAngularDamping(500);
        body.setLinearDamping(0.0f);

        float sf = 2*10e-2f; // Scaling factor
        float[] vertices = { -1*sf,0, 1*sf,0, 1*sf,3*sf, 0,4*sf, -1*sf,3*sf};
        setFixtures(vertices, 10f, 0.5f, 0.8f);
    }

    protected boolean pewpewdeath(Camera cam){
        if (body.getWorldCenter().x > cam.viewportWidth)
            return true;

        if (body.getWorldCenter().x < 0)
            return true;

        //if spaceship goes past the top border
        if (body.getWorldCenter().y > cam.viewportHeight)
            return true;

        //if spaceship goes past the bottom border
        if (body.getWorldCenter().y < 0)
            return true;

        if (this.health < initHealth)
            return true;

        return false;
    }



}
