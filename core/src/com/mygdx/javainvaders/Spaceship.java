package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Spaceship extends SpaceEntity {

    private float throttle = 9f;
    private float angularVelocity = 3.0f;

    private List<com.mygdx.javainvaders.Bullet> bullets = new ArrayList<com.mygdx.javainvaders.Bullet>();
    private int maxBullets = 5;
    private float bulletInterval = 0.5f * 10e2f;
    private long bulletTime = System.currentTimeMillis();

    private World world;


    //just call SpaceEntity class constructor
    Spaceship(World world, float initialX, float initialY) {
        super(world, initialX, initialY);
        this.world = world;
        body.setAngularDamping(5); //how long it takes to stop the rotation basically
        body.setLinearDamping(0.1f);
        float sf = 1;
        float[] vertices = {-1*sf,0, 0,0.5f*sf, 1*sf,0, 0,2*sf};
        this.setFixtures(vertices, 0.1f, 0.5f, 0.1f);
    }

    //handles input to spaceship control and draws it
    private void inputHandling() {
        //goes forward
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            body.applyForceToCenter(
                    (float) -Math.sin(body.getAngle()) * throttle,
                    (float) Math.cos(body.getAngle()) * throttle,
                    true
            );
        }
        //rotate
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotate(angularVelocity);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotate(-angularVelocity);
        }

        //do pewpew
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            pewpew();
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
    private void pewpew() {
        if (this.bullets.size() < this.maxBullets && System.currentTimeMillis() - this.bulletTime >= this.bulletInterval) {
            float angle = this.body.getAngle();
            float x = this.body.getPosition().x + 3* (float) -Math.sin(angle);
            float y = this.body.getPosition().y + 3* (float) Math.cos(angle);
            com.mygdx.javainvaders.Bullet b = new com.mygdx.javainvaders.Bullet(this.world, x, y);
            b.body.setTransform(x, y, angle);
            b.body.setLinearVelocity(20* (float) -Math.sin(angle), 20 * (float) Math.cos(angle));
            this.bullets.add(b);
            this.bulletTime = System.currentTimeMillis();
        }
    }

    void update(ShapeRenderer shapeRenderer, Camera cam) {
//        if( health < 0 ) System.out.println("you are dead");

        inputHandling();
        borderTeletransportation(cam);
        draw(shapeRenderer);

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
        }
    }
}
