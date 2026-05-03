package com.example.stayalive.gamepanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.stayalive.Game;
import com.example.stayalive.R;
import com.example.stayalive.gameobject.Player;

public class UpgradeMenu {
    private Game game;
    private Canvas canvas;
    private Player player;
    private Context context;
    private Rect buttonBounds[];
    private String[] upgradeOptions;
    private int screenWidth, screenHeight;
    private int buttonWidth = 700;  // Fixed button width
    private int buttonHeight = 100; // Fixed button height
    private int verticalSpacing = 150; // Vertical spacing between buttons
    private Bitmap levelupSprite;

    public UpgradeMenu(Context context, Player player) {
        this.game = game;
        this.context = context;
        this.player = player;
        this.levelupSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.levelup_sprite);

        // Initialize screen width and height
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        // Upgrade Options
        upgradeOptions = new String[]{
                "Increase Damage",
                "Increase Fire Rate",
                "Increase Movement Speed",
                "Increase Health"
        };

        // Create an array of Rect objects for buttons
        buttonBounds = new Rect[upgradeOptions.length];

        // Calculate positions dynamically based on the screen size
        int totalMenuHeight = (upgradeOptions.length * buttonHeight) + ((upgradeOptions.length - 1) * verticalSpacing);
        int startY = (screenHeight - totalMenuHeight) / 2;  // Center vertically

        for (int i = 0; i < upgradeOptions.length; i++) {
            // Center buttons horizontally, and position vertically based on index
            int startX = (screenWidth - buttonWidth) / 2;  // Center horizontally
            buttonBounds[i] = new Rect(startX, startY + (i * (buttonHeight + verticalSpacing)),
                    startX + buttonWidth, startY + (i * (buttonHeight + verticalSpacing)) + buttonHeight);
        }
    }

    public void update(float touchX, float touchY) {
        // Handles Input for Upgrade Selection
        for (int i = 0; i < upgradeOptions.length; i++) {
            if (buttonBounds[i].contains((int) touchX, (int) touchY)) {
                player.applyUpgrade(i);  // Apply the chosen upgrade
                player.setUpgrading(false);// Close the Upgrade Menu after selection
                break;
            }
        }
    }

    public void draw(Canvas canvas) {
        // Desired width and height for scaling
        int newWidth = 900; // New width in pixels
        int newHeight = 800; // New height in pixels

        // Scale the bitmap to the new dimensions
        Bitmap scaledSprite = Bitmap.createScaledBitmap(levelupSprite, newWidth, newHeight, true);

        // Define where the scaled sprite will be drawn on the canvas
        float x = 90; // Adjust x position (horizontal placement)
        float y = 100; // Adjust y position (vertical placement)

        // Draw the scaled game over sprite onto the canvas
        canvas.drawBitmap(scaledSprite, x, y, null);
        this.canvas = canvas;
        Paint paint = new Paint();
        Paint textPaint = new Paint();
        Paint buttonColor = new Paint();

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        buttonColor.setColor(Color.GRAY);

        // Draws the Upgrade UI
        for (int i = 0; i < upgradeOptions.length; i++) {
            // Draws the Buttons
            canvas.drawRect(buttonBounds[i], buttonColor);

            // Draws the Text for each Selection (Center text horizontally within button)
            float textWidth = textPaint.measureText(upgradeOptions[i]);
            float x1 = buttonBounds[i].left + (buttonWidth - textWidth) / 2;
            float y1 = buttonBounds[i].top + (buttonHeight / 2) + (textPaint.getTextSize() / 2) - 10;  // Center text vertically
            canvas.drawText(upgradeOptions[i], x1, y1, textPaint);
        }
    }
}
