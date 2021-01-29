package com.example.developerslife;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MemeReceiver {
    private final String GET_RANDOM_MEME_URL = "https://developerslife.ru/random?json=true";
    private final String GIF_URL_KEY = "gifURL";
    private final String GIF_DESCRIPTION_KEY = "description";

    public Meme getRandomMeme() {

        GetMemeTask getMemeTask = new GetMemeTask();
        try {
            JSONObject json = getMemeTask.execute(GET_RANDOM_MEME_URL).get();
            String gifUrl = json.getString(GIF_URL_KEY);

            String descriptionGif = json.getString(GIF_DESCRIPTION_KEY);
            return new Meme(gifUrl, descriptionGif);


        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static class GetMemeTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

            publishProgress();

            URL url = null;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                String jsonString = stringBuilder.toString();

                try {
                    JSONObject result = new JSONObject(jsonString);
                    return result;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return null;
        }
    }

}
