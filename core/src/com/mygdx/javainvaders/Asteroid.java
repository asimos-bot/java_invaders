package com.mygdx.javainvaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Asteroid extends SpaceEntity {

    private float highestVertexHeight=0;
    private float lowestVertexHeight=Float.MAX_VALUE;
    private int numVertices;
    public int generationsLeft = 3;

    Asteroid(World world, float initialX, float initialY, int numVertices, float minVertexHeight, float maxVertextHeight){


        super(world, initialX, initialY);

        this.numVertices = numVertices;

        body.setAngularDamping(0); //spinning for daaaaaaaays
        body.setLinearDamping(0);

        defineAsteroid( numVertices, minVertexHeight, maxVertextHeight );
    }
    Asteroid(World world, Vector2 initialPosition, int numVertices, Vector2 verticesHeightRanges){

        this( world, initialPosition.x, initialPosition.y, numVertices, verticesHeightRanges.x, verticesHeightRanges.y );
    }

    void defineAsteroid(int numVertices, float minVertexHeight, float maxVertexHeight){
        int sf = 1;
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

            vertices[i] = (float) Math.cos( currentAngle ) * VertexMagnitude*sf;

            //decompose vector and get y
            vertices[i+1] = (float) Math.sin( currentAngle ) * VertexMagnitude*sf;
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
    int getNumVertices(){ return numVertices; }

    ArrayList<Asteroid> split(int numChilds, float gapBetweenChildVertexHeightRanges){

        if( generationsLeft == 0 ) return null;

        ArrayList<Asteroid> childs = new ArrayList<Asteroid>(numChilds);

        float angleBetweenSides = (float)Math.PI*2/numChilds; // Math.PI*2 / numChilds is the angle between

        float currentAngle = 0;

        float halfAngleSin = (float)Math.sin( angleBetweenSides/2 );

        //get childs vertices range
        float maxChildVertexHeight = halfAngleSin * lowestVertexHeight / ( 1 + halfAngleSin);
        float minChildVertexHeight = maxChildVertexHeight - gapBetweenChildVertexHeightRanges;

        minChildVertexHeight = minChildVertexHeight < 0 ? maxChildVertexHeight : minChildVertexHeight;

        float distanceParentChild = lowestVertexHeight - maxChildVertexHeight;

        for(int i=0; i < numChilds; i++){

            //get child origin point
            Vector2 childOriginPoint = body.getWorldCenter()
                    .add( new Vector2( distanceParentChild, 0 )
                            .rotateRad( angleBetweenSides ) );

            childs.add( new Asteroid( body.getWorld(),
                    childOriginPoint,
                    numVertices,
                    new Vector2( minChildVertexHeight, maxChildVertexHeight )
            ));

            childs.get(i).generationsLeft = this.generationsLeft-1;

            childs.get(i).push( new Vector2( (float)Math.cos(currentAngle), (float)Math.sin(currentAngle) ).scl(
                    childs.get(i).body.getMass()*10e3f
            ) );

            currentAngle += angleBetweenSides;
        }

        return childs;
    }
}
