package com.example.developerslife;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.ArrayList;

public class MemeController {
    private ArrayList<Meme> memes = new ArrayList<>();

    private Context context;
    private ImageView imageViewMeme;
    private TextView textViewDescriptionMeme;
    private int currentMemeIndex;

    public MemeController(Context context, ImageView imageViewMeme, TextView textViewDescriptionMeme) {
        this.context = context;
        this.imageViewMeme = imageViewMeme;
        this.textViewDescriptionMeme = textViewDescriptionMeme;
        currentMemeIndex = -1;
    }

    public void nextMeme() {
        Meme meme = null;

        if (currentMemeIndex + 1 < memes.size()) {
            meme = memes.get(currentMemeIndex + 1);
        } else {
            MemeReceiver memeReceiver = new MemeReceiver();
            try {
                meme = memeReceiver.getRandomMeme();
            } catch (InterruptedException | JSONException e) {
                textViewDescriptionMeme.setText(R.string.loading_error);
                e.printStackTrace();
            } finally {
            }
            memes.add(meme);
        }

        setMeme(meme);
        currentMemeIndex++;
    }

    public void previousMeme() {

        if (currentMemeIndex > 0) {
            currentMemeIndex--;
            Meme meme = memes.get(currentMemeIndex);
            setMeme(meme);
        }

    }

    public boolean isFirstPosition() {
        return currentMemeIndex == 0;
    }

    private void setMeme(Meme meme) {
        Glide
                .with(context)
                .load(meme.getGifUrl())
                .into(imageViewMeme);

        textViewDescriptionMeme.setText(meme.getDescription());
    }

}
