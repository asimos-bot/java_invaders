package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.physics.box2d.World;

public class Spaceship extends SpaceEntity {

    private float throttle = 2500;
    private float angularVelocity = 10000;

    //just call SpaceEntity class constructor
    Spaceship(World world, float initialX, float initialY){
        super(world, initialX, initialY);
        body.setAngularDamping(3); //get a better feel for the control

    }

    //handles input to spaceship control and draws it
    void inputHandling(){
        //goes forward
        if( Gdx.input.isKeyPressed(Input.Keys.UP) ){
            body.applyForceToCenter(
                    (float)Math.cos(Math.toRadians( body.getAngle()+90 )) * throttle,
                    (float)Math.sin(Math.toRadians( body.getAngle()+90 )) * throttle,
                    true
            );
        }
        //rotate
        if( Gdx.input.isKeyPressed(Input.Keys.LEFT) ){
            rotate(angularVelocity);
        }else if( Gdx.input.isKeyPressed(Input.Keys.RIGHT) ){
            rotate(-angularVelocity);
        }
    }
    void update(){
        inputHandling();
        draw();
    }
}
