package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.World;

public class Spaceship extends SpaceEntity {

    private float throttle = 2500;
    private float angularVelocity = 2.5f;

    //just call SpaceEntity class constructor
    Spaceship(World world, float initialX, float initialY){
        super(world, initialX, initialY);
        body.setAngularDamping(3); //how long it takes to stop the rotation basically
        body.setLinearDamping(0.5f);
    }

    //handles input to spaceship control and draws it
    private void inputHandling(){
        //goes forward
        if( Gdx.input.isKeyPressed(Input.Keys.UP) ){
            body.applyForceToCenter(
                    (float)Math.cos( body.getAngle() ) * throttle,
                    (float)Math.sin( body.getAngle() ) * throttle,
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

    private void borderTeletransportation(){

        //if spaceship goes past the right border
        if( body.getWorldCenter().x > Gdx.graphics.getWidth() )
            body.setTransform( 0, body.getPosition().y , body.getAngle());

        //if spaceship goes past the left border
        if( body.getWorldCenter().x < 0 )
            body.setTransform( Gdx.graphics.getWidth(), body.getPosition().y , body.getAngle());

        //if spaceship goes past the top border
        if( body.getWorldCenter().y > Gdx.graphics.getHeight() )
            body.setTransform( body.getPosition().x, 0, body.getAngle() );

        //if spaceship goes past the bottom border
        if( body.getWorldCenter().y < 0 )
            body.setTransform( body.getPosition().x, Gdx.graphics.getHeight(), body.getAngle() );
    }

    void update(){
        inputHandling();
        borderTeletransportation();
        draw();
    }
}
