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

public class HowToPlay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        ImageView imageView = findViewById(R.id.how_to_play_image);
        Bitmap howToPlayImage = BitmapFactory.decodeResource(getResources(), R.drawable.how_to_play);
        imageView.setImageBitmap(howToPlayImage);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HowToPlay.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
