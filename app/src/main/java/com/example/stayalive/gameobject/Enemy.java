package com.example.stayalive.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;

import com.example.stayalive.GameDisplay;
import com.example.stayalive.GameLoop;
import com.example.stayalive.R;
import com.example.stayalive.graphics.EnemyAnimation;
import com.example.stayalive.graphics.Sprite;
import com.example.stayalive.graphics.SpriteSheet;

public class Enemy extends Circle {

    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.6;
    public static double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    public static double BASE_HEALTH = 3.0;
    private double currentHealth = BASE_HEALTH;  // Current health of the enemy

    private Player player;

    // Enemy animation object
    private EnemyAnimation enemyAnimation;

    // Constructor
    public Enemy(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;

        // Initialize the animation for this enemy
        SpriteSheet spriteSheet = new SpriteSheet(context);
        this.enemyAnimation = new EnemyAnimation(spriteSheet);
    }

    // Constructor for spawning in random locations
    public Enemy(Context context, Player player) {
        super(
                context,
                ContextCompat.getColor(context, R.color.enemy),
                Math.random() * 1000,  // Random spawn X position
                Math.random() * 1000,  // Random spawn Y position
                30  // Enemy radius
        );
        this.player = player;

        // Initialize the animation for this enemy
        SpriteSheet spriteSheet = new SpriteSheet(context);
        this.enemyAnimation = new EnemyAnimation(spriteSheet);
    }

    // Method to check if the enemy is ready to spawn
    public static boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn--;
            return false;
        }
    }

    // Update method: moves the enemy towards the player and updates the animation
    public void update() {
        // Calculate direction vector from enemy to player
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        // Calculate the distance to the player
        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

        // Normalize direction (to get a unit vector)
        double directionX = distanceToPlayerX / distanceToPlayer;
        double directionY = distanceToPlayerY / distanceToPlayer;

        // Set velocity based on direction
        if (distanceToPlayer > 0) {
            velocityX = directionX * MAX_SPEED;
            velocityY = directionY * MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }

        // Update position based on velocity
        positionX += velocityX;
        positionY += velocityY;

        // Update the enemy's animation frame
        enemyAnimation.update();
    }
    public static void upgrade(){
        BASE_HEALTH += 0.5;
        MAX_SPEED += 2.0;
        SPAWNS_PER_MINUTE += 0.5;
    }


    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    // Draw the enemy using the current animation and apply display coordinates
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        Sprite currentSprite = enemyAnimation.getCurrentSprite();
        int spriteWidth = currentSprite.getWidth();
        int spriteHeight = currentSprite.getHeight();
        int drawX = (int) gameDisplay.gameToDisplayCoordinatesX(positionX);
        int drawY = (int) gameDisplay.gameToDisplayCoordinatesY(positionY);
        currentSprite.draw(canvas, drawX - spriteWidth / 2, drawY - spriteHeight / 2);
    }
}
