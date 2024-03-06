package com.example.prolab2_1;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

enum MotionDirection{
    RIGHT,
    LEFT,
    UP,
    DOWN
}

public class Character {
    int locationX;
    int locationY;
    int characterSizeX;
    int characterSizeY;
    InputStream imagePath;
    ImageView imageView;
    Image image;
    MotionDirection direction;
    // It is for intersecting just one time. but never updating second time
    boolean intersectionController = true;

    private TranslateTransition translateComponent = new TranslateTransition();

    public Character(String imagePath, int locationX, int locationY, int characterSizeX, int characterSizeY) throws FileNotFoundException {
        this.locationX = locationX;
        this.locationY = locationY;
        this.characterSizeX = characterSizeX;
        this.characterSizeY = characterSizeY;
        this.imagePath = new FileInputStream(imagePath);
        image = new Image(this.imagePath);
        imageView = new ImageView(image);
        imageView.setX(locationX * 10);
        imageView.setY(locationY * 10);
        imageView.setFitHeight(characterSizeY * 10);
        imageView.setFitWidth(characterSizeX * 10);
    }

    public MotionDirection specifyDirectionRandomly()
    {
        Random random = new Random();
        int enumLength = MotionDirection.values().length;
        int directionIndex = random.nextInt(enumLength);
        direction = MotionDirection.values()[directionIndex];

        return direction;
    }

    public void move()
    {
        translateComponent.setNode(imageView);
        translateComponent.setDuration(Duration.millis(10000));

        switch (specifyDirectionRandomly())
        {
            case UP:
                translateComponent.setByY(-1000);
                break;
            case DOWN:
                translateComponent.setByY(1000);
                break;
            case LEFT:
                translateComponent.setByX(-1000);
                break;
            case RIGHT:
                translateComponent.setByX(1000);
                break;
        }

        translateComponent.play();
    }

    public void clearSpeed()
    {
        translateComponent.stop();

        if (direction == MotionDirection.UP || direction == MotionDirection.DOWN)
        {
            translateComponent.setByY(0);
        }

        else
        {
            translateComponent.setByX(0);
        }
    }

    public void updateMovement(ArrayList<StaticObstacle> staticObstacles)
    {
        final ChangeListener<Number> checkIntersection = (ob,n,n1)-> {
            for (StaticObstacle obstacle : staticObstacles)
            {
                if (imageView.getBoundsInParent().intersects(obstacle.imageView.getBoundsInParent()) && intersectionController)
                {
                    intersectionController = false;
                    clearSpeed();
                    specifyDirectionRandomly();
                    move();
                }
            }
        };

        imageView.translateXProperty().addListener(checkIntersection);
        imageView.translateYProperty().addListener(checkIntersection);
    }
}