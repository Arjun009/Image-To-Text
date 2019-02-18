package com.arjun.imagetotext;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.Frame.Builder;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    EditText mResultEt;
    private Button buttonclick;
    ImageView mPreviewIv;
    private TextToSpeech mTTS;
    private Button button_save;
    String mstring;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeak;


    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE=1000;
    private static final int IMAGE_PICK_CAMERA_CODE=1001;
    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;



    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultEt=(EditText)findViewById(R.id.resultEt);
        mPreviewIv=(ImageView)findViewById(R.id.imageIv);
        buttonclick=(Button)findViewById(R.id.buttonclick);
        button_save=(Button)findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mstring= mResultEt.getText().toString().trim();
                if(mstring.isEmpty()){
                    Toast.makeText(MainActivity.this, "No text to save", Toast.LENGTH_SHORT).show();

                }
                else{

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
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





        buttonclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showImageImportDialog();

            }
        });

        mButtonSpeak = findViewById(R.id.button_speak);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(new Locale("en", "IN"));

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        mButtonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        mSeekBarPitch = findViewById(R.id.seek_bar_pitch);
        mSeekBarSpeed = findViewById(R.id.seek_bar_speed);

        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });



        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




    }

    private void saveToText(String mstring) {
        String timestamp=new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(System.currentTimeMillis());
        try{

            File path= Environment.getExternalStorageDirectory();
            File dir=new File(path+"/Image To Text/");
            dir.mkdir();
            String filename="ImageFile_ "+timestamp+".txt";
            File file=new File(dir,filename);
            FileWriter fw=new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf=new BufferedWriter(fw);
            bf.write(mstring);
            bf.close();
            Toast.makeText(MainActivity.this, filename+" is saved to\n "+dir, Toast.LENGTH_LONG).show();

        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void speak() {
        String text = mResultEt.getText().toString();
        float pitch = (float) mSeekBarPitch.getProgress() / 50;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) mSeekBarSpeed.getProgress() / 50;
        if (speed < 0.1) speed = 0.1f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.addImage){
            showImageImportDialog();

        }
        if(id==R.id.settings){
            Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {

        String[] items={" Camera"," Gallery"};
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(!checkCameraPermission()){

                        requestCameraPermission();
                    }
                    else{
                        pickCamera();

                    }

                }
                if(which==1){

                    if(!checkStoragePermission()){

                        requestStoragePermission();
                    }
                    else{
                        pickGallery();

                    }





                }

            }
        });
        dialog.create().show();
    }

    private void pickGallery() {

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {

        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){

        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result && result1;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted=grantResults[0]==
                            PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean writeStorageAccepted=grantResults[0]==
                            PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickGallery();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case WRITE_EXTERNAL_STORAGE_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                    saveToText(mstring);
                }
                else{
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){

            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
            if(requestCode==IMAGE_PICK_CAMERA_CODE){

                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);

            }




        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode== RESULT_OK){
                Uri resultUri=result.getUri();
                mPreviewIv.setImageURI(resultUri);
                BitmapDrawable bitmapDrawable=(BitmapDrawable)mPreviewIv.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();

                TextRecognizer recognizer= new TextRecognizer.Builder(getApplicationContext()).build();
                if(!recognizer.isOperational()){
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

                }
                else{
                    Frame frame= new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items=recognizer.detect(frame);
                    StringBuilder sb=new StringBuilder();
                    for(int i=0;i<items.size();i++){
                        TextBlock myItem=items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    mResultEt.setText(sb.toString());

                }
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();

            }

        }
    }
}
