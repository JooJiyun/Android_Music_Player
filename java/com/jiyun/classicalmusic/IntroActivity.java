package com.jiyun.classicalmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    private boolean flg = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        Animation anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(2000);
        anim.setStartOffset(300);
        anim.setRepeatMode(Animation.REVERSE);

        TextView text_intro = findViewById(R.id.text_intro);
        text_intro.startAnimation(anim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flg = false;
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(flg){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}