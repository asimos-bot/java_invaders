package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AsteroidGenerator {

    private World world;
    Camera camera;
    private Set<Asteroid> asteroids = new HashSet<Asteroid>();
    private int maxNumAsteroids;
    private Vector2 numVerticesRanges;
    private Vector2 verticesHeightRanges;
    private int youngestGeneration = Integer.MAX_VALUE;


    AsteroidGenerator(World world, Camera cam, int maxNumAsteroids, Vector2 numVerticesRanges, Vector2 verticesHeightRanges){

        //save configs for our asteroids
        this.world = world;
        this.camera = cam;
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
            initialY = Helper.randFloatRange( MaxHeightRange, camera.viewportHeight - MaxHeightRange );

            //left
            if( side == 0 ){
                initialX = -MaxHeightRange;
                //right
            }else{
                initialX = camera.viewportWidth + MaxHeightRange;
            }
        }else{
            initialX = Helper.randFloatRange( MaxHeightRange, camera.viewportWidth - MaxHeightRange );
            //bottom
            if( side == 1 ){
                initialY = -MaxHeightRange;
                //top
            }else{
                initialY = camera.viewportHeight + MaxHeightRange;
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

        newAsteroid.pushTowards( new Vector2( camera.viewportWidth/2, camera.viewportHeight/2 ), Asteroid.forceBase );

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

    private void addAsteroids(ArrayList<Asteroid> asteroidsToAdd){

        if ( asteroidsToAdd.size() > 0 ){

            Asteroid asteroidToAdd = asteroidsToAdd.remove(0);
            asteroids.add(asteroidToAdd);
            addAsteroids(asteroidsToAdd);

            if( asteroidToAdd.generationsLeft < youngestGeneration ) youngestGeneration = asteroidToAdd.generationsLeft;
        }
    }


    void update(ShapeRenderer shapeRenderer){

        if( asteroids.size() < maxNumAsteroids ){

            launchNewAsteroid();
        }

        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
        ArrayList<Asteroid> asteroidsToAdd = new ArrayList<Asteroid>();

        //reset
        if( asteroids.size() > maxNumAsteroids ){
            youngestGeneration = Integer.MAX_VALUE;
        }

        for(Asteroid asteroid : asteroids){

//            //put asteroid to be removed if it is offscreen
//            if( Helper.isOffScreen( asteroid.body.getWorldCenter(), asteroid.getHighestVertexHeight()*2 , camera) ||
//                    //overpopulation not good
//                    maxNumAsteroids < asteroids.size() - asteroidsToRemove.size() && asteroid.generationsLeft == youngestGeneration ){

            if( Helper.isOffScreen( asteroid.body.getWorldCenter(), asteroid.getHighestVertexHeight()*2 , camera)){

                asteroidsToRemove.add( asteroid );

            }else{

                //see if asteroid is dead an should split
                if( asteroid.getHealth() < 0 ){

                    ArrayList<Asteroid> childs = asteroid.split(3, 10);

                    asteroidsToRemove.add( asteroid );

                    if( childs == null ) continue;

                    for(Asteroid child : childs) asteroidsToAdd.add(child);
                }else{

                    //if we have a overpopulation of asteroids, start seeking the younger ones to kill
//                    if( asteroids.size() > maxNumAsteroids && asteroid.generationsLeft < youngestGeneration ){
//                        youngestGeneration = asteroid.generationsLeft;
//                    }

                    asteroid.draw(shapeRenderer);
                }
            }
        }

        removeAsteroids( asteroidsToRemove );
        addAsteroids( asteroidsToAdd );
    }
}
