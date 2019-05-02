package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashSet;
import java.util.Set;

public class AsteroidGenerator {

    private World world;
    private Set<Asteroid> asteroids = new HashSet<Asteroid>();
    private int gapBetweenLaunches;
    private int counter = 0;
    private int maxNumAsteroids;
    private Vector2 numVerticesRanges;
    private Vector2 verticesHeightRanges;

    AsteroidGenerator(World world, int maxNumAsteroids, int gapBetweenLaunches, Vector2 numVerticesRanges, Vector2 verticesHeightRanges){

        //save configs for our asteroids
        this.world = world;
        this.maxNumAsteroids = maxNumAsteroids;
        this.gapBetweenLaunches = gapBetweenLaunches;
        this.numVerticesRanges = numVerticesRanges;
        this.verticesHeightRanges = verticesHeightRanges;
    }

    void clearOffscreenAsteroids(){

        //TODO: add check to see if asteroid overlaps with screen rectangle and delete if it doesn't
    }

    private Vector2 getRandomOriginPoint(float randomMaxHeightRange){
        //get random origin point
        int side = (int)Helper.randFloatRange( 0, 4 );

        float initialX=0, initialY=0;

        //for even values we go to the left and right side
        if( side % 2 == 0 ){
            initialY = Helper.randFloatRange( randomMaxHeightRange, Gdx.graphics.getHeight() - randomMaxHeightRange );

            //left
            if( side == 0 ){
                initialX = -randomMaxHeightRange;
                //right
            }else{
                initialX = Gdx.graphics.getWidth() + randomMaxHeightRange;
            }
        }else{
            initialX = Helper.randFloatRange( randomMaxHeightRange, Gdx.graphics.getWidth() - randomMaxHeightRange );
            //bottom
            if( side == 1 ){
                initialY = -randomMaxHeightRange;
                //top
            }else{
                initialY = Gdx.graphics.getHeight() + randomMaxHeightRange;
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

        Vector2 originPoint = getRandomOriginPoint(randomMaxHeightRange);

        Asteroid newAsteroid = new Asteroid( world, originPoint.x, originPoint.y, numVertices, randomMinHeightRange, randomMaxHeightRange );
        newAsteroid.pushTowards( new Vector2( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 ), 10e6f );

        asteroids.add(newAsteroid);
    }
    void update(){

        counter++; //update counter
        if( counter == gapBetweenLaunches && asteroids.size() < maxNumAsteroids ){

            System.out.println("new asteroid launched!");
            counter=0;
            launchNewAsteroid();
        }

        for(Asteroid asteroid : asteroids){

            asteroid.draw();
            System.out.println( asteroid.body.getPosition().toString() );
        }
    }
}
