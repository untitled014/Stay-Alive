package com.example.stayalive.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.stayalive.R;

public class SpriteSheet {
    private static final int SPRITE_WIDTH_PIXELS = 128;
    private static final int SPRITE_HEIGHT_PIXELS = 128;
    private Bitmap bitmap;

    public SpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_sheet_transparent, bitmapOptions);
    }

    public Sprite[] getPlayerSpriteArray() {
        Sprite[] spriteArray = new Sprite[3];
        spriteArray[0] = new Sprite(this, new Rect(0*128, 0, 1*128, 128));
        spriteArray[1] = new Sprite(this, new Rect(1*128, 0, 2*128, 128));
        spriteArray[2] = new Sprite(this, new Rect(2*128, 0, 3*128, 128));
        return spriteArray;
    }
    public Sprite[] getEnemySpriteArray() {
        Sprite[] spriteArray = new Sprite[3];
        spriteArray[0] = new Sprite(this, new Rect(3*128, 0, 4*128, 128));
        spriteArray[1] = new Sprite(this, new Rect(4*128, 0, 5*128, 128));
        spriteArray[2] = new Sprite(this, new Rect(5*128, 0, 6*128, 128));
        return spriteArray;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Sprite getGrassSprite() {
        return getSpriteByIndex(2, 0);
    }

    private Sprite getSpriteByIndex(int idxRow, int idxCol) {
        return new Sprite(this, new Rect(
                idxCol*SPRITE_WIDTH_PIXELS,
                idxRow*SPRITE_HEIGHT_PIXELS,
                (idxCol + 1)*SPRITE_WIDTH_PIXELS,
                (idxRow + 1)*SPRITE_HEIGHT_PIXELS
        ));
    }
}
