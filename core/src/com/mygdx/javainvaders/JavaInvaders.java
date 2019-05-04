package com.mygdx.javainvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class JavaInvaders extends ApplicationAdapter {

	//Box2D
	private World world; //box2d world object (handle the physics interactions in our game)

	//entities
	private Spaceship spaceship;
	private AsteroidGenerator asteroidGenerator;

	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;

	@Override
	public void create () {

		//set clear color
		Gdx.gl.glClearColor(0, 0, 0, 1);

		//camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

		//initialize box2d for physics simulation
		Box2D.init();
		world = new World(new Vector2(0,0), true); //set world object (will do the physiscs)

        //debug
        debugRenderer = new Box2DDebugRenderer();

		//create spaceship
		spaceship = new Spaceship(world,
				Gdx.graphics.getWidth()/2f,
				Gdx.graphics.getHeight()/2f);
		float[] vertices = { -5,10 , 20,0 , -5,-10 , -3,0 };
		spaceship.setFixtures(vertices, 0.1f, 0.5f, 0.1f);

		asteroidGenerator = new AsteroidGenerator(world,
                5,
                2000,
                new Vector2(6, 8),
                new Vector2(30, 100)
        );

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {

				Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                float damage = contact.getWorldManifold().getNormal().len2();

                SpaceEntity EntityA = (SpaceEntity)bodyA.getUserData();
                SpaceEntity EntityB = (SpaceEntity)bodyB.getUserData();

                EntityA.health -= damage;
                EntityB.health -= damage;
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});
	}

	@Override
	public void render () {

        camera.update();

        //clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spaceship.update();
		asteroidGenerator.update();

		//debug
		//debugRenderer.render(world, camera.combined);

		//tell box2D to do its calculations
		world.step(1/60f, 6, 2);
	}
	
	@Override
	public void dispose () {

		//free memory
		world.dispose();
		debugRenderer.dispose();
	}
}
