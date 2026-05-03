package com.example.stayalive.gameobject;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.stayalive.GameLoop;
import com.example.stayalive.R;

public class Gun extends Circle {
    public static double SPEED_PIXELS_PER_SECOND = 1000.0;  // Speed of the spell
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    private Enemy targetEnemy;

    // Constructor now requires both Player and Enemy objects
    public Gun(Context context, Player player, Enemy targetEnemy) {
        super(
                context,
                ContextCompat.getColor(context, R.color.spell),  // Spell color
                player.getPositionX(),
                player.getPositionY(),
                15  // Spell radius
        );
        this.targetEnemy = targetEnemy;

        // Calculate direction vector towards the target enemy
        double deltaX = targetEnemy.getPositionX() - player.getPositionX();
        double deltaY = targetEnemy.getPositionY() - player.getPositionY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Normalize the direction vector
        if (distance > 0) {
            velocityX = (deltaX / distance) * MAX_SPEED;  // Normalize and scale by speed
            velocityY = (deltaY / distance) * MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }
    }

    @Override
    public void update() {
        // Move spell in the direction of the target enemy
        positionX += velocityX;
        positionY += velocityY;
    }
}