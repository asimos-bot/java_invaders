package com.mygdx.javainvaders;

public class Helper {

    //if casted to int -> include min, do not include max
    static float randFloatRange(float min, float max){
        return min + (float) Math.random() * (max - min);
    }
}
