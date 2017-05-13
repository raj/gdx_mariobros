package com.rajdeenoo.mariobros.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by raj on 13/05/2017.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
