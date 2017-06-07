package com.example.slaby.android_6;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Bitmap image;
    ImageView imagePreview;
    File userImageFolder;
    SimpleDateFormat dt;
    SharedPreferences prefs;
    RatingBar ratingBar;
    File currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        this.imagePreview = (ImageView) findViewById(R.id.imagePreview);
        userImageFolder = getAlbumStorageDir(this, "Photos");
        dt = new SimpleDateFormat("yyyyymmdd_hhmmss");
        prefs = getSharedPreferences("com.example.slaby.android_6", Context.MODE_PRIVATE);
        String pathToOpen = prefs.getString("sciezka", null);

        this.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (imagePreview.getTag() == null) {
                    System.out.println("RETURN");
                    return;
                }
                String path = imagePreview.getTag().toString();
                prefs.edit().putFloat(path, rating).apply();
            }
        });
        if (pathToOpen != null) {
            setImage(pathToOpen);
        }

    }


    public void setImage(String pathToOpen) {
        if (prefs.contains(pathToOpen)) {
            ratingBar.setRating(prefs.getFloat(pathToOpen, (float) 0));
        }
        currentImage = new File(pathToOpen);
        Bitmap bitmap = BitmapFactory.decodeFile(currentImage.getPath());
        imagePreview.setTag(currentImage.getAbsoluteFile());
        imagePreview.setImageBitmap(bitmap);
    }

    public void onFileNameChange(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.changefilename);

        Button close = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        TextView tw = (TextView) dialog.findViewById(R.id.editText);
        tw.setText(currentImage.getName());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CANCEL");
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OK");
                TextView tw = (TextView) dialog.findViewById(R.id.editText);
                changeFileName(tw.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void changeFileName(String newName) {
        File imageFile = new File(userImageFolder, newName);
        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            Bitmap bitmap = BitmapFactory.decodeFile(currentImage.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            System.out.println("File saved to: " + imageFile);
            currentImage.delete();
            currentImage = imageFile;
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("changeFileName " + currentImage.getName() + " new name: " + newName);
    }

    public void onRatingClick(View view) {
        System.out.println("onRatingClick" + view.toString());
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
