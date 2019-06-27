package com.mygdx.javainvaders;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceEntity {

    protected Body body;
    protected float[] shape;
    protected float health;

    protected SpaceEntity(){}

    protected SpaceEntity(World world, float initialX, float initialY){

        create(world, initialX, initialY);
    }

    protected void create(World world, float initialX, float initialY){

        //create body definition
        BodyDef bodyDef = new BodyDef(); //create
        bodyDef.type = BodyDef.BodyType.DynamicBody; //set type of body
        bodyDef.position.set(initialX, initialY);

        //create our body definition in the world
        body = world.createBody(bodyDef);
        body.setUserData(this);

        health = body.getMass() * 5;
    }

    protected SpaceEntity(World world, Vector2 initialPosition){

        this( world, initialPosition.x, initialPosition.y );
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

    protected void draw(ShapeRenderer shapeRenderer){

        //draw only countor lines
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.identity();
        //white lines
        shapeRenderer.setColor(1,1,1,1);
        //draw here (get center of our polygon, where point 0,0 is)
        shapeRenderer.translate(body.getPosition().x, body.getPosition().y, 0);
//        shapeRenderer.translate(0.1f, 0.1f, 0);
        //draw in the right orientation
        shapeRenderer.rotate(0, 0, 1, (float)Math.toDegrees( (double)body.getAngle() ) );
        //with this shape
        shapeRenderer.polygon(shape);
        //that's it
        shapeRenderer.end();

    }
    float getHealth(){ return health; }

    protected void rotate(float angularVelocity){
        body.setAngularVelocity(angularVelocity);
    }
}
