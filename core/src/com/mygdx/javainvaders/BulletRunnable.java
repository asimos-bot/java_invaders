package com.mygdx.javainvaders;

import com.badlogic.gdx.graphics.Camera;

import java.util.ArrayList;
import java.util.List;

public class BulletRunnable implements Runnable{
    protected Thread t;
    protected List<Bullet> bullets;
    protected List<Bullet> deadBullets;
    protected Camera cam;

    BulletRunnable(List<Bullet> b, Camera c){
        bullets = b;
        cam = c;
        deadBullets =  new ArrayList<Bullet>();
    }


    public void run(){
        try {
            for(Bullet b: bullets){
                if (b.pewpewdeath(cam)){
                    deadBullets.add(b);
                }
            }
        } catch (Exception e) {
            System.out.println("Thread interrupted.");
        }
//        System.out.println("Thread exiting.");
    }

    public void start () {
//        System.out.println("Starting Thread");
        if (t == null) {
            t = new Thread (this);
            t.start ();
        }
    }
}


