package com.example.prolab2_1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

enum TreasureType {
    GOLD,
    SILVER,
    EMERALD,
    COPPER
}

enum TreasureState{
    OPEN,
    CLOSE
}

public class Treasure {
    private Enum treasureType;
    private TreasureState treasureState;
    protected int sizeX;
    protected int sizeY;
    protected Image image;
    protected InputStream imagePath;
    protected ImageView imageView;
    protected ArrayList<Node> nodes = new ArrayList<>();

    Treasure(String imagePath, Enum treasureType) throws FileNotFoundException {
        treasureState = TreasureState.CLOSE;
        this.treasureType = treasureType;
        this.imagePath = new FileInputStream(imagePath);
        sizeX = 2;
        sizeY = 2;
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setFitHeight(sizeY * HelloApplication.rectangleAndGapSize);
        imageView.setFitWidth(sizeX * HelloApplication.rectangleAndGapSize);
    }

    public void updateImage(String imagePath) throws FileNotFoundException {
        InputStream newImagePath = new FileInputStream(imagePath);
        Image newImage = new Image(newImagePath);
        imageView.setImage(newImage);
    }

    public Enum getTreasureType() { return treasureType; }

    public TreasureState getTreasureState() { return treasureState; }

    public void setTreasureState(TreasureState state) { treasureState = state; }
}