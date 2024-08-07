package com.example.mygoogleassistant.functions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mygoogleassistant.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class GoogleLensActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE=1000;
    private static final int IMAGE_PICK_CAMERA_CODE=1001;
    String[] cameraPermission;
    String[] storagePermission;
    EditText mResultEt;
    ImageView mPreviewIv;
    Uri image_uri;
    Button b;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_lens);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setSubtitle("Insert or Click Photos");
        mResultEt=findViewById(R.id.resultedt);
        mPreviewIv=findViewById(R.id.ImageViewpre);
        b=findViewById(R.id.button);
        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES};
        storagePermission=new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm =mResultEt.getText().toString();
                if (!nm.isEmpty()) {
                    Uri uri = Uri.parse("https://www.google.com/search?q="+nm);
                    Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(gSearchIntent);
                }
                else{
                    Toast.makeText(GoogleLensActivity.this, "Add an Image With Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.addImage){
            shoeImageImportDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void shoeImageImportDialog() {
        String[] items={"Camera","Gallery"};
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which==0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (!checkCameraPermission()) {
                            requestCameraPermission();

                        }
                        else{
                            pickCamera();
                        }
                    }
                }

                if (which==1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (!checkStoragePermission()) {
                            requestStoragePermission();

                        }
                        else{
                            pickGallery();
                        }
                    }

                }

            }
        });
        dialog.create().show();
    }

    private void pickCamera() {
        //User can only capture image using Camera
        ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .start(IMAGE_PICK_CAMERA_CODE);


        /*ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Text");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(i,IMAGE_PICK_CAMERA_CODE);*/


    }
    private void pickGallery() {
        //User can only select image from Gallery
        //Default Request Code is ImagePicker.REQUEST_CODE
        ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .start(IMAGE_PICK_GALLERY_CODE);

        /*Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);*/
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQUEST_CODE);
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkStoragePermission() {
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)==(PackageManager.PERMISSION_GRANTED);
        return  result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickCamera();

                    } else {
                        Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();

                    } else {
                        Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                assert data != null;
                image_uri=data.getData();
                mPreviewIv.setImageURI(image_uri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();

                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myitems = items.valueAt(i);
                        sb.append(myitems.getValue());
                        sb.append("\n");

                    }
                    mResultEt.setText(sb.toString());
                }


            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                assert data != null;
                image_uri=data.getData();
                mPreviewIv.setImageURI(image_uri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();

                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myitems = items.valueAt(i);
                        sb.append(myitems.getValue());
                        sb.append("\n");

                    }
                    mResultEt.setText(sb.toString());
                }


            }
        }
        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();

                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myitems = items.valueAt(i);
                        sb.append(myitems.getValue());
                        sb.append("\n");

                    }
                    mResultEt.setText(sb.toString());
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }*/
    }
}