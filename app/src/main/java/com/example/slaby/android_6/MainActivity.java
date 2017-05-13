package com.example.slaby.android_6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    Bitmap image;
    ImageView imagePreview;
    File userImageFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imagePreview = (ImageView) findViewById(R.id.imagePreview);
        userImageFolder = getAlbumStorageDir(this, "Photos");
    }

    @Override
    protected void onActivityResult(int reqId, int resId, Intent i) {
        image = (Bitmap) i.getExtras().get("data");
        imagePreview.setImageBitmap(image);
        saveImage();
    }

    public void openSystemCamera(View view) {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    public void saveImage() {
        System.out.println("SAVE IMAGE");
        File imageFile = new File(userImageFolder,"test.jpeg");
        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

    public void goToGallery() {
        System.out.println("GALLERY BUTTON");
    }
}
