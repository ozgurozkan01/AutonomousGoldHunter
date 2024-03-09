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

public class StaticObstacle extends ObstacleBase{
    private String season;
    private TypeObstacles obstacleType;

    StaticObstacle(String imagePath, int sizeX, int sizeY, String season, TypeObstacles obstacleType) throws FileNotFoundException {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.season = season;
        this.imagePath = new FileInputStream(imagePath);
        this.obstacleType = obstacleType;
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setFitHeight(sizeY * 10);
        imageView.setFitWidth(sizeX * 10);
    }

    public String getSeason(){
        return season;
    }
    public TypeObstacles getObstacleType() {
        return obstacleType;
    }
}
