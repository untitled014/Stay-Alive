package com.example.stayalive.gamepanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.graphics.Typeface;

import com.example.stayalive.Game;
import com.example.stayalive.R;

public class MainMenu {
    private final Game game;
    private final Paint paint;
    private final Bitmap backgroundImage; // Background image
    private Bitmap scaledBackgroundImage; // Scaled background image

    private final String[] menuItems = {"Start Game", "How to Play", "About Us",     "Exit"};
    private final int buttonHeight = 150; // Height of each button
    private final int buttonSpacing = 50; // Spacing between buttons
    private int selectedItem = 0;

    // Store button Y position and height for each menu item
    private final int[] buttonTopY = new int[menuItems.length];
    private final int[] buttonBottomY = new int[menuItems.length];

    public MainMenu(Game game, Context context) {
        this.game = game; // Initialize the Game object reference
        this.paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        // Set custom font from assets
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/slkscreb.ttf");
        paint.setTypeface(typeface); // Apply custom font

        // Load background image
        backgroundImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        // Calculate the Y positions for each menu item
        int yOffset = 1200;  // Starting Y position for the first item
        for (int i = 0; i < menuItems.length; i++) {
            buttonTopY[i] = yOffset;
            buttonBottomY[i] = yOffset + buttonHeight;
            yOffset += buttonHeight + buttonSpacing;  // Move to the next button position
        }
    }

    public void draw(Canvas canvas) {
        // Get the width and height of the canvas (screen)
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        // Scale the background image to fit the screen
        scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage, canvasWidth, canvasHeight, false);

        // Draw the scaled background image
        canvas.drawBitmap(scaledBackgroundImage, 0, 0, null);

        // Draw each menu item with highlighting for the selected item
        for (int i = 0; i < menuItems.length; i++) {
            if (i == selectedItem) {
                paint.setColor(Color.YELLOW);  // Highlight selected item
            } else {
                paint.setColor(Color.WHITE);
            }
            canvas.drawText(menuItems[i], canvasWidth / 2, buttonTopY[i] + buttonHeight / 2, paint);
        }
    }

    public void update(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // Check if the user clicked on any menu option
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Loop through buttons and check if the touch Y position falls within any button's vertical range
            for (int i = 0; i < menuItems.length; i++) {
                if (y >= buttonTopY[i] && y <= buttonBottomY[i]) {
                    selectedItem = i; // Set the selected item based on the Y position
                    handleMenuAction(); // Handle action for the selected item
                    break;  // Only handle one menu item per touch
                }
            }
        }
    }

    private void handleMenuAction() {
        switch (selectedItem) {
            case 0:  // "Start Game"
                game.startGame(); // Call the method in Game class to start the game
                break;
            case 1:  // "How to Play"
                Intent howToPlayIntent = new Intent(game.getContext(), HowToPlay.class);
                game.getContext().startActivity(howToPlayIntent);
                // Code to show settings (you can implement later)
                break;
            case 2: // "About Us"
                Intent aboutUsIntent = new Intent(game.getContext(), AboutUs.class);
                game.getContext().startActivity(aboutUsIntent);
                break;
            case 3:  // "Exit Confirmation"
                Intent exitConfirmationIntent = new Intent(game.getContext(), ExitConfirmation.class);
                game.getContext().startActivity(exitConfirmationIntent);
                break;
        }
    }
}
