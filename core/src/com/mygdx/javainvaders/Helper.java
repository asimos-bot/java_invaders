package com.mygdx.javainvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class Helper {

    //if casted to int -> include min, do not include max
    static float randFloatRange(float min, float max){
        return min + (float) Math.random() * (max - min);
    }

    static boolean isOffScreen(Vector2 point, float offset, Camera cam){

        return point.x > cam.viewportWidth + offset ||
               point.y > cam.viewportHeight + offset ||
               point.x + offset < 0 ||
               point.y + offset < 0;
    }
}
