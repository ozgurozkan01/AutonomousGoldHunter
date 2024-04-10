package com.example.prolab2_1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Node {
    Rectangle rectangle;
    boolean isObstaclePlaced = true;
    boolean isPlayerMoved = true;
    boolean isSeen = false;
    boolean isChecked = false;
    boolean isOpen = false;
    boolean canBeUsedOnPathfinding = true;
    Enum obstacleType;
    Treasure treasure;
    InputStream imagePath;
    ImageView imageView;
    Image image;

    Node parent;
    int column;
    int row;
    int gCost;
    int hCost;
    int fCost;

    boolean start;
    boolean goal;

    Node() throws FileNotFoundException {
        imagePath = new FileInputStream("pictures/fog.jpg");
        image = new Image(imagePath);
        imageView = new ImageView(image);
    }

    public void setImageViewPosition(int x, int y) {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(rectangle.getWidth());
        imageView.setFitHeight(rectangle.getHeight());
    }

    public void setAsStart(boolean isStart) {
        start = isStart;
    }

    public void setAsGoal(boolean isGoal) {
        goal = isGoal;
    }

    public void setAsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setAsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setAsPath() {
    }
}

/* TAMAM */
