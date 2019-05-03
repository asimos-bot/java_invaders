package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Spaceship extends SpaceEntity {

    private float throttle = 10e4f;
    private float angularVelocity = 3.0f;

    private List<com.mygdx.javainvaders.Bullet> bullets = new ArrayList<com.mygdx.javainvaders.Bullet>();
    private int maxBullets = 50;
    private float bulletInterval = 0.25f * 1000;
    private long bulletTime = System.currentTimeMillis();

    private World world;


    //just call SpaceEntity class constructor
    Spaceship(World world, float initialX, float initialY) {
        super(world, initialX, initialY);
        this.world = world;
        body.setAngularDamping(5); //how long it takes to stop the rotation basically
        body.setLinearDamping(0.5f);
    }

    //handles input to spaceship control and draws it
    private void inputHandling() {
        //goes forward
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            body.applyForceToCenter(
                    (float) Math.cos(body.getAngle()) * throttle,
                    (float) Math.sin(body.getAngle()) * throttle,
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

    private void borderTeletransportation() {

        //if spaceship goes past the right border
        if (body.getWorldCenter().x > Gdx.graphics.getWidth())
            body.setTransform(0, body.getPosition().y, body.getAngle());

        //if spaceship goes past the left border
        if (body.getWorldCenter().x < 0)
            body.setTransform(Gdx.graphics.getWidth(), body.getPosition().y, body.getAngle());

        //if spaceship goes past the top border
        if (body.getWorldCenter().y > Gdx.graphics.getHeight())
            body.setTransform(body.getPosition().x, 0, body.getAngle());

        //if spaceship goes past the bottom border
        if (body.getWorldCenter().y < 0)
            body.setTransform(body.getPosition().x, Gdx.graphics.getHeight(), body.getAngle());
    }

    // Does pewpew to shoot sum of dat assteroidzzzzz
    private void pewpew() {
        if (this.bullets.size() < this.maxBullets && System.currentTimeMillis() - this.bulletTime >= this.bulletInterval) {
            float angle = this.body.getAngle();
            float x = this.body.getPosition().x + 30 * (float) Math.cos(angle);
            float y = this.body.getPosition().y + 30 * (float) Math.sin(angle);
            com.mygdx.javainvaders.Bullet b = new com.mygdx.javainvaders.Bullet(this.world, x, y);
            b.body.setTransform(x, y, angle);
            this.bullets.add(b);
            this.bulletTime = System.currentTimeMillis();
        }
    }

    void update() {
        inputHandling();
        borderTeletransportation();
        draw();

        List<com.mygdx.javainvaders.Bullet> deadBullets = new ArrayList<com.mygdx.javainvaders.Bullet>();
        for (Bullet b : this.bullets) {
            if (b.pewpewdeath()){
                deadBullets.add(b); // Kill bullets
                continue;
            }
            b.draw();
            float angle = b.body.getAngle();
            float x = Bullet.throttle * (float) Math.cos(angle);
            float y = Bullet.throttle * (float) Math.sin(angle);
            b.accelCount++;
            b.body.setLinearVelocity(b.accelCount * x, b.accelCount * y);
        }

        for (Bullet b : deadBullets) {
            this.bullets.remove(b);
        }

    }
}
