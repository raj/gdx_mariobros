package com.rajdeenoo.mariobros.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.rajdeenoo.mariobros.MarioBros;
import com.rajdeenoo.mariobros.Screens.PlayScreen;

/**
 * Created by raj on 13/05/2017.
 */

public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroyed;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        setBounds(getX(),getY(),16/ MarioBros.PPM, 16 / MarioBros.PPM);
        defineItem();
        toDestroyed = false;
        destroyed = false;
    }


    public abstract void defineItem();
    public abstract void use();

    public void update(float dt) {
        if(toDestroyed && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch) {
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy() {
        toDestroyed = true;

    }

}
