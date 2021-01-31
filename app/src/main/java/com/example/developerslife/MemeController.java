package com.example.developerslife;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.ArrayList;

/*
    Класс для управления выводом мемов на экран.
*/
public class MemeController {
    public final int MODE_TOP = 0; // Режим "топ"
    public final int MODE_NEW = 1; // Режим "новое"
    public final int MODE_RANDOM = 2; // Режим "рандом"

    private int mode; // Хранит номер выбранного режима

    // ---------------------------------------------------------------------------------------------
    /*
        Списки закэшированных мемов из каждой категории, а также
        номера текущих позиций в каждой категории.

        Нужны для сохранения состояния при переключении режимов и для
        переключений "вперёд-назад" внутри одной категории.
    */
    private ArrayList<Meme> memesRandom = new ArrayList<>();
    private int currentRandomMemeIndex;

    private ArrayList<Meme> memesTop = new ArrayList<>();
    private int currentTopMemeIndex;

    private ArrayList<Meme> memesNew = new ArrayList<>();
    private int currentNewMemeIndex;
    // ---------------------------------------------------------------------------------------------

    private Context context; // Контекст, в котором происходит работа
    private ImageView imageViewMeme; // ImageView, в который надо выводить изображения
    private TextView textViewDescriptionMeme; // TextView, в который надо выводить описание

    public MemeController(Context context, ImageView imageViewMeme, TextView textViewDescriptionMeme) {
        this.context = context;
        this.imageViewMeme = imageViewMeme;
        this.textViewDescriptionMeme = textViewDescriptionMeme;
        currentRandomMemeIndex = -1;
        currentTopMemeIndex = -1;
        currentNewMemeIndex = -1;
        setMode(MODE_NEW);
    }

    // Функция устанавливает следующий мем (если есть, то из кэша, иначе - с сайта) в зависимости от категории.
    public void nextMeme() {
        switch (mode) {
            case MODE_RANDOM:
                nextRandomMeme();
                break;

            case MODE_TOP:
                nextTopMeme();
                break;

            case MODE_NEW:
                nextNewMeme();
                break;

            default:
                Toast.makeText(context, R.string.loading_error, Toast.LENGTH_LONG).show();
        }

    }

    // Функция устанавливает предыдущий мем (если он есть в кэше) в зависимости от категории.
    public void previousMeme() {
        switch (mode) {
            case MODE_RANDOM:
                previousRandomMeme();
                break;

            case MODE_TOP:
                previousTopMeme();
                break;

            case MODE_NEW:
                previousNewMeme();
                break;

            default:
                Toast.makeText(context, R.string.loading_error, Toast.LENGTH_LONG).show();

        }

    }

    // Функция проверяет, является ли текущий элемент первым в своей категории.
    public boolean isFirstPosition() {
        switch (mode) {
            case MODE_RANDOM:
                return currentRandomMemeIndex == 0;
            case MODE_TOP:
                return currentTopMemeIndex == 0;
            case MODE_NEW:
                return currentNewMemeIndex == 0;
        }
        return false;
    }

    /*
        Функция проверяет, загружен ли хотя бы один элемент из текущей категории.
        Возвращает true, если элементов нет.
    */
    public boolean isNullPosition() {
        switch (mode) {
            case MODE_RANDOM:
                return currentRandomMemeIndex == -1;
            case MODE_TOP:
                return currentTopMemeIndex == -1;
            case MODE_NEW:
                return currentNewMemeIndex == -1;
        }
        return false;
    }

