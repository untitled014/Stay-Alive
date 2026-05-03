package com.example.stayalive.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;

import com.example.stayalive.Game;
import com.example.stayalive.GameDisplay;
import com.example.stayalive.GameLoop;
import com.example.stayalive.gamepanel.HealthBar;
import com.example.stayalive.gamepanel.ExperienceBar;
import com.example.stayalive.gamepanel.Joystick;
import com.example.stayalive.R;
import com.example.stayalive.Utils;
import com.example.stayalive.graphics.Animator;
import com.example.stayalive.map.MapLayout;

public class Player extends Circle {
    // Boundaries based on the map layout
    private double leftBoundary = 64;
    private double rightBoundary = MapLayout.NUMBER_OF_COLUMN_TILES * MapLayout.TILE_WIDTH_PIXELS;
    private double topBoundary = 0;
    private double bottomBoundary = (MapLayout.NUMBER_OF_ROW_TILES * MapLayout.TILE_HEIGHT_PIXELS)-64;

    // Padding from the boundaries (if needed)
    private int padding = 0; // Set this to control the distance from edges
    public static double SPEED_PIXELS_PER_SECOND = 400.0;
    public static double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static int MAX_HEALTH_POINTS = 5;
    private Joystick joystick;
    private HealthBar healthBar;
    private ExperienceBar experienceBar;
    public int healthPoints = MAX_HEALTH_POINTS;
    private Animator animator;
    private PlayerState playerState;
    private float currentExperience;
    private float maxExperience;
    public int level=1;


    // This will identify if the player is upgrading or not
    private boolean isUpgrading = false;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius, Animator animator) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        this.animator = animator;
        this.playerState = new PlayerState(this);

        // Initialize experience
        this.currentExperience = 0;
        this.maxExperience = 100;  // Example max experience (you can adjust this value)

        // Initialize the ExperienceBar
        this.experienceBar = new ExperienceBar(220, 2200, 800, 40, this.maxExperience);  // Example fixed position at the bottom
    }
    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void update() {
        // Update velocity based on joystick input
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;

        // Update position based on velocity
        positionX += velocityX;
        positionY += velocityY;

        // Ensure player stays within screen map boundaries (updated to fit the tile grid)
        positionX = Math.max(leftBoundary + padding, Math.min(positionX, rightBoundary - padding));
        positionY = Math.max(topBoundary + padding, Math.min(positionY, bottomBoundary - padding));

        // Update direction
        if (velocityX != 0 || velocityY != 0) {
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX / distance;
            directionY = velocityY / distance;
        }

        playerState.update();
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        animator.draw(canvas, gameDisplay, this);
        healthBar.draw(canvas, gameDisplay);

        // Draw ExperienceBar at the bottom of the screen (fixed position)
        experienceBar.setCurrentExperience(currentExperience);  // Update experience bar progress
        experienceBar.draw(canvas);
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoints) {
        if (healthPoints >= 0) {
            this.healthPoints = healthPoints;
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }


    public void addExperience(float amount) {
        currentExperience += amount;
        if (currentExperience >= maxExperience) {
            currentExperience = maxExperience;
            levelUp();  // Level up the player when max experience is reached
        }
    }

    public void levelUp() {
        currentExperience = 0;  // Reset current experience
        isUpgrading = true; //
        level++;
        Enemy.upgrade();
    }

    // Method for applying upgrades
    public void applyUpgrade(int upgradeType) {
        switch (upgradeType) {
            case 0: // Damage Increase or Bullet Speed
                Game.gundamage += 1; // Adjust to increase bullet speed/damage.
                break;
            case 1: // Rate of Fire Increase
                Game.BULLET_CAST_COOLDOWN -= 0.3;
                Gun.SPEED_PIXELS_PER_SECOND += 100.0;
                break;
            case 2: // Movement Speed Increase
                Player.MAX_SPEED += 10.0;
                break;
            case 3: // HP Increase
                healthPoints++; // Increase HP by 1
                break;
        }
    }

    public boolean isUpgrading() {
        return isUpgrading;
    }

    public void setUpgrading(boolean isUpgrading) {
        this.isUpgrading = isUpgrading;
    }

    public int getLevel() {
        return level;
    }
}
