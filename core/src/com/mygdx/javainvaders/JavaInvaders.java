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
	private World world; //box2d world object (handle the physics interactions in our game)

	//entities
	private Spaceship spaceship;

	@Override
	public void create () {

		//camera
		camera = new OrthographicCamera(30,
				30 * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();

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
		spaceship.setFixtures(vertices, 0.5f, 0.5f, 0.5f);
	}

	@Override
	public void render () {
		//update camera
		camera.update();

		//clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		if( Gdx.input.isTouched() ){
			System.out.println("x:" + Gdx.input.getX() + ", y:" + Gdx.input.getY());
		}
		//
		spaceship.rotate(200000000);
		spaceship.draw();

		//tell box2D to do its calculations
		world.step(1/60f, 6, 2);
	}
	
	@Override
	public void dispose () {

		//free memory
		world.dispose();
	}
}
