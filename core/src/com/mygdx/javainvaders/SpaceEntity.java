package com.mygdx.javainvaders;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;

public class SpaceEntity {

    protected Body body;
    protected float[] shape;

    protected SpaceEntity(World world, float initialX, float initialY){

        //create body definition
        BodyDef bodyDef = new BodyDef(); //create
        bodyDef.type = BodyDef.BodyType.DynamicBody; //set type of body
        bodyDef.position.set(initialX, initialY);

        //create our body definition in the world
        body = world.createBody(bodyDef);
    }

    protected void setFixtures(float[] vertices, float density, float friction, float restitution){

        shape = vertices;

        //set fixtures
        PolygonShape entityShape = new PolygonShape(); //set our body shape
        entityShape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = entityShape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        //create fixture and attach it to body
        body.createFixture(fixtureDef);

        entityShape.dispose();
    }

    protected void draw(){

        //get a ShapeRenderer
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        //draw only countor lines
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //white lines
        shapeRenderer.setColor(1,1,1,1);
        //draw here (get center of our polygon, where point 0,0 is)
        shapeRenderer.translate(body.getWorldCenter().x, body.getWorldCenter().y, 0);
        //draw in the right orientation
        shapeRenderer.rotate(0, 0, 1, body.getAngle());
        //with this shape
        shapeRenderer.polygon(shape);
        //that's it
        shapeRenderer.end();
    }

    protected void rotate(float angularVelocity){
        body.setAngularVelocity(angularVelocity);
    }
}
