package com.example.prolab2_1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class InfoRect {
    Rectangle rectangle;
    boolean isAvailable = true;
}

public class HelloApplication extends Application {
    ArrayList<InfoRect> rectangleArray = new ArrayList<>();
    Random random = new Random();

    @Override
    public void start(Stage stage) throws IOException {
        int windowHeight = 1000;
        int windowWidth = 1000;
        double rectangleSize = 9.5;
        double gapSize = 0.5;
        int characterSizeX = 2;
        int characterSizeY = 2;


        /*System.out.println("Enter Window Height: ");
        Scanner scanner = new Scanner(System.in);
        windowHeight = scanner.nextInt();
        System.out.println("Enter Window Width: ");
        windowWidth = scanner.nextInt();*/

        double rectAmountOnX = windowHeight / (rectangleSize + gapSize);
        double rectAmountOnY = windowWidth / (rectangleSize + gapSize);
        double rectTotal = rectAmountOnX * rectAmountOnY;


        // Creating Ractangles
        for (double y = 0; y < windowHeight; y+=rectangleSize + gapSize) {
            for (double x = 0; x < windowWidth; x+=rectangleSize + gapSize) {
                InfoRect infoRect = new InfoRect();
                infoRect.rectangle = new Rectangle(x, y, rectangleSize, rectangleSize);
                if (x > 490)
                    infoRect.rectangle.setFill(Color.LIGHTGREEN);
                else
                    infoRect.rectangle.setFill(Color.WHITE);
                rectangleArray.add(infoRect);
            }
        }


        // Create Obstacles and Treasures
        ArrayList<TypeObstacles> typeObstacle = new ArrayList<>();
        typeObstacle.addAll(Arrays.asList(TypeObstacles.values()));

        ArrayList<DynamicObstacles> dynamicObstacle = new ArrayList<>();
        dynamicObstacle.addAll(Arrays.asList(DynamicObstacles.values()));

        ArrayList<StaticObstacle> staticObstacles = new ArrayList<>();
        ArrayList<DynamicObstacle> dynamicObstacles = new ArrayList<>();
        ArrayList<Treasure> treasures = new ArrayList<>();
        TreasureGenerator treasureGenerator = new TreasureGenerator();
        ObstacleGenerator obstacleGenerator = new ObstacleGenerator();

        int totalStaticObstacle = 20;
        int totalDynamicObstacle = 3;
        int totalTreasure = 5;
        int randomObstacleIndex;
        int randomSeason;
        int staticObstacleSize;

        obstacleGenerator.createDefaultObstacles(staticObstacles);
        staticObstacleSize = staticObstacles.size();

        for (int i = 0; i < totalStaticObstacle - staticObstacleSize; i++) {
            randomSeason = random.nextInt(2);
            switch (randomSeason){
                case 0:
                    randomObstacleIndex = random.nextInt(typeObstacle.size());
                    staticObstacles.add(obstacleGenerator.generateWinterObstacle(typeObstacle.get(randomObstacleIndex)));
                    break;
                case 1:
                    randomObstacleIndex = random.nextInt(typeObstacle.size());
                    staticObstacles.add(obstacleGenerator.generateSummerObstacle(typeObstacle.get(randomObstacleIndex)));
                    break;
            }
        }

        for (int i = 0; i < totalDynamicObstacle; i++){
            randomObstacleIndex = random.nextInt(dynamicObstacle.size());
            dynamicObstacles.add(obstacleGenerator.generateDynamicObstacle(dynamicObstacle.get(randomObstacleIndex)));
        }

        for (int i = 0; i < totalTreasure; i++){
            treasures.add(treasureGenerator.goldChest());
            treasures.add(treasureGenerator.silverChest());
            treasures.add(treasureGenerator.emeraldChest());
            treasures.add(treasureGenerator.copperChest());
        }

        // Set Coordinates of Treasures and Obstacles
        ArrayList<InfoRect> infoRects = new ArrayList<>();
        int imageX;
        int imageY;
        int index;

        // Set Coordinates of Static Obstacles
        for (int k = 0; k < staticObstacles.size(); k++) {
            search:
            for (int m = 0; m < 1; m++) {
                imageY = random.nextInt(99 - staticObstacles.get(k).sizeY);
                if (staticObstacles.get(k).getSeason() == "summer")
                    imageX = random.nextInt(49 - staticObstacles.get(k).sizeX) + 50;
                else
                    imageX = random.nextInt(49 - staticObstacles.get(k).sizeX);
                for (int y = 0; y < staticObstacles.get(k).sizeY + 2; y++) {
                    for (int x = 0; x < staticObstacles.get(k).sizeX + 2; x++) {
                        index = (imageX + x) + ((imageY + y) * 100);
                        if (rectangleArray.get(index).isAvailable) {
                            infoRects.add(rectangleArray.get(index));
                        }
                        else {
                            m--;
                            infoRects.clear();
                            continue search;
                        }
                    }
                }
            }

            for (int i = 0; i < infoRects.size(); i++)
                infoRects.get(i).isAvailable = false;

            staticObstacles.get(k).imageView.setX(infoRects.get(staticObstacles.get(k).sizeX + 3).rectangle.getX());
            staticObstacles.get(k).imageView.setY(infoRects.get(staticObstacles.get(k).sizeX + 3).rectangle.getY());
            infoRects.clear();
        }

        // Set Coordinates of Dynamic Obstacles
        for (int k = 0; k < dynamicObstacles.size(); k++) {
            search:
            for (int m = 0; m < 1; m++) {
                imageY = random.nextInt(99 - dynamicObstacles.get(k).sizeY);
                imageX = random.nextInt(99 - dynamicObstacles.get(k).sizeX);

                for (int y = 0; y < dynamicObstacles.get(k).sizeY; y++) {
                    for (int x = 0; x < dynamicObstacles.get(k).sizeX; x++) {
                        index = (imageX + x) + ((imageY + y) * 100);
                        if (rectangleArray.get(index).isAvailable) {
                            infoRects.add(rectangleArray.get(index));
                        }
                        else {
                            m--;
                            infoRects.clear();
                            continue search;
                        }
                    }
                }
            }

            for (int i = 0; i < infoRects.size(); i++) {
                infoRects.get(i).rectangle.setFill(Color.LIGHTPINK);
                infoRects.get(i).isAvailable = false;
            }

            dynamicObstacles.get(k).imageView.setX(infoRects.get(0).rectangle.getX());
            dynamicObstacles.get(k).imageView.setY(infoRects.get(0).rectangle.getY());
            infoRects.clear();
        }

        // Set Coordinates of Treasures
        for (int k = 0; k < treasures.size(); k++) {
            search:
            for (int m = 0; m < 1; m++) {
                imageY = random.nextInt(99 - treasures.get(k).sizeY);
                imageX = random.nextInt(99 - treasures.get(k).sizeX);

                for (int y = 0; y < treasures.get(k).sizeY; y++) {
                    for (int x = 0; x < treasures.get(k).sizeX; x++) {
                        index = (imageX + x) + ((imageY + y) * 100);
                        if (rectangleArray.get(index).isAvailable) {
                            infoRects.add(rectangleArray.get(index));
                        }
                        else {
                            m--;
                            infoRects.clear();
                            continue search;
                        }
                    }
                }
            }

            for (int i = 0; i < infoRects.size(); i++)
                infoRects.get(i).isAvailable = false;

            treasures.get(k).imageView.setX(infoRects.get(0).rectangle.getX());
            treasures.get(k).imageView.setY(infoRects.get(0).rectangle.getY());
            infoRects.clear();
        }

        // Create Character Object
        Character arthurMorgan = null;
        boolean isCharacterCreated = false;
        int characterX;
        int characterY;

        while (!isCharacterCreated) {
            characterX = random.nextInt(99 - characterSizeX);
            characterY = random.nextInt(99 - characterSizeY);

            if (rectangleArray.get(characterX + characterY * 100).isAvailable){
                arthurMorgan = new Character("pictures/bee.png", characterX, characterY, characterSizeX, characterSizeY);
                isCharacterCreated = true;
            }
        }

        arthurMorgan.move();
        arthurMorgan.updateMovement(staticObstacles);

        // Add Obstacles, Treasures, Rectangles and Character to Screen
        Group myGroup = new Group();

        for (int i = 0; i < rectTotal; i++)
            myGroup.getChildren().add(rectangleArray.get(i).rectangle);

        for (int i = 0; i < staticObstacles.size(); i++)
            myGroup.getChildren().add(staticObstacles.get(i).imageView);

        for (int i = 0; i < dynamicObstacles.size(); i++)
            myGroup.getChildren().add(dynamicObstacles.get(i).imageView);

        for (int i = 0; i < treasures.size(); i++)
            myGroup.getChildren().add(treasures.get(i).imageView);

        myGroup.getChildren().add(arthurMorgan.imageView);


        Scene scene = new Scene(myGroup,windowWidth,windowHeight);
        scene.setFill(Color.BLACK);
        stage.setTitle("GOLDEN HUNTER");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}