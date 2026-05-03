package com.example.stayalive;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.stayalive.gameobject.Circle;
import com.example.stayalive.gameobject.Enemy;
import com.example.stayalive.gameobject.Gun;
import com.example.stayalive.gameobject.Player;
import com.example.stayalive.gamepanel.ExperienceBar;
import com.example.stayalive.gamepanel.GameOver;
import com.example.stayalive.gamepanel.Joystick;
import com.example.stayalive.gamepanel.MainMenu;
import com.example.stayalive.gamepanel.UpgradeMenu;
import com.example.stayalive.graphics.Animator;
import com.example.stayalive.graphics.SpriteSheet;
import com.example.stayalive.map.Tilemap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game manages all objects in the game and is responsible for updating all states and render all
 * objects to the screen
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Tilemap tilemap;
    private int joystickPointerId = 0;
    private final Joystick joystick;
    private static Player player;
    private float initialPlayerX;
    private float initialPlayerY;
    private GameLoop gameLoop;
    private static List<Enemy> enemyList = new ArrayList<>();
    private List<Gun> bulletList = new ArrayList<>();
    public static int gundamage = 1;
    private int numberOfbullets = 0;
    private GameOver gameOver;
    private GameDisplay gameDisplay;
    public static double BULLET_CAST_COOLDOWN = 1.5; // Cooldown for casting bullets
    private double CooldownTimer = 0.0;  // Timer to track spell cooldown
    private ExperienceBar experienceBar;
    private UpgradeMenu upgradeMenu; // Upgrade menu
    private MainMenu mainMenu; // Main menu screen
    public boolean isInMainMenu = true; // Flag to track if we're in the main menu
    private int score = 0;

    public Game(Context context) {
        super(context);
        gameOver = new GameOver(context);

        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        // Initialize game panels
        gameOver = new GameOver(context);
        joystick = new Joystick(550, 1800, 150, 100 );

        // Initialize game objects
        SpriteSheet spriteSheet = new SpriteSheet(context);
        Animator animator = new Animator(spriteSheet.getPlayerSpriteArray());
        player = new Player(context, joystick, 2*500, 500, 32, animator);
        initialPlayerX = 2 * 500; // The player's initial X position
        initialPlayerY = 500;     // The player's initial Y position

        // Initialize display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Initialize Tilemap
        tilemap = new Tilemap(spriteSheet);

        experienceBar = new ExperienceBar(100, displayMetrics.heightPixels, displayMetrics.widthPixels, 20, 100);

        setFocusable(true);

        upgradeMenu = new UpgradeMenu(context, player);
        mainMenu = new MainMenu(this, context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isInMainMenu) {
            mainMenu.update(event); // Handle main menu input
            return true;
        }

        if (player.getHealthPoint() <= 0) {
            gameOver.handleTouchEvent(event, this); // Handles touch events for Game Over
            return true;
        }


        // Handle user input touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (player.isUpgrading()) {
                    upgradeMenu.update(event.getX(), event.getY());
                } else {
                    if (joystick.getIsPressed()) {
                        // Joystick was pressed before this event -> cast spell
                        numberOfbullets++;
                    } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                        // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                        joystickPointerId = event.getPointerId(event.getActionIndex());
                        joystick.setIsPressed(true);
                    } else {
                        // Joystick was not previously, and is not pressed in this event -> cast spell
                        numberOfbullets++;
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    // Joystick was pressed previously and is now moved
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    // joystick pointer was let go off -> setIsPressed(false) and resetActuator()
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }

        return super.onTouchEvent(event);
    }



    private Enemy getNearestEnemy() {
        Enemy nearestEnemy = null;
        double closestDistance = Double.MAX_VALUE;

        // Loop through all enemies to find the nearest one
        for (Enemy enemy : enemyList) {
            double distance = Math.sqrt(Math.pow(player.getPositionX() - enemy.getPositionX(), 2) + Math.pow(player.getPositionY() - enemy.getPositionY(), 2));
            if (distance < closestDistance) {
                closestDistance = distance;
                nearestEnemy = enemy;
            }
        }
        return nearestEnemy;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
        ;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isInMainMenu) {
            mainMenu.draw(canvas); // Draw the main menu if we're in the main menu state
        } else {

            if (player.isUpgrading()) {
                upgradeMenu.draw(canvas); // Draws the Upgrade Menu on Upgrading
                return;
            }

            // Draw Tilemap
            tilemap.draw(canvas, gameDisplay);

            // Draw game objects
            player.draw(canvas, gameDisplay);

            // Draw enemies
            for (Enemy enemy : enemyList) {
                enemy.draw(canvas, gameDisplay);  // Draw enemy
            }

            // Draw bullets
            for (Gun bullet : bulletList) {
                bullet.draw(canvas, gameDisplay); // Draw bullet
            }

            // Draw game panels
            joystick.draw(canvas);

            // Draw Game Over screen if the player is dead
            if (player.getHealthPoint() <= 0) {
                gameOver.draw(canvas);
            }
            drawScore(canvas);
            drawLevelIndicator(canvas);
        }
    }
    private void drawScore(Canvas canvas) {
        // Set up Paint object for drawing text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);  // Set the text color (white)
        paint.setTextSize(100);  // Set text size (you can adjust this value)

        // Load custom font from assets
        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/slkscreb.ttf");
        paint.setTypeface(customFont);  // Set the custom font

        // Draw the score text at the top-left corner of the screen
        canvas.drawText("Score: " + score, 50, 200, paint);
    }

    private void drawLevelIndicator(Canvas canvas) {
        // Set up Paint object for drawing text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/slkscreb.ttf");
        paint.setTypeface(customFont);

        int playerLevel = player.getLevel();
        canvas.drawText("Level: " + playerLevel, 50, getHeight() - 100, paint);
    }
    public void update() {
        if (isInMainMenu) {
            return;
        }
        if (player.isUpgrading()) {
            return;  // Stop updating if the player is upgrading
        }
        if (player.getHealthPoint() <= 0) return;  // Stop updating if the player is dead

        joystick.update();
        player.update();
        CooldownTimer -= 1.0 / GameLoop.MAX_UPS;  // Decrease cooldown timer

        // Spawn new enemies periodically
        if (Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player));
        }

        // Update enemies
        for (Enemy enemy : enemyList) {
            enemy.update();  // Update animation for each enemy
        }

        // Cast bullets automatically to the nearest enemy if cooldown has passed
        if (CooldownTimer <= 0) {
            Enemy nearestEnemy = getNearestEnemy();
            if (nearestEnemy != null) {
                Gun bullet = new Gun(getContext(), player, nearestEnemy);
                bulletList.add(bullet);  // Add new bullet to the list
                CooldownTimer = BULLET_CAST_COOLDOWN;  // Reset the cooldown timer
            }
        }

        // Update all bullets
        for (Gun bullet : bulletList) {
            bullet.update(); // Move the bullet
        }

        // Handle collisions between enemies, bullets, and the player
        handleCollisions();

        // Update the game display to center it around the player
        gameDisplay.update();
    }

    private void handleCollisions() {
        // Iterate through enemies and check collisions with the player and bullets
        Iterator<Enemy> enemyIterator = enemyList.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();

            // Check for collision with the player
            if (Circle.isColliding(enemy, player)) {
                enemyIterator.remove(); // Remove enemy if it collides with the player
                player.setHealthPoint(player.getHealthPoint() - 1); // Decrease player's health
                continue;
            }

            // Check for collisions with bullets
            Iterator<Gun> bulletIterator = bulletList.iterator();
            while (bulletIterator.hasNext()) {
                Gun bullet = bulletIterator.next();
                if (Circle.isColliding(bullet, enemy)) {
                    enemy.takeDamage(gundamage);  // Deal damage to enemy
                    bulletIterator.remove();  // Remove bullet after collision
                    break;  // Stop checking other bullets once the enemy is hit
                }
            }

            // Remove the enemy if its health is 0 or less
            if (enemy.getCurrentHealth() <= 0) {
                enemyIterator.remove(); // Remove dead enemy from the game
                player.addExperience(10); // Reward player with experience points
                score += 10;
            }
        }
    }



    public void restartGame() {
        //Re-initialize game objects
        score = 0;
        player.setHealthPoint(Player.MAX_HEALTH_POINTS);
        enemyList.clear();
        bulletList.clear();
        player.setPosition(initialPlayerX, initialPlayerY);

        // Revert Player, Enemy, and Gun properties to normal (before upgrading)
        Player.MAX_HEALTH_POINTS=5;
        Player.MAX_SPEED = 400.0/30.0;
        Enemy.BASE_HEALTH =3.0;
        Enemy.MAX_SPEED = (400.0*.6)/30.0;
        Enemy.SPAWNS_PER_MINUTE = 20;
        gundamage=1;
        BULLET_CAST_COOLDOWN=1.5;
        CooldownTimer = BULLET_CAST_COOLDOWN;

        startGame();
    }

    public void goToMainMenu() {
        isInMainMenu = true;
    }

    public void startGame() {
        isInMainMenu = false;
    }
}
