package com.example.prolab2_1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

enum TypeObstacles {
    TREE,
    ROCK,
    WALL,
    MOUNTAIN
}

enum TreeObstacles {
    TREE2X2,
    TREE3X3,
    TREE4X4,
    TREE5X5
}

enum RockObstacles {
    ROCK2X2,
    ROCK3X3
}

enum Season {
    WINTER,
    SUMMER
}

public class StaticObstacle extends ObstacleBase{
    Season season;
    TypeObstacles obstacleType;

    StaticObstacle(String imagePath, int sizeX, int sizeY, Season season, TypeObstacles obstacleType) throws FileNotFoundException {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.season = season;
        this.obstacleType = obstacleType;
        this.imagePath = new FileInputStream(imagePath);
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setFitHeight(sizeY * HelloApplication.rectangleAndGapSize);
        imageView.setFitWidth(sizeX * HelloApplication.rectangleAndGapSize);
    }

    public TypeObstacles getObstacleType() {
        return obstacleType;
    }
}

/* TAMAM */