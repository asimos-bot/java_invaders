package com.mygdx.javainvaders;

import com.badlogic.gdx.physics.box2d.World;

public class Asteroid extends SpaceEntity {

    Asteroid(World world, float initialX, float initialY){
        super(world, initialX, initialY);
        body.setAngularDamping(0); //spinning for daaaaaaaays
        body.setLinearDamping(0);
    }

    void defineAsteroid(int numVertices, float minVertexHeight, float maxVertexHeight){

        int numCoordinates = numVertices*2;

        //create vertices
        float[] vertices = new float[ numCoordinates ];

        //get angle between vertices to the center of the asteroid
        float AngleBetweenVertices = 360 / numVertices;

        float currentAngle = 0;

        for(int i=0; i < numCoordinates; i+=2){

            //get vertice's vector magnitude
            float VertexMagnitude = (float) (minVertexHeight + Math.random() * (maxVertexHeight - minVertexHeight));

            //decompose vector and get x
            vertices[i] = (float) Math.cos( Math.toRadians( currentAngle ) ) * VertexMagnitude;

            //decompose vector and get y
            vertices[i+1] = (float) Math.sin( Math.toRadians( currentAngle ) ) * VertexMagnitude;

            currentAngle += AngleBetweenVertices;
        }

        //set asteroid shape
        setFixtures( vertices, 0.5f, 0.5f, 1f );
    }

    void launchAsteroid(float positionX, float positionY, float forceX, float forceY, float torque){

        //set position
        body.setTransform( positionX, positionY, body.getAngle() );

        //apply force
        body.applyForceToCenter( forceX, forceY, true );

        //get that sweet spinning going
        rotate( torque );
    }

    void pushAsteroid(float forceX, float forceY){

        launchAsteroid( body.getPosition().x, body.getPosition().y, forceX, forceY, body.getAngularVelocity() );
    }
}
