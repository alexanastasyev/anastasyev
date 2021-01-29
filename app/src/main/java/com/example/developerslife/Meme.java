package com.example.developerslife;

public class Meme {
    private String gifUrl;
    private String description;

    public Meme(String gifUrl, String description) {
        this.gifUrl = gifUrl;
        this.description = description;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public String getDescription() {
        return description;
    }
}
