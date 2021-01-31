package com.example.developerslife;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// Класс для загрузки данных с сайта developerslife
public class MemeReceiver {
    private final String GET_RANDOM_MEME_URL = "https://developerslife.ru/random?json=true"; // Ссылка для загрузки случайного мема
    private final String GET_TOP_MEMES_URL = "https://developerslife.ru/top/%s?json=true"; // Ссылка для загрузки мемов из раздела "топ"
    private final String GET_NEW_MEMES_URL = "https://developerslife.ru/latest/%s?json=true"; // Ссылка для загрузки мемов из раздела "последние" (новые)

    private final String GIF_URL_KEY = "gifURL"; // Ключ для получения ссылки на gif из json объекта
    private final String GIF_DESCRIPTION_KEY = "description"; // Ключ для получения текстового описания из json объекта
    private final String MEMES_RESULT_ARRAY_KEY = "result"; // Ключ для получения json-массива из json-объекта
    private final int MEMES_PER_PAGE = 5; // Размер одного массива (страницы)

    /*
       Функция возвращает случайный мем с сайта developerslife.
       Здесь используется AsyncTask для получения json-объекта по ссылке.
       Далее из json-объекта создаётся объект типа Meme.
    */
    public Meme getRandomMeme() throws InterruptedException, JSONException {
        GetMemeTask getMemeTask = new GetMemeTask();
        try {
            JSONObject json = getMemeTask.execute(GET_RANDOM_MEME_URL).get();
            if (json == null) {
                throw new JSONException("Ошибка загрузки!");
            }
            String gifUrl = json.getString(GIF_URL_KEY);
            String descriptionGif = json.getString(GIF_DESCRIPTION_KEY);
            return new Meme(gifUrl, descriptionGif);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InterruptedException();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Ошибка загрузки.");
        }
        return null;
    }

    /*
       Функция возвращает мем с сайта developerslife из раздела "топ".
       Параметр topIndex описывает, каким по счёту должен быть мем (какое место он занимает в топе).

       Здесь используется AsyncTask для получения json-объекта по ссылке.
       Далее из json-объекта создаётся объект типа Meme.
    */
    public Meme getTopMeme(int topIndex) throws InterruptedException, JSONException {

        GetMemeTask getMemeTask = new GetMemeTask();
        try {

            int pageNumber = (int) (topIndex / MEMES_PER_PAGE);
            int memeIndex = topIndex % MEMES_PER_PAGE;

            JSONObject json = getMemeTask.execute(String.format(GET_TOP_MEMES_URL, pageNumber)).get();
            if (json == null) {
                throw new JSONException("Ошибка загрузки!");
            }
            JSONArray memesJsonArray = json.getJSONArray(MEMES_RESULT_ARRAY_KEY);
            ArrayList<Meme> result = new ArrayList<>();
            for (int i = 0; i < MEMES_PER_PAGE; i++) {
                JSONObject memeSingleJson = memesJsonArray.getJSONObject(i);
                String gifUrl = memeSingleJson.getString(GIF_URL_KEY);
                String descriptionGif = memeSingleJson.getString(GIF_DESCRIPTION_KEY);
                result.add(new Meme(gifUrl, descriptionGif));
            }
            return result.get(memeIndex);
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InterruptedException();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Ошибка загрузки.");
        }

        return null;
    }

    /*
       Функция возвращает мем с сайта developerslife из раздела "последнее" (новое).
       Параметр newIndex описывает, каким по счёту должен быть мем (какое место он занимает в списке нового).

       Здесь используется AsyncTask для получения json-объекта по ссылке.
       Далее из json-объекта создаётся объект типа Meme.
    */
    public Meme getNewMeme(int newIndex) throws InterruptedException, JSONException {

        GetMemeTask getMemeTask = new GetMemeTask();
        try {

            int pageNumber = (int) (newIndex / MEMES_PER_PAGE);
            int memeIndex = newIndex % MEMES_PER_PAGE;

            JSONObject json = getMemeTask.execute(String.format(GET_NEW_MEMES_URL, pageNumber)).get();
            if (json == null) {
                throw new JSONException("Ошибка загрузки!");
            }
            JSONArray memesJsonArray = json.getJSONArray(MEMES_RESULT_ARRAY_KEY);
            ArrayList<Meme> result = new ArrayList<>();
            for (int i = 0; i < MEMES_PER_PAGE; i++) {
                JSONObject memeSingleJson = memesJsonArray.getJSONObject(i);
                String gifUrl = memeSingleJson.getString(GIF_URL_KEY);
                String descriptionGif = memeSingleJson.getString(GIF_DESCRIPTION_KEY);
                result.add(new Meme(gifUrl, descriptionGif));
            }
            return result.get(memeIndex);
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InterruptedException();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Ошибка загрузки.");
        }

        return null;
    }

    /*
        Класс для получения json-объекта по ссылке.
    */
    private static class GetMemeTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
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
