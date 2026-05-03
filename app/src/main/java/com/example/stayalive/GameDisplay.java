package com.example.stayalive;

import android.graphics.Rect;

import com.example.stayalive.gameobject.GameObject;

public class GameDisplay {
    public final Rect DISPLAY_RECT;
    public static int widthPixels;
    public static int heightPixels;
    private final GameObject centerObject;
    private final double displayCenterX;
    private final double displayCenterY;
    private double gameToDisplayCoordinatesOffsetX;
    private double gameToDisplayCoordinatesOffsetY;
    private double gameCenterX;
    private double gameCenterY;

    public GameDisplay(int widthPixels, int heightPixels, GameObject centerObject) {
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        DISPLAY_RECT = new Rect(0, 0, widthPixels, heightPixels);

        this.centerObject = centerObject;

        displayCenterX = widthPixels/2.0;
        displayCenterY = heightPixels/2.0;

        update();
    }

    public void update() {
        // Update the game world coordinates of the center object (e.g., Player's position)
        gameCenterX = centerObject.getPositionX();
        gameCenterY = centerObject.getPositionY();

        // Calculate the offset required to center the screen around the game object
        gameToDisplayCoordinatesOffsetX = displayCenterX - gameCenterX;
        gameToDisplayCoordinatesOffsetY = displayCenterY - gameCenterY;
    }

    public double gameToDisplayCoordinatesX(double x) {
        return x + gameToDisplayCoordinatesOffsetX;
    }

    public double gameToDisplayCoordinatesY(double y) {
        return y + gameToDisplayCoordinatesOffsetY;
    }

    public Rect getGameRect() {
        return new Rect(
                (int) (gameCenterX - widthPixels/2),
                (int) (gameCenterY - heightPixels/2),
                (int) (gameCenterX + widthPixels/2),
                (int) (gameCenterY + heightPixels/2)
        );
    }
}
