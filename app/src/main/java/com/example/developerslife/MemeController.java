package com.example.developerslife;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.ArrayList;

public class MemeController {
    public final int MODE_TOP = 0;
    public final int MODE_NEW = 1;
    public final int MODE_RANDOM = 2;

    private ArrayList<Meme> memesRandom = new ArrayList<>();
    private int currentRandomMemeIndex;

    private ArrayList<Meme> memesTop = new ArrayList<>();
    private int currentTopMemeIndex;

    private ArrayList<Meme> memesNew = new ArrayList<>();
    private int currentNewMemeIndex;

    private Context context;
    private ImageView imageViewMeme;
    private TextView textViewDescriptionMeme;
    private int mode;

    public MemeController(Context context, ImageView imageViewMeme, TextView textViewDescriptionMeme) {
        this.context = context;
        this.imageViewMeme = imageViewMeme;
        this.textViewDescriptionMeme = textViewDescriptionMeme;
        currentRandomMemeIndex = -1;
        currentTopMemeIndex = -1;
        currentNewMemeIndex = -1;
        mode = MODE_RANDOM;
    }

    public void nextMeme() {
        Meme meme = null;
        switch (mode) {
            case MODE_RANDOM:
                if (currentRandomMemeIndex + 1 < memesRandom.size()) {
                    meme = memesRandom.get(currentRandomMemeIndex + 1);
                } else {
                    MemeReceiver memeReceiver = new MemeReceiver();
                    try {
                        meme = memeReceiver.getRandomMeme();
                    } catch (InterruptedException | JSONException e) {
                        textViewDescriptionMeme.setText(R.string.loading_error);
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
                break;

            case MODE_TOP:
                if (currentTopMemeIndex + 1 < memesTop.size()) {
                    meme = memesTop.get(currentTopMemeIndex + 1);
                } else {
                    MemeReceiver memeReceiver = new MemeReceiver();
                    try {
                        meme = memeReceiver.getTopMeme(currentTopMemeIndex + 1);
                    } catch (InterruptedException | JSONException e) {
                        textViewDescriptionMeme.setText(R.string.loading_error);
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
                break;

            case MODE_NEW:
                if (currentNewMemeIndex + 1 < memesNew.size()) {
                    meme = memesNew.get(currentNewMemeIndex + 1);
                } else {
                    MemeReceiver memeReceiver = new MemeReceiver();
                    try {
                        meme = memeReceiver.getNewMeme(currentNewMemeIndex + 1);
                    } catch (InterruptedException | JSONException e) {
                        textViewDescriptionMeme.setText(R.string.loading_error);
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
                break;

            default:
                textViewDescriptionMeme.setText(R.string.loading_error);
        }

    }

    public void previousMeme() {
        switch (mode) {
            case MODE_RANDOM:
                if (currentRandomMemeIndex > 0) {
                    currentRandomMemeIndex--;
                    Meme meme = memesRandom.get(currentRandomMemeIndex);
                    setMeme(meme);
                }
                break;

            case MODE_TOP:
                if (currentTopMemeIndex > 0) {
                    currentTopMemeIndex--;
                    Meme meme = memesTop.get(currentTopMemeIndex);
                    setMeme(meme);
                }
                break;

            case MODE_NEW:
                if (currentNewMemeIndex > 0) {
                    currentNewMemeIndex--;
                    Meme meme = memesNew.get(currentNewMemeIndex);
                    setMeme(meme);
                }
                break;

            default:
                textViewDescriptionMeme.setText(R.string.loading_error);

        }

    }

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

    public void updateMeme() {
        if (isNullPosition()) {
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

    private void setMeme(Meme meme) {
        Glide
                .with(context)
                .load(meme.getGifUrl())
                .into(imageViewMeme);

        textViewDescriptionMeme.setText(meme.getDescription());
    }


}
