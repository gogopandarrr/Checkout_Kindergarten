package com.onethefull.attendmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    ImageView iv_logo;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        iv_logo = findViewById(R.id.logo);

        Animation ani = AnimationUtils.loadAnimation(this, R.anim.appear_logo);
        ani.setDuration(1500);

        iv_logo.startAnimation(ani);

        timer.schedule(task, 1800);


    }//oc


    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);

            finish();
        }
    };
}
