package com.mygdx.javainvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class JavaInvaders extends ApplicationAdapter {

    //Box2D
    private World world; //box2d world object (handle the physics interactions in our game)

    //entities
    private Spaceship spaceship;
    private Asteroid asteroid;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    @Override
    public void create() {
        Bullet.init();
        //set clear color
        Gdx.gl.glClearColor(0, 0, 0, 1);

        //camera
        float width = 20; // 20 meters
        //You can calculate on your chosen width or height to maintain aspect ratio
        float height = (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()) * width;
        camera = new OrthographicCamera(width, height);
        //Now the center of the camera is on 0,0 in the game world. It's often more desired and practical to have it's bottom left corner start out on 0,0
        //All we need to do is translate it by half it's width and height since that is the offset from it's center point (and that is currently set to 0,0.
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);


        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH / Constants.PPM, Constants.VIEWPORT_HEIGHT / Constants.PPM);

        camera.update();
        //initialize box2d for physics simulation
        Box2D.init();
        world = new World(new Vector2(0, 0), true); //set world object (will do the physiscs)

        //debug
        debugRenderer = new Box2DDebugRenderer();

        //create spaceship
        spaceship = new Spaceship(world,
                Gdx.graphics.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f);
        float[] vertices = {-5, 10, 20, 0, -5, -10, -3, 0};
        spaceship.setFixtures(vertices, 0.2f, 0.5f, 0.1f);

        asteroid = new Asteroid(world, 200, 200);
        asteroid.defineAsteroid(8, 80, 20);
        asteroid.pushAsteroid(100000, 100000);
    }

    @Override
    public void render() {

        camera.update();

        //clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spaceship.update();
        asteroid.draw();

        //debug
        debugRenderer.render(world, camera.combined);

        //tell box2D to do its calculations
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void dispose() {

        //free memory
        world.dispose();
    }
}
