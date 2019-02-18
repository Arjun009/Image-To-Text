package com.arjun.imagetotext;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity1 extends AppCompatActivity {

    Button abc;
    LinearLayout l1,l2;
    static MediaPlayer as;
    Animation uptodown,downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        abc=(Button)findViewById(R.id.buttonsub);
        l1 = (LinearLayout) findViewById(R.id.l1);
//        l2 = (LinearLayout) findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
//        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
//        l2.setAnimation(downtoup);

    }
    @Override
    protected void onResume(){
        super.onResume();
        final MediaPlayer as=MediaPlayer.create(this,R.raw.reverse);
        abc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                as.start();
                Intent ii =new Intent(MainActivity1.this,selection.class);
                startActivity(ii);

            }
        });

    }

}
