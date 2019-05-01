package com.mygdx.javainvaders;

import com.badlogic.gdx.physics.box2d.World;

public class Spaceship extends SpaceEntity {

    //just call SpaceEntity class constructor
    Spaceship(World world, float initialX, float initialY){
        super(world, initialX, initialY);
    }
}
