package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AsteroidGenerator {

    private World world;
    private Set<Asteroid> asteroids = new HashSet<Asteroid>();
    private int maxNumAsteroids;
    private Vector2 numVerticesRanges;
    private Vector2 verticesHeightRanges;

    AsteroidGenerator(World world, int maxNumAsteroids, long timeGapBetweenLaunches, Vector2 numVerticesRanges, Vector2 verticesHeightRanges){

        //save configs for our asteroids
        this.world = world;
        this.maxNumAsteroids = maxNumAsteroids;
        this.numVerticesRanges = numVerticesRanges;
        this.verticesHeightRanges = verticesHeightRanges;
    }

    private Vector2 getRandomOriginPoint(float MaxHeightRange){
        //get random origin point
        int side = (int)Helper.randFloatRange( 0, 4 );

        float initialX=0, initialY=0;

        //for even values we go to the left and right side
        if( side % 2 == 0 ){
            initialY = Helper.randFloatRange( MaxHeightRange, Gdx.graphics.getHeight() - MaxHeightRange );

            //left
            if( side == 0 ){
                initialX = -MaxHeightRange;
                //right
            }else{
                initialX = Gdx.graphics.getWidth() + MaxHeightRange;
            }
        }else{
            initialX = Helper.randFloatRange( MaxHeightRange, Gdx.graphics.getWidth() - MaxHeightRange );
            //bottom
            if( side == 1 ){
                initialY = -MaxHeightRange;
                //top
            }else{
                initialY = Gdx.graphics.getHeight() + MaxHeightRange;
            }
        }
        return new Vector2( initialX, initialY );
    }

    //launches asteroids from a random side of the display and in a random direction (but vaguely towards the middle)
    //when the frameCounter reaches frameGapBetweenLaunches
    private void launchNewAsteroid(){

        //get random num of vertices
        int numVertices = (int)Helper.randFloatRange( numVerticesRanges.x, numVerticesRanges.y+1 );
        //get random height ranges (make asteroids general shapes vary more)
        float randomMinHeightRange = Helper.randFloatRange( verticesHeightRanges.x, verticesHeightRanges.y );
        float randomMaxHeightRange = Helper.randFloatRange( randomMinHeightRange, verticesHeightRanges.y );

        Asteroid newAsteroid = new Asteroid( world, 0, 0, numVertices, randomMinHeightRange, randomMaxHeightRange );

        Vector2 originPoint = getRandomOriginPoint(newAsteroid.getHighestVertexHeight());

        newAsteroid.body.setTransform( originPoint.x, originPoint.y, newAsteroid.body.getAngle() );

        newAsteroid.pushTowards( new Vector2( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 ), 10e6f );

        asteroids.add(newAsteroid);
    }

    private void removeAsteroids(ArrayList<Asteroid> asteroidsToRemove){

        if( asteroidsToRemove.size() > 0 ){

            Asteroid asteroidToRemove = asteroidsToRemove.remove(0);
            asteroids.remove(asteroidToRemove);
            world.destroyBody( asteroidToRemove.body );

            removeAsteroids(asteroidsToRemove);
        }
    }

    void update(){

        if( asteroids.size() < maxNumAsteroids ){

            launchNewAsteroid();
        }

        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();

        for(Asteroid asteroid : asteroids){

            //put asteroid to be removed if it is offscreen
            if( Helper.isOffScreen( asteroid.body.getWorldCenter(), asteroid.getHighestVertexHeight()*2 ) ){

                asteroidsToRemove.add( asteroid );

            }else{

                asteroid.draw();
            }
        }

        removeAsteroids( asteroidsToRemove );
    }
}
