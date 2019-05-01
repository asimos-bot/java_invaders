package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.World;

public class Spaceship extends SpaceEntity {

    private int throttle = 10000;

    //just call SpaceEntity class constructor
    Spaceship(World world, float initialX, float initialY){
        super(world, initialX, initialY);
    }

    //handles input to spaceship control and draws it
    void update(){
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
            rotate(throttle*10);
        }else if( Gdx.input.isKeyPressed(Input.Keys.RIGHT) ){
            rotate(-throttle*10);
        }
        draw();
    }
}
