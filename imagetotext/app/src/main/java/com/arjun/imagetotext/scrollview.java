package com.arjun.imagetotext;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Animatable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.ms.square.android.expandabletextview.ExpandableTextView;


import java.text.SimpleDateFormat;

public class scrollview extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        TextView textab=(TextView)findViewById(R.id.textab);
        long date=System.currentTimeMillis();
        SimpleDateFormat aa=new SimpleDateFormat("dd MMM yyyy, h:mm a");
        String datestring=aa.format(date);
        textab.setText(""+datestring);
        ExpandableTextView expTv1 = (ExpandableTextView)findViewById(R.id.expand_text_view);
        expTv1.setText(getString(R.string.in_news));

//        MagicButton btnYoutube=(MagicButton) findViewById(R.id.magic_button_youtube);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout cool=(CollapsingToolbarLayout)findViewById(R.id.ab12);
        setSupportActionBar(toolbar);
        cool.setTitle("Image To Text");
//        btnYoutube.setMagicButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ii=new Intent(scrollview.this,value.class);
//                startActivity(ii);
//            }
//        }

//        bu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ii=new Intent(scrollview.this,value.class);
//                startActivity(ii);
//            }
//        });
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cool.setCollapsedTitleTextAppearance(R.style.Text123);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(scrollview.this,selection.class);
                startActivity(in);

            }
        });


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            }

        }




}
