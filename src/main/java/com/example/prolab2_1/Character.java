package com.example.prolab2_1;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

enum MotionDirection{
    RIGHT,
    LEFT,
    UP,
    DOWN
}

public class Character {
    int locationX;
    int locationY;
    int lastLocationX;
    int lastLocationY;
    int characterSizeX;
    int characterSizeY;
    int currentRectangleIndex;
    int maxStraigthWay = 25;
    int currentStraightWay = 0;
    InputStream imagePath;
    ImageView imageView;
    Image image;
    MotionDirection direction;

    public Character(String imagePath, int locationX, int locationY, int characterSizeX, int characterSizeY, int rectangleAndGapSize) throws FileNotFoundException {
        this.locationX = locationX;
        this.locationY = locationY;
        this.lastLocationX = locationX * rectangleAndGapSize;
        this.lastLocationY = locationY * rectangleAndGapSize;
        this.characterSizeX = characterSizeX * rectangleAndGapSize;
        this.characterSizeY = characterSizeY * rectangleAndGapSize;
        this.imagePath = new FileInputStream(imagePath);
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setX(locationX * rectangleAndGapSize);
        imageView.setY(locationY * rectangleAndGapSize);
        imageView.setFitHeight(this.characterSizeY);
        imageView.setFitWidth(this.characterSizeX);
    }


    public void specifyDirectionRandomly() {
        Random random = new Random();
        int enumLength = MotionDirection.values().length;
        int directionIndex = random.nextInt(enumLength);
        MotionDirection lastDirection = direction;
        direction = MotionDirection.values()[directionIndex];
        currentStraightWay = 0;
        while (lastDirection != null &&
                (((lastDirection == MotionDirection.DOWN || lastDirection == MotionDirection.UP) && (direction == MotionDirection.DOWN || direction == MotionDirection.UP)) ||
                        ((lastDirection == MotionDirection.LEFT || lastDirection == MotionDirection.RIGHT) && (direction == MotionDirection.LEFT || direction == MotionDirection.RIGHT)))) {
            directionIndex = random.nextInt(enumLength);
            direction = MotionDirection.values()[directionIndex];
        }
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

    public boolean shouldCheckAround(int windowWidth, int windowHeight, int rectangleAndGapSize, ArrayList<RectangleInfo> rectangleInfo) {
        if (Math.abs(imageView.getY() - lastLocationY) >= rectangleAndGapSize) {
            lastLocationY = (int)imageView.getY();

            switch (direction) {
                case UP:
                    currentRectangleIndex -= (windowWidth / rectangleAndGapSize);
                    break;
                case DOWN:
                    currentRectangleIndex += (windowWidth / rectangleAndGapSize);
                    break;
            }

            rectangleInfo.get(currentRectangleIndex).rectangle.setFill(Color.RED);
            checkDirectionsAvailable(windowWidth, windowHeight, rectangleAndGapSize,rectangleInfo);

            currentStraightWay++;
            if (currentStraightWay >= maxStraigthWay)
                specifyDirectionRandomly();

            return true;
        }

        else if (Math.abs(imageView.getX() - lastLocationX) >= rectangleAndGapSize){
            lastLocationX = (int)imageView.getX();
            switch (direction) {
                case RIGHT:
                    currentRectangleIndex++;
                    break;
                case LEFT:
                    currentRectangleIndex--;
                    break;
            }

            checkDirectionsAvailable(windowWidth, windowHeight, rectangleAndGapSize,rectangleInfo);
            rectangleInfo.get(currentRectangleIndex).rectangle.setFill(Color.RED);

            currentStraightWay++;
            if (currentStraightWay >= maxStraigthWay)
                specifyDirectionRandomly();

            return true;
        }

        return false;
    }



    public void checkDirectionsAvailable(int windowWidth, int windowHeight, int rectangleAndGapSize, ArrayList<RectangleInfo> rectangleInfo)
    {
        switch (direction) {
            case UP:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex - windowWidth / rectangleAndGapSize * i) >= 0 &&
                            !rectangleInfo.get(currentRectangleIndex - windowWidth / rectangleAndGapSize * i).isPlayerMoved) {
                        specifyDirectionRandomly();
                        if (i == 3)
                            System.out.println(rectangleInfo.get(currentRectangleIndex - windowWidth / rectangleAndGapSize * i).obstacleType);
                    }
                }
                break;

            case DOWN:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex + windowWidth / rectangleAndGapSize * i) < (windowWidth / rectangleAndGapSize * windowHeight / rectangleAndGapSize) &&
                            !rectangleInfo.get(currentRectangleIndex + windowWidth / rectangleAndGapSize * i).isPlayerMoved) {
                        specifyDirectionRandomly();
                        if (i == 3)
                            System.out.println(rectangleInfo.get(currentRectangleIndex + windowWidth / rectangleAndGapSize * i).obstacleType);
                    }
                }
                break;

            case LEFT:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex - i > currentRectangleIndex - (currentRectangleIndex % (windowWidth / rectangleAndGapSize)) - 1)
                            && !rectangleInfo.get(currentRectangleIndex - i).isPlayerMoved) {
                        specifyDirectionRandomly();
                        if (i == 3)
                            System.out.println(rectangleInfo.get(currentRectangleIndex - i).obstacleType);
                    }
                }
                break;

            case RIGHT:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex + i < currentRectangleIndex + ((windowWidth / rectangleAndGapSize) - currentRectangleIndex % (windowWidth / rectangleAndGapSize))) &&
                            !rectangleInfo.get(currentRectangleIndex + i).isPlayerMoved) {
                        specifyDirectionRandomly();
                        if (i == 3)
                            System.out.println(rectangleInfo.get(currentRectangleIndex + i).obstacleType);
                    }
                }
                break;
        }
    }
}