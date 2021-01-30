package com.example.developerslife;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MemeTest {

    private final String MEME_GIF_URL = "http://static.devli.ru/public/images/gifs/201909/c61fd928-af03-4487-9a92-bf84fbaea14d.gif";
    private final String MEME_DESCRIPTION = "Скрипт работать перестал. Пришлось новые данные вручную править.";
    private Meme meme;

    @Before
    public void beforeTests() {
        meme = new Meme(MEME_GIF_URL, MEME_DESCRIPTION);
    }

    @Test
    public void newMemeNotNullTest() {
        Assert.assertNotNull(meme);
    }

    @Test
    public void newMemeGifUrlIsCorrectTest() {
        String actualUrl = meme.getGifUrl();
        String correctUrl = MEME_GIF_URL;
        Assert.assertEquals(actualUrl, correctUrl);
    }

    @Test
    public void newMemeDescriptionIsCorrectTest() {
        String actualDescription = meme.getDescription();
        String correctDescription = MEME_DESCRIPTION;
        Assert.assertEquals(actualDescription, correctDescription);
    }

}
