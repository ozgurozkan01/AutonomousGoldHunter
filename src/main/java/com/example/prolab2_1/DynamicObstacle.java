package com.example.prolab2_1;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

enum DynamicObstacles {
    BEE,
    BIRD
}

public class DynamicObstacle extends ObstacleBase{
    private TranslateTransition translateObstacle = new TranslateTransition();
    Enum species;
    int visitFieldX;
    int visitFieldY;

    DynamicObstacle (String imagePath, int visitFieldX, int visitFieldY,int sizeX, int sizeY, Enum species) throws FileNotFoundException {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.visitFieldX = visitFieldX;
        this.visitFieldY = visitFieldY;
        this.species = species;
        this.imagePath = new FileInputStream(imagePath);
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setFitHeight(this.sizeY * HelloApplication.rectangleAndGapSize);
        imageView.setFitWidth(this.sizeX * HelloApplication.rectangleAndGapSize);
        translateObstacle.setNode(imageView);
        translateObstacle.setDuration(Duration.millis(1500));
        translateObstacle.setCycleCount(TranslateTransition.INDEFINITE);
        translateObstacle.setAutoReverse(true);

        if (species == DynamicObstacles.BEE)
            translateObstacle.setByX((visitFieldX - sizeX) * HelloApplication.rectangleAndGapSize);
        else
            translateObstacle.setByY((visitFieldY - sizeY) * HelloApplication.rectangleAndGapSize);

        translateObstacle.play();
    }
}

/* TAMAM */