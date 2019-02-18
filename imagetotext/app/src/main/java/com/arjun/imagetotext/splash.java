package com.arjun.imagetotext;



import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class splash extends AppCompatActivity {
    private LinearLayout iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv=findViewById(R.id.iv);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        iv.startAnimation(myanim);
        final Intent i =new Intent(this,MainActivity1.class);
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}
