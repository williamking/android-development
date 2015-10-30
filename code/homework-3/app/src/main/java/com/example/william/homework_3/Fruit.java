package com.example.william.homework_3;

/**
 * Created by william on 15-10-30.
 */
public class Fruit {
    private String name;
    private int imageId;

    public Fruit(String name, int id) {
        this.name = name;
        this.imageId = id;
    }

    public String getName() {
        return this.name;
    }

    public int getImageId() {
        return this.imageId;
    }

}
