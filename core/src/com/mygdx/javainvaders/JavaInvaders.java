package com.mygdx.javainvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class JavaInvaders extends ApplicationAdapter {

	//device's display dimensions
	private int displayWidth, displayHeight;

	//Box2D
	private World world; //box2d world object

	private SpaceEntity spaceship;

	@Override
	public void create () {

		//get device's display dimensions
		displayWidth = Gdx.graphics.getWidth();
		displayHeight = Gdx.graphics.getHeight();

		//set clear color
		Gdx.gl.glClearColor(0, 0, 0, 1);

		//initialize box2d for physics simulation
		Box2D.init();
		world = new World(new Vector2(0,0), true); //set world object (will do the simulation)

		//create spaceship
		spaceship = new SpaceEntity(world, displayWidth/2f, displayHeight/2f);
		float[] vertices = { 0,0 , 10,3 , 20,0 , 10, 20 };
		spaceship.setFixtures(vertices, 0.5f, 0.5f, 0.5f);
	}

	@Override
	public void render () {
		//clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//render bounding boxes
		spaceship.draw();

		//tell box2D to step
		world.step(1/60f, 6, 2);
	}
	
	@Override
	public void dispose () {

		//free memory
		world.dispose();
	}
}
