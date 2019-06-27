package com.mygdx.javainvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

enum GameState{

    pre_playing,
    playing,
    animating,
    start_menu,
    pre_start_menu,
    pre_death_end,
    death_end
};

public class JavaInvaders extends ApplicationAdapter {

    //Box2D
    private World world; //box2d world object (handle the physics interactions in our game)

    //game world
    private Menu menu;
    GameState state = GameState.pre_start_menu;
    private DeathScreen death;

    //entities
    private Spaceship spaceship;
    private AsteroidGenerator asteroidGenerator;

    //rendering stuff
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Score score;
    private ScalingViewport viewport;

    @Override
    public void create() {
        // world
        world = new World(new Vector2(0, 0f), true);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                float damage = contact.getWorldManifold().getNormal().len2();

                SpaceEntity EntityA = (SpaceEntity) bodyA.getUserData();
                SpaceEntity EntityB = (SpaceEntity) bodyB.getUserData();

                EntityA.health -= damage;
                EntityB.health -= damage;

                if(state == GameState.playing) score.addScore((int)damage);
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

        // camera and renderers
        camera = new OrthographicCamera(64, 48);
        viewport = new ScalingViewport(Scaling.stretch, 64, 48, camera);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);

        //score
        score = new Score(viewport);

        //game objects
        shapeRenderer = new ShapeRenderer();
        asteroidGenerator = new AsteroidGenerator(world,
                camera,
                20,
                new Vector2(4, 8),
                new Vector2(3, 8)
        );

        //menu
        menu = new Menu("Java Invaders", viewport);
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

        //game state dependent
        switch(state){

            case pre_playing:
                //make spaceship reference to a new one
                spaceship = new Spaceship(world, camera);
                score.reset();
            case playing:
                score.update();
                state = spaceship.update(shapeRenderer, camera);
                break;

            case pre_start_menu:
                Gdx.input.setInputProcessor(menu);
                state = GameState.start_menu;
            case start_menu:

                state = menu.update();
                break;

            case pre_death_end:
                death = new DeathScreen(score.getScore(), viewport);
                Gdx.input.setInputProcessor(death);
                state = GameState.death_end;
            case death_end:
                state = death.update();
                break;
        }

        //draw asteroids
        asteroidGenerator.update(shapeRenderer);
    }

    public void logicStep(float delta) {
        world.step(delta, 6, 2);
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {

        //free memory
        world.dispose();
        menu.dispose();
        score.dispose();
    }
}
