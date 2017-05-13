package com.example.slaby.android_6;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        userImageFolder = getAlbumStorageDir(this, "Photos");
        userImagesList = getUserImagesFiles();
        gridImagePreview = (GridLayout) findViewById(R.id.gridImagePreview);
        addImagesToGrid();
    }

    public void addImagesToGrid() {
        List<ImageView> imageViewList = getImageViewsFromFiles(userImagesList);
        for (int i = 0; i < imageViewList.size(); i++) {
            View tmp = imageViewList.get(i);
            tmp.setPadding(10,10,10,10);
            tmp.setMinimumWidth(400);
            tmp.setMinimumHeight(400);
            gridImagePreview.addView(tmp);
        }
    }

    public List<ImageView> getImageViewsFromFiles(List<File> files) {
        List<ImageView> imageViewList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File tmp = files.get(i);
            Bitmap bitmap = BitmapFactory.decodeFile(tmp.getPath());
            ImageView tmpImageView = new ImageView(this);
            tmpImageView.setImageBitmap(bitmap);
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
}
