package com.mygdx.javainvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class JavaInvaders extends ApplicationAdapter {

	//Box2D
	private World world; //box2d world object (handle the physics interactions in our game)

	//entities
	private Spaceship spaceship;
	private Asteroid asteroid;

	@Override
	public void create () {

		//set clear color
		Gdx.gl.glClearColor(0, 0, 0, 1);

		//initialize box2d for physics simulation
		Box2D.init();
		world = new World(new Vector2(0,0), true); //set world object (will do the physiscs)

		//create spaceship
		spaceship = new Spaceship(world,
				Gdx.graphics.getWidth()/2f,
				Gdx.graphics.getHeight()/2f);
		float[] vertices = { -10,-4f , 0,17f , 10,-4f , 0,-2f };
		spaceship.setFixtures(vertices, 0.1f, 0.5f, 1f);

		asteroid = new Asteroid(world, 50, 50);
		asteroid.defineAsteroid( 8, 80, 20 );
		asteroid.pushAsteroid( 100000,100000 );
	}

	@Override
	public void render () {

		//clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spaceship.update();
		asteroid.draw();

		//tell box2D to do its calculations
		world.step(1/60f, 6, 2);
	}
	
	@Override
	public void dispose () {

		//free memory
		world.dispose();
	}
}
