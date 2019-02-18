package com.arjun.imagetotext;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private EditText mResultEt;
    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;
    String mstring;
    private Button button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mResultEt = (EditText) findViewById(R.id.txvResult);
        button_save=(Button)findViewById(R.id.button_save2);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mstring= mResultEt.getText().toString().trim();
                if(mstring.isEmpty()){
                    Toast.makeText(MainActivity2.this, "No text to save", Toast.LENGTH_SHORT).show();

                }
                else{

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                            String [] Permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(Permissions,WRITE_EXTERNAL_STORAGE_CODE);
                        }
                        else{
                            saveToText(mstring);
                        }
                    }
                    else{
                        saveToText(mstring);
                    }
                }


            }
        });




    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mResultEt.setText(result.get(0));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                    saveToText(mstring);
                }
                else{
                    Toast.makeText(MainActivity2.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveToText(String mstring) {
        String timestamp=new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(System.currentTimeMillis());
        try{

            File path= Environment.getExternalStorageDirectory();
            File dir=new File(path+"/Image To Text/");
            dir.mkdir();
            String filename="VoiceFile_ "+timestamp+".txt";
            File file=new File(dir,filename);
            FileWriter fw=new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf=new BufferedWriter(fw);
            bf.write(mstring);
            bf.close();
            Toast.makeText(MainActivity2.this, filename+" is saved to\n "+dir, Toast.LENGTH_LONG).show();

        }
        catch (Exception e){
            Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }
}

