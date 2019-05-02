package com.mygdx.javainvaders;

import com.badlogic.gdx.physics.box2d.World;

public class Asteroid extends SpaceEntity {

    Asteroid(World world, float initialX, float initialY){
        super(world, initialX, initialY);
        body.setAngularDamping(0); //spinning for daaaaaaaays
        body.setLinearDamping(0);
    }

    void defineAsteroid(int numVertices, float minVertexHeight, float maxVertexHeight){

        if( numVertices < 3 ) numVertices = 3;
        if( minVertexHeight < 0 ) minVertexHeight = 0;

        int numCoordinates = numVertices*2;

        //create vertices
        float[] vertices = new float[ numCoordinates ];

        //get angle between vertices to the center of the asteroid
        float AngleBetweenVertices = (float) Math.PI*2 / numVertices;

        float currentAngle = 0;

        float VertexMagnitude=0;
        float previousVertextMagnitude=0;

        //if previous vertex magnitude was less than the one before if
        //if the current vertex is greater than the previous then we'll
        //have a valley( not good because box2d don't support concave shapes )
        boolean valleyAlert=false;

        for(int i=0; i < numCoordinates; i+=2){

            //get vertice's vector magnitude
            VertexMagnitude = (float) (minVertexHeight + Math.random() * (maxVertexHeight - minVertexHeight));

            //decompose vector and get x
            vertices[i] = (float) Math.cos( currentAngle ) * VertexMagnitude;

            //decompose vector and get y
            vertices[i+1] = (float) Math.sin( currentAngle ) * VertexMagnitude;

            currentAngle -= AngleBetweenVertices;
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
