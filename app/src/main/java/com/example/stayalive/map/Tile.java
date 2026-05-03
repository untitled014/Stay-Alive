package com.example.stayalive.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.stayalive.graphics.SpriteSheet;

abstract class Tile {

    protected final Rect mapLocationRect;

    public Tile(Rect mapLocationRect) {
        this.mapLocationRect = mapLocationRect;
    }

    public enum TileType {
        WATER_TILE,
        LAVA_TILE,
        GRASS_TILE
    }

    public static Tile getTile(int idxTileType, SpriteSheet spriteSheet, Rect mapLocationRect) {

        switch(TileType.values()[idxTileType]) {

            case GRASS_TILE:
                return new GrassTile(spriteSheet, mapLocationRect);
            default:
                return null;
        }

    }

    public abstract void draw(Canvas canvas);
}
