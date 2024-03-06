package com.example.prolab2_1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Treasure {
    private String type;
    protected int sizeX;
    protected int sizeY;
    protected Image image;
    protected InputStream imagePath;
    protected ImageView imageView;

    Treasure(String type, String imagePath) throws FileNotFoundException {
        this.type = type;
        this.imagePath = new FileInputStream(imagePath);
        sizeX = 3;
        sizeY = 3;
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setFitHeight(sizeY * 10);
        imageView.setFitWidth(sizeX * 10);
    }
}