package com.example.developerslife;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewGif;
    private TextView textViewDescription;
    private FloatingActionButton floatingActionButtonPrevious;

    private MemeController memeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        floatingActionButtonPrevious = findViewById(R.id.floatingActionButtonPrevious);

        imageViewGif = findViewById(R.id.imageViewGif);
        textViewDescription = findViewById(R.id.textViewDescription);

        memeController = new MemeController(this, imageViewGif, textViewDescription);
        memeController.nextMeme();

    }

    public void onClickNext(View view) {
        memeController.nextMeme();
        floatingActionButtonPrevious.setClickable(true);
        floatingActionButtonPrevious.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.clickable)));
    }

    public void onClickPrevious(View view) {
        memeController.previousMeme();
        if (memeController.isFirstPosition()) {
            floatingActionButtonPrevious.setClickable(false);
            floatingActionButtonPrevious.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.not_clickable)));
        }
    }
}