package com.mygdx.javainvaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Asteroid extends SpaceEntity {

    private float highestVertexHeight=0;
    private float lowestVertexHeight=Float.MAX_VALUE;
    private int generationsLeft = 3;

    Asteroid(World world, float initialX, float initialY, int numVertices, float minVertexHeight, float maxVertextHeight){
        super(world, initialX, initialY);
        body.setAngularDamping(0); //spinning for daaaaaaaays
        body.setLinearDamping(0);

        defineAsteroid( numVertices, minVertexHeight, maxVertextHeight );
    }
    Asteroid(World world, Vector2 initialPosition, int numVertices, Vector2 verticesHeightRanges){

        this( world, initialPosition.x, initialPosition.y, numVertices, verticesHeightRanges.x, verticesHeightRanges.y );
    }

    void defineAsteroid(int numVertices, float minVertexHeight, float maxVertexHeight){

        if( numVertices < 3 ) numVertices = 3;
        if( numVertices > 8 ) numVertices = 8;
        if( minVertexHeight < 0 ) minVertexHeight = 0;

        int numCoordinates = numVertices*2;

        //create vertices
        float[] vertices = new float[ numCoordinates ];

        //get angle between vertices to the center of the asteroid
        float AngleBetweenVertices = (float) Math.PI*2 / numVertices;

        float currentAngle = 0;

        float vertexMagnitude=0;
        float previousVertextMagnitude=0;

        //if previous vertex magnitude was less than the one before if
        //if the current vertex is greater than the previous then we'll
        //have a valley( not good because box2d don't support concave shapes )
        boolean valleyAlert=false;

        for(int i=0; i < numCoordinates; i+=2){

            //save previous vertex's magnitude
            previousVertextMagnitude = vertexMagnitude;

            //we need to be careful with this vector (else we end up with a valley )
            if( valleyAlert ){
                //the minimal magnitude will be the previous vector magnitude

                vertexMagnitude = Helper.randFloatRange( vertexMagnitude, maxVertexHeight );
                valleyAlert = false;
            }else{

                //get vertice's vector magnitude
                vertexMagnitude = Helper.randFloatRange( minVertexHeight, maxVertexHeight );
            }

            if( vertexMagnitude < previousVertextMagnitude ) valleyAlert = true;

            //decompose vector and get x
            vertices[i] = (float) Math.cos( currentAngle ) * vertexMagnitude;

            //decompose vector and get y
            vertices[i+1] = (float) Math.sin( currentAngle ) * vertexMagnitude;

            if( vertexMagnitude > highestVertexHeight ) highestVertexHeight = vertexMagnitude;
            if( vertexMagnitude < lowestVertexHeight ) lowestVertexHeight = vertexMagnitude;

            currentAngle -= AngleBetweenVertices;
        }

        //set asteroid shape
        setFixtures( vertices, 0.5f, 0.5f, 1f );
    }

    void launch(float positionX, float positionY, float forceX, float forceY, float torque){

        //set position
        body.setTransform( positionX, positionY, body.getAngle() );

        //apply force
        body.applyForceToCenter( forceX, forceY, true );

        //get that sweet spinning going
        rotate( torque );
    }

    void push(float forceX, float forceY){

        launch( body.getPosition().x, body.getPosition().y, forceX, forceY, body.getAngularVelocity() );
    }
    void push(Vector2 force){

        push(force.x, force.y);
    }

    void pushTowards(Vector2 point, float force){

        push(point.sub( body.getWorldCenter() ).setLength(force));
    }

    float getHighestVertexHeight(){
        return highestVertexHeight;
    }
    float getLowestVertexHeight(){ return lowestVertexHeight; }

}
