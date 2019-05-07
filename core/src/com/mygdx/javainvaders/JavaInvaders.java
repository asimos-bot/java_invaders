package com.mygdx.javainvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class JavaInvaders extends ApplicationAdapter {

    //Box2D
    private World world; //box2d world object (handle the physics interactions in our game)

    //menu
    private Menu menu;


    //entities
    private Spaceship spaceship;
    private AsteroidGenerator asteroidGenerator;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        // world
        world = new World(new Vector2(0, 0f), true);
        // camera and renderers
        camera = new OrthographicCamera(64, 48);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
//        camera.zoom += 10f;
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);
        shapeRenderer = new ShapeRenderer();

        spaceship = new Spaceship(world, camera.viewportWidth / 2, camera.viewportHeight / 2);

        asteroidGenerator = new AsteroidGenerator(world,
                camera,
                40,
                new Vector2(4, 8),
                new Vector2(3, 8)
        );

        menu = new Menu("Java Invaders");

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                float damage = contact.getWorldManifold().getNormal().len2();

                SpaceEntity EntityA = (SpaceEntity) bodyA.getUserData();
                SpaceEntity EntityB = (SpaceEntity) bodyB.getUserData();
                if (EntityA.getClass().getName().equals("com.mygdx.javainvaders.Bullet") || EntityB.getClass().getName().equals("com.mygdx.javainvaders.Bullet")) {
                    damage *= 7;
                }

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
    public void render() {
        logicStep(1 / 60f);

        // Clear screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.identity();

        // draw entities
        asteroidGenerator.update(shapeRenderer);

        if( !menu.isActive() ) {
            spaceship.update(shapeRenderer, camera);
        }else{
            menu.update();
        }
        debugRenderer.render(world, camera.combined);

    }

    public void logicStep(float delta) {
        world.step(delta, 6, 2);
    }

    @Override
    public void dispose() {

        //free memory
        world.dispose();
        debugRenderer.dispose();
        menu.dispose();
    }
}
