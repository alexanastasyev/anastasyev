package com.example.developerslife;

// Класс описывает мем, загружаемый из интернета
public class Meme {
    private String gifUrl; // Ссылка на gif-изображение
    private String description; // Текстовое описание

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
