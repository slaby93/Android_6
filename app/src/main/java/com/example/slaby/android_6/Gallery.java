package com.example.slaby.android_6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gallery extends AppCompatActivity {
    File userImageFolder;
    List<File> userImagesList;
    GridLayout gridImagePreview;
    int index = 0;
    int numberOfImages = 9;
    String selectedImagePath = "";
    List<ImageView> imageViewList;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        userImageFolder = getAlbumStorageDir(this, "Photos");
        userImagesList = getUserImagesFiles();
        prefs = getSharedPreferences("com.example.slaby.android_6", Context.MODE_PRIVATE);

        gridImagePreview = (GridLayout) findViewById(R.id.gridImagePreview);
        imageViewList = getImageViewsFromFiles(userImagesList);
        setImagesOnGrid(index);
    }

    public void refreshDirectory() {
        userImagesList = getUserImagesFiles();
        imageViewList = getImageViewsFromFiles(userImagesList);
    }

    public void setImagesOnGrid(int index) {
        gridImagePreview.removeAllViews();

        for (int i = index; i < index + numberOfImages; i++) {
            if (i >= imageViewList.size()) {
                return;
            }
            View tmp = imageViewList.get(i);

            tmp.setPadding(10, 10, 10, 10);
            tmp.setMinimumWidth(350);
            tmp.setMinimumHeight(450);
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imagePath = (ImageView) v;
                    String path = imagePath.getTag().toString();
                    savePathToPrefs(path);
                    goBackToMain();
                }
            });
            gridImagePreview.addView(tmp);
        }
    }

    public void goBackToMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    public void savePathToPrefs(String path) {
        prefs.edit().putString("sciezka", path).apply();
    }

    public List<ImageView> getImageViewsFromFiles(List<File> files) {
        List<ImageView> imageViewList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File tmp = files.get(i);
            Bitmap bitmap = BitmapFactory.decodeFile(tmp.getPath());
            ImageView tmpImageView = new ImageView(this);
            tmpImageView.setTag(tmp.getAbsoluteFile());
            tmpImageView.setImageBitmap(bitmap);
            tmpImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageView imagePath = (ImageView) v;
                    String path = imagePath.getTag().toString();
                    File f = new File(path);
                    f.delete();
                    refreshDirectory();
                    index = 0;
                    setImagesOnGrid(index);
                    return true;
                }
            });
            imageViewList.add(tmpImageView);
        }
        return imageViewList;
    }

    public List<File> getUserImagesFiles() {
        return Arrays.asList(userImageFolder.listFiles());
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

    public void onNext(View view) {
        index = Math.min(imageViewList.size() - numberOfImages, index + numberOfImages);
        setImagesOnGrid(index);
    }

    public void onPrev(View view) {

        index = Math.max(0, index - numberOfImages);


        setImagesOnGrid(index);
    }

}
