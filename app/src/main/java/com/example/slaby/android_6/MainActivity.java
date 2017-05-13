package com.example.slaby.android_6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Bitmap image;
    ImageView imagePreview;
    File userImageFolder;
    SimpleDateFormat dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imagePreview = (ImageView) findViewById(R.id.imagePreview);
        userImageFolder = getAlbumStorageDir(this, "Photos");
        dt = new SimpleDateFormat("yyyyymmdd_hhmmss");
    }

    @Override
    protected void onActivityResult(int reqId, int resId, Intent i) {
        try {
            image = (Bitmap) i.getExtras().get("data");
            imagePreview.setImageBitmap(image);
            saveImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openSystemCamera(View view) {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    public void saveImage() {
        try {
            File imageFile = new File(userImageFolder, "image_" + dt.format(new Date()) + ".jpeg");
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            System.out.println("File saved to: " + imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            System.out.println("Directory not found");
        }
        return file;
    }

    public void goToGallery(View view) {
        System.out.println("Going to Gallery");
        Intent myIntent = new Intent(this, Gallery.class);
        startActivity(myIntent);
    }
}
