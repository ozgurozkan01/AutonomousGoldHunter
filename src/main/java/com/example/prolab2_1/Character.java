package com.example.prolab2_1;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

enum MotionDirection{
    RIGHT,
    LEFT,
    UP,
    DOWN
}

public class Character {
    int lastLocationX;
    int lastLocationY;
    int locationX;
    int locationY;
    int characterSizeX;
    int characterSizeY;
    int currentRectangleIndex;
    InputStream imagePath;
    ImageView imageView;
    Image image;
    MotionDirection direction;
    // It is for intersecting just one time. but never updating second time
    boolean intersectionController = true;

    public Character(String imagePath, int locationX, int locationY, int characterSizeX, int characterSizeY) throws FileNotFoundException {
        this.locationX = locationX;
        this.locationY = locationY;
        this.lastLocationX = locationX * 10;
        this.lastLocationY = locationY * 10;
        this.characterSizeX = characterSizeX * 10;
        this.characterSizeY = characterSizeY * 10;
        this.imagePath = new FileInputStream(imagePath);
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setX(locationX * 10);
        imageView.setY(locationY * 10);
        imageView.setFitHeight(this.characterSizeY);
        imageView.setFitWidth(this.characterSizeX);
    }

    public void specifyDirectionRandomly() {
        Random random = new Random();
        int enumLength = MotionDirection.values().length;
        int directionIndex = random.nextInt(enumLength);
        direction = MotionDirection.values()[directionIndex];
    }

    public void move(int windowWidth, int windowHeight) {
        if (imageView.getY() > 0 && direction == MotionDirection.UP)
            imageView.setY(imageView.getY() - 0.5);

        else if (imageView.getY() < windowHeight - characterSizeY && direction == MotionDirection.DOWN)
            imageView.setY(imageView.getY() + 0.5);

        else if (imageView.getX() > 0 && direction == MotionDirection.LEFT)
            imageView.setX(imageView.getX() - 0.5);

        else if (imageView.getX() < windowWidth - characterSizeX && direction == MotionDirection.RIGHT)
            imageView.setX(imageView.getX() + 0.5);

        else
            specifyDirectionRandomly();
    }

    public boolean shouldCheckAround(int windowWidth, double rectangleSize, double gapSize, ArrayList<InfoRect> rectArrayList) {
        if (Math.abs(imageView.getY() - lastLocationY) >= (rectangleSize + gapSize)) {
            lastLocationY = (int)imageView.getY();

            switch (direction) {
                case UP:
                    currentRectangleIndex -= (windowWidth / (rectangleSize + gapSize));
                    break;
                case DOWN:
                    currentRectangleIndex += (windowWidth / (rectangleSize + gapSize));
                    break;
            }
            checkDirectionsAvailable(windowWidth, rectArrayList);
            rectArrayList.get(currentRectangleIndex).rectangle.setFill(Color.RED);
            return true;
        }

        else if (Math.abs(imageView.getX() - lastLocationX) >= (rectangleSize + gapSize)){
            lastLocationX = (int)imageView.getX();
            switch (direction) {
                case RIGHT:
                    currentRectangleIndex++;
                    break;
                case LEFT:
                    currentRectangleIndex--;
                    break;
            }
            checkDirectionsAvailable(windowWidth, rectArrayList);
            rectArrayList.get(currentRectangleIndex).rectangle.setFill(Color.RED);
            return true;
        }
        return false;
    }

    public void checkDirectionsAvailable(int windowWidth, ArrayList<InfoRect> infoRectList)
    {
        /*
        * Left = -1 -2 -3
        * Right = 1 2 3
        * Up = -width -2width -3width
        * Down = width 2width 3width
        */

        switch (direction) {
            case UP:
                for (int i = 1; i <= 3; i++) {
                    if (!infoRectList.get(currentRectangleIndex - windowWidth / 10 * i).isPlayerMoved)
                        specifyDirectionRandomly();
                }
                break;

            case DOWN:
                for (int i = 1; i <= 3; i++) {
                    if (!infoRectList.get(currentRectangleIndex + windowWidth / 10 * i).isPlayerMoved)
                        specifyDirectionRandomly();
                }
                break;

            case LEFT:
                for (int i = 1; i <= 3; i++) {
                    if (!infoRectList.get(currentRectangleIndex - i).isPlayerMoved)
                        specifyDirectionRandomly();
                }
                break;

            case RIGHT:
                for (int i = 1; i <= 3; i++) {
                    if (!infoRectList.get(currentRectangleIndex + i).isPlayerMoved)
                        specifyDirectionRandomly();
                }
                break;
        }
    }
}