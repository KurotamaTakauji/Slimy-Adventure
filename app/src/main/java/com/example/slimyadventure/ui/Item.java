package com.example.slimyadventure.ui;

public class Item {

    String name;
    String score;
    int image;

    public Item(String name, String score, int image) {
        this.name = name;
        this.score = score;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
