package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Spaceship extends SpaceEntity {

    private float throttle = 9f;
    private float angularVelocity = 4f;

    private List<com.mygdx.javainvaders.Bullet> bullets = new ArrayList<com.mygdx.javainvaders.Bullet>();
    private int maxBullets = 5;
    private float bulletInterval = 0.5f * 10e2f;
    private long bulletTime = System.currentTimeMillis();

    private World world;
    private Camera camera;
    private GameState state = GameState.animating;

    private boolean THREAD_FLAG = false;

    //just call SpaceEntity class constructor
    Spaceship(World world, Camera camera, boolean tf) {
        this.world = world;
        this.camera = camera;
        this.THREAD_FLAG = tf;
    }

    void create(){
        super.create(world, camera.viewportWidth/2, camera.viewportHeight/2);
        this.world = world;
        body.setAngularDamping(8f); //how long it takes to stop the rotation basically
        body.setLinearDamping(0.5f);
        float sf = 1;
        float[] vertices = {-1*sf,0, 0,0.5f*sf, 1*sf,0, 0,2*sf};
        this.setFixtures(vertices, 0.1f, 0.5f, 0.1f);
    }

    void moveForward(){
        body.applyForceToCenter(
                (float) -Math.sin(body.getAngle()) * throttle,
                (float) Math.cos(body.getAngle()) * throttle,
                true
        );
    }

    //handles input to spaceship control and draws it
    private void inputHandling(Camera cam) {

        /*
            KEYBOARD
         */

        //goes forward
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveForward();
        }
        //rotate
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotate(angularVelocity);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotate(-angularVelocity);
        }

        //do pewpew
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            try {
                pewpew();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        /*
            TOUCHSCREEN
         */

        if( Gdx.input.isTouched() ) {
            boolean left = false, right = false;

            //gather areas being touched
            if ((Gdx.input.isTouched(0) && Gdx.input.getX(0) < Gdx.graphics.getWidth() / 2) ||
                    (Gdx.input.isTouched(1) && Gdx.input.getX(1) < Gdx.graphics.getWidth() / 2)) {
                left = true;
            } if ((Gdx.input.isTouched(0) && Gdx.input.getX(0) > Gdx.graphics.getWidth() / 2) ||
                    (Gdx.input.isTouched(1) && Gdx.input.getX(1) > Gdx.graphics.getWidth() / 2)) {
                right = true;
            }

            //always shoot (temporary, don't know best way to do it yet (divide screen in three?))
            try {
                pewpew();
            } catch (Exception e) {
                System.out.println(e);
            }

            //choose action
            if (left && right) {
                moveForward();
            } else if (left) {
                rotate(angularVelocity);
            } else if (right) {
                rotate(-angularVelocity);
            }
        }
    }

    private void borderTeletransportation(Camera cam) {

        //if spaceship goes past the right border
        if (body.getWorldCenter().x > cam.viewportWidth)
            body.setTransform(1, body.getPosition().y, body.getAngle());

        //if spaceship goes past the left border
        if (body.getWorldCenter().x < 0)
            body.setTransform(cam.viewportWidth-1, body.getPosition().y, body.getAngle());

        //if spaceship goes past the top border
        if (body.getWorldCenter().y > cam.viewportHeight)
            body.setTransform(body.getPosition().x, 1, body.getAngle());

        //if spaceship goes past the bottom border
        if (body.getWorldCenter().y < 0)
            body.setTransform(body.getPosition().x, cam.viewportHeight-1, body.getAngle());
    }

    // Does pewpew to shoot sum of dat assteroidzzzzz
    private void pewpew() throws  Exception{
        if (!(this.bullets.size() < this.maxBullets && System.currentTimeMillis() - this.bulletTime >= this.bulletInterval)) {
            throw new Exception("Bullet Timer Error");
        }
        float angle = this.body.getAngle();
        float x = this.body.getPosition().x + 3* (float) -Math.sin(angle);
        float y = this.body.getPosition().y + 3* (float) Math.cos(angle);
        com.mygdx.javainvaders.Bullet b = new com.mygdx.javainvaders.Bullet(this.world, x, y);
        b.body.setTransform(x, y, angle);
        b.body.setLinearVelocity(20* (float) -Math.sin(angle), 20 * (float) Math.cos(angle));
        this.bullets.add(b);
        this.bulletTime = System.currentTimeMillis();

    }

    GameState update(ShapeRenderer shapeRenderer, Camera cam) {
//        if( health < 0 ) System.out.println("you are dead");

        if( state == GameState.animating ){
            create();
            state = GameState.playing;
            return GameState.playing;
        }

        inputHandling(cam);
        borderTeletransportation(cam);
        draw(shapeRenderer);

//        List<com.mygdx.javainvaders.Bullet> deadBullets = new ArrayList<com.mygdx.javainvaders.Bullet>();


        if (this.THREAD_FLAG) {
            // Check which bullets are dead with MULTI THREADING WOW
            List<Bullet> b1 = bullets.subList(0, bullets.size()/2);
            List<Bullet> b2 = bullets.subList(bullets.size()/2, bullets.size());
            BulletRunnable br1 = new BulletRunnable(b1, cam);
            BulletRunnable br2 = new BulletRunnable(b2, cam);
            br1.start();
            br2.start();
            try {
                br1.t.join();
                br2.t.join();
            } catch ( InterruptedException e ) {
                System.out.println(e);
            }

            List<Bullet> deadBullets = br1.deadBullets;
            deadBullets.addAll(br2.deadBullets);
            for (Bullet b : deadBullets) {
                this.bullets.remove(b);
                world.destroyBody(b.body);
            }
            for (Bullet b : this.bullets) {
                b.draw(shapeRenderer);
                float angle = b.body.getAngle();
                float x = Bullet.throttle * (float) -Math.sin(angle);
                float y = Bullet.throttle * (float) Math.cos(angle);
                b.body.applyForceToCenter(x, y, true);

            }
        } else {
            List<com.mygdx.javainvaders.Bullet> deadBullets = new ArrayList<com.mygdx.javainvaders.Bullet>();
            for (Bullet b : this.bullets) {
                if (b.pewpewdeath(cam)){
                    deadBullets.add(b); // Kill bullets
                    continue;
                }
                b.draw(shapeRenderer);
                float angle = b.body.getAngle();
                float x = Bullet.throttle * (float) -Math.sin(angle);
                float y = Bullet.throttle * (float) Math.cos(angle);
                b.body.applyForceToCenter(x, y, true);

            }

            for (Bullet b : deadBullets) {
                this.bullets.remove(b);
                world.destroyBody(b.body);
            }
        }


        //update game state
        if( health < 0 ){

            return GameState.pre_death_end;
        }
        return GameState.playing;
    }
}
