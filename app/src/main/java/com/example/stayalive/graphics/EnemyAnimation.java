package com.example.stayalive.graphics;

public class EnemyAnimation {

    private final Sprite[] frames;  // Array to hold enemy sprite frames
    private int currentFrameIndex = 0;
    private final int totalFrames;
    private final int frameSpeed;  // Number of game updates per frame
    private int frameCounter = 0;

    public EnemyAnimation(SpriteSheet spriteSheet) {
        // Assuming you have a sheet with multiple frames for the enemy
        frames = spriteSheet.getEnemySpriteArray();  // You need to have a method to fetch enemy frames
        totalFrames = frames.length;
        frameSpeed = 10;  // How many frames per second, adjust for smoother animation
    }

    // Update the current sprite based on frame speed
    public void update() {
        // Update the current animation frame every few frames (for smooth animation)
        frameCounter++;
        if (frameCounter >= frameSpeed) {
            frameCounter = 0;
            currentFrameIndex = (currentFrameIndex + 1) % totalFrames;
        }
    }


    // Get the current sprite frame
    public Sprite getCurrentSprite() {
        return frames[currentFrameIndex];
    }
}
