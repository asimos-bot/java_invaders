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

	//camera
	private OrthographicCamera camera;

	//Box2D
	private World world; //box2d world object

	private SpaceEntity spaceship;

	@Override
	public void create () {

		//camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();

		//set clear color
		Gdx.gl.glClearColor(0, 0, 0, 1);

		//initialize box2d for physics simulation
		Box2D.init();
		world = new World(new Vector2(0,0), true); //set world object (will do the physiscs)

		//create spaceship
		spaceship = new SpaceEntity(world,
				Gdx.graphics.getWidth()/2f,
				Gdx.graphics.getHeight()/2f);
		float[] vertices = { 0,0 , 10,3 , 20,0 , 10, 20 };
		spaceship.setFixtures(vertices, 0.5f, 0.5f, 0.5f);
	}

	@Override
	public void render () {
		camera.update();
		if( Gdx.input.isTouched() ){
			System.out.println("x:" + Gdx.input.getX() + ", y:" + Gdx.input.getY());
		}
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