    /*
        "Обновляет" мем (картинку и текстовое описание) в соответствии с выбранной категорией.

        Вызывается при смене категории.
    */
    public void updateMeme() {
        /*
            Если в кэше этой категории нет элементов, то сначала устанавливаем текст и иозображение ошибки
            (на случай, если не удастся загрузить), а потом выполняем загрузку, вызывая метод nextMeme().

             Если же в кэше есть элементы, то берём следующий и устанавливаем его.
        */
        if (isNullPosition()) {
            textViewDescriptionMeme.setText("");
            imageViewMeme.setImageResource(R.drawable.error);
            nextMeme();
            return;
        }

        Meme meme = null;
        switch (mode) {
            case MODE_RANDOM:
                meme = memesRandom.get(currentRandomMemeIndex);
                break;
            case MODE_TOP:
                meme = memesTop.get(currentTopMemeIndex);
                break;
            case MODE_NEW:
                meme = memesNew.get(currentNewMemeIndex);
                break;
        }
        setMeme(meme);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    /*
        Выводит следующий мем из категории "рандом".
        Если есть - то из кэша, иначе - загружает с сайта.
    */
    private void nextRandomMeme() {
        Meme meme = null;
        if (currentRandomMemeIndex + 1 < memesRandom.size()) {
            meme = memesRandom.get(currentRandomMemeIndex + 1);
        } else {
            MemeReceiver memeReceiver = new MemeReceiver();
            try {
                meme = memeReceiver.getRandomMeme();
            } catch (InterruptedException | JSONException e) {
                Toast.makeText(context, R.string.loading_error, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            if (meme != null) {
                memesRandom.add(meme);
            }
        }
        if (meme != null) {
            setMeme(meme);
            currentRandomMemeIndex++;
        }
    }

    /*
        Выводит следующий мем из категории "топ".
        Если есть - то из кэша, иначе - загружает с сайта.
    */
    private void nextTopMeme() {
        Meme meme = null;
        if (currentTopMemeIndex + 1 < memesTop.size()) {
            meme = memesTop.get(currentTopMemeIndex + 1);
        } else {
            MemeReceiver memeReceiver = new MemeReceiver();
            try {
                meme = memeReceiver.getTopMeme(currentTopMemeIndex + 1);
            } catch (InterruptedException | JSONException e) {
                Toast.makeText(context, R.string.loading_error, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            if (meme != null) {
                memesTop.add(meme);
            }
        }
        if (meme != null) {
            setMeme(meme);
            currentTopMemeIndex++;
        }
    }

    /*
        Выводит следующий мем из категории "новое".
        Если есть - то из кэша, иначе - загружает с сайта.
    */
    private void nextNewMeme() {
        Meme meme = null;
        if (currentNewMemeIndex + 1 < memesNew.size()) {
            meme = memesNew.get(currentNewMemeIndex + 1);
        } else {
            MemeReceiver memeReceiver = new MemeReceiver();
            try {
                meme = memeReceiver.getNewMeme(currentNewMemeIndex + 1);
            } catch (InterruptedException | JSONException e) {
                Toast.makeText(context, R.string.loading_error, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            if (meme != null) {
                memesNew.add(meme);
            }
        }
        if (meme != null) {
            setMeme(meme);
            currentNewMemeIndex++;
        }
    }

    /*
        Выводит предыдущий мем из категории "рандом".
        Если есть - то из кэша, иначе - загружает с сайта.
    */
    private void previousRandomMeme() {
        if (currentRandomMemeIndex > 0) {
            currentRandomMemeIndex--;
            Meme meme = memesRandom.get(currentRandomMemeIndex);
            setMeme(meme);
        }
    }

    /*
        Выводит предыдущий мем из категории "топ".
        Если есть - то из кэша, иначе - загружает с сайта.
    */
    private void previousTopMeme() {
        if (currentTopMemeIndex > 0) {
            currentTopMemeIndex--;
            Meme meme = memesTop.get(currentTopMemeIndex);
            setMeme(meme);
        }
    }

    /*
        Выводит предыдущий мем из категории "новое".
        Если есть - то из кэша, иначе - загружает с сайта.
    */
    private void previousNewMeme() {
        if (currentNewMemeIndex > 0) {
            currentNewMemeIndex--;
            Meme meme = memesNew.get(currentNewMemeIndex);
            setMeme(meme);
        }
    }

    /*
        Устанавливает gif-изображение и текстовое описание мема на соответствующие элементы активности.
    */
    private void setMeme(Meme meme) {
        Glide
                .with(context)
                .load(meme.getGifUrl())
                .into(imageViewMeme);

        textViewDescriptionMeme.setText(meme.getDescription());
    }


}
