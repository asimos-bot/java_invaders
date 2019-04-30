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

    private Body body;
    private float[] shape;

    SpaceEntity(World world, float initialX, float initialY){

        //create body definition
        BodyDef bodyDef = new BodyDef(); //create
        bodyDef.type = BodyDef.BodyType.DynamicBody; //set type of body
        bodyDef.position.set(initialX, initialY);

        //create our body definition in the world
        body = world.createBody(bodyDef);
    }

    void setFixtures(float[] vertices, float density, float friction, float restitution){

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
    void draw(){

        //get a ShapeRenderer
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        //draw only countor lines
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //white lines
        shapeRenderer.setColor(1,1,1,1);
        //draw here
        shapeRenderer.translate(body.getPosition().x, body.getPosition().y, 0);
        //with this shape
        shapeRenderer.polygon(shape);
        //that's it
        shapeRenderer.end();
    }
}
