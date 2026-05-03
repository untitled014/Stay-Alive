package com.example.stayalive.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ExperienceBar {

    private int x, y, width, height;
    private float maxExperience;
    private float currentExperience;
    private Paint paintBackground;
    private Paint paintForeground;

    public ExperienceBar(int x, int y, int width, int height, float maxExperience) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxExperience = maxExperience;

        // Initialize the paint objects
        paintBackground = new Paint();
        paintBackground.setColor(Color.GRAY);  // Color for the empty experience bar

        paintForeground = new Paint();
        paintForeground.setColor(Color.GREEN);  // Color for the filled experience bar
    }

    public void setCurrentExperience(float experience) {
        this.currentExperience = experience;
    }

    public void draw(Canvas canvas) {
        // Draw background bar
        canvas.drawRect(x-150, y, x + width, y + height, paintBackground);

        // Draw foreground bar (filled portion)
        float progress = currentExperience / maxExperience;
        canvas.drawRect(x-150, y, x-150 + (width * progress), y + height, paintForeground);
    }
}
