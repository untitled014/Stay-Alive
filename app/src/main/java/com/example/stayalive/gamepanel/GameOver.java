package com.example.stayalive.gamepanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.stayalive.Game;
import com.example.stayalive.R;

/**
 * GameOver is a panel which draws the scaled Game Over sprite to the screen.
 */
public class GameOver {

    private Context context;
    private Bitmap gameOverSprite; // Bitmap to hold the game over sprite image
    private final Paint paint;
    private final String[] buttons = {"Play Again", "Main Menu"};
    private final Rect[] buttonRects = new Rect[buttons.length];
    private final int buttonWidth = 600;
    private final int buttonHeight = 150;
    private final int buttonGap = 50;

    public GameOver(Context context) {
        this.context = context;
        // Load the game over sprite from the drawable resource
        this.gameOverSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_over_sprite);
        this.paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/slkscreb.ttf");
        paint.setTypeface(typeface);
    }

    public void draw(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        Bitmap scaledSprite = Bitmap.createScaledBitmap(gameOverSprite, 900, 400, true);

        float x = ((canvasWidth - scaledSprite.getWidth()) / 2f)-10; // Center horizontally
        float y = 300; // Vertical position for "Game Over"

        canvas.drawBitmap(scaledSprite, x, y, null);

        int buttonStartY = canvasHeight / 2 - (buttons.length * (buttonHeight + buttonGap)) / 2;

        for (int i = 0; i < buttons.length; i++) {
            int buttonX = (canvasWidth - buttonWidth) / 2;
            int buttonY = buttonStartY + i * (buttonHeight + buttonGap);
            buttonRects[i] = new Rect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight);
            paint.setColor(Color.WHITE);
            canvas.drawRect(buttonRects[i], paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(buttons[i], buttonX + buttonWidth / 2f, buttonY + buttonHeight / 2f + 20, paint);
        }
    }

    public void handleTouchEvent(MotionEvent event, Game game) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            // Observes if Play Again Button is clicked
            if (buttonRects[0].contains((int) touchX, (int) touchY)) {
                game.restartGame();
            }
            // Observes if Main Menu button is clicked
            else if (buttonRects[1].contains((int) touchX, (int) touchY)) {
                game.goToMainMenu();
            }
        }
    }
}
