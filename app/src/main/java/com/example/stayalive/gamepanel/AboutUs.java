package com.example.stayalive.gamepanel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import com.example.stayalive.R;

public class AboutUs extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Set up the image view and load the image
        ImageView imageView = findViewById(R.id.about_us_image);
        Bitmap aboutUsImage = BitmapFactory.decodeResource(getResources(), R.drawable.about_us);
        imageView.setImageBitmap(aboutUsImage);

        // Set up the back button to navigate to the MainMenu
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the Main Menu
                Intent intent = new Intent(AboutUs.this, MainMenu.class);
                startActivity(intent);
                finish(); // Optional: Finish this activity so it doesn't stay in the back stack
            }
        });
    }
}