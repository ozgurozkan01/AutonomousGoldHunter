package com.example.prolab2_1;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class InfoRect {
    Rectangle rectangle;
    boolean isObstacleReplaced = true;
    boolean isPlayerMoved = true;
    boolean isSeen = false;
}

public class HelloApplication extends Application {
    ArrayList<InfoRect> rectangleArray = new ArrayList<>();
    Random random = new Random();
    Character arthurMorgan = null;
    int windowHeight = 1000;
    int windowWidth = 1000;
    double rectangleSize = 9.5;
    double gapSize = 0.5;
    int characterSizeX = 1;
    int characterSizeY = 1;

    @Override
    public void start(Stage stage) throws IOException {

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

        int totalStaticObstacle = 30;
        int totalDynamicObstacle = 10;
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
        int startImageIndex;
        int obstacleBorderSpace;
        int imageBorderSpace = 0;

        // Set Coordinates of Static Obstacles
        for (int k = 0; k < staticObstacles.size(); k++) {
            search:
            for (int m = 0; m < 1; m++) {
                imageY = random.nextInt(99 - staticObstacles.get(k).sizeY);
                if (staticObstacles.get(k).getSeason() == "summer")
                    imageX = random.nextInt(49 - staticObstacles.get(k).sizeX) + 50;
                else
                    imageX = random.nextInt(49 - staticObstacles.get(k).sizeX);

                
                if(staticObstacles.get(k).getObstacleType() == TypeObstacles.MOUNTAIN ||
                        staticObstacles.get(k).getObstacleType() ==TypeObstacles.TREE) {
                    obstacleBorderSpace = 0;
                    imageBorderSpace = 0;
                }
                else {
                    obstacleBorderSpace = 2;
                    imageBorderSpace = 3;
                }


                for (int y = 0; y < staticObstacles.get(k).sizeY + obstacleBorderSpace; y++) {
                    for (int x = 0; x < staticObstacles.get(k).sizeX + obstacleBorderSpace; x++) {
                        index = (imageX + x) + ((imageY + y) * 100);
                        if (rectangleArray.get(index).isObstacleReplaced) {
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
                infoRects.get(i).isObstacleReplaced = false;
                if (staticObstacles.get(k).getObstacleType() == TypeObstacles.TREE ||  staticObstacles.get(k).getObstacleType() == TypeObstacles.MOUNTAIN) {
                    infoRects.get(i).isPlayerMoved = false;
                }
            }

            startImageIndex = staticObstacles.get(k).sizeX + 3;
            if (staticObstacles.get(k).getObstacleType() == TypeObstacles.ROCK ||  staticObstacles.get(k).getObstacleType() == TypeObstacles.WALL) {
                for (int j = 1; j <= staticObstacles.get(k).sizeY; j++) {
                    for (int l = 0; l < staticObstacles.get(k).sizeX; l++) {
                        infoRects.get(l + startImageIndex).isPlayerMoved = false;
                    }
                    startImageIndex += staticObstacles.get(k).sizeX + 2;
                }
            }

            staticObstacles.get(k).imageView.setX(infoRects.get(staticObstacles.get(k).sizeX + imageBorderSpace).rectangle.getX());
            staticObstacles.get(k).imageView.setY(infoRects.get(staticObstacles.get(k).sizeX + imageBorderSpace - 1).rectangle.getY());
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
                        if (rectangleArray.get(index).isObstacleReplaced) {
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
                infoRects.get(i).isObstacleReplaced = false;
                infoRects.get(i).isPlayerMoved = false;
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
                        if (rectangleArray.get(index).isObstacleReplaced) {
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
                infoRects.get(i).isObstacleReplaced = false;

            treasures.get(k).imageView.setX(infoRects.get(0).rectangle.getX());
            treasures.get(k).imageView.setY(infoRects.get(0).rectangle.getY());
            infoRects.clear();
        }

        // Create Character Object
        boolean isCharacterCreated = false;
        int characterX;
        int characterY;

        while (!isCharacterCreated) {
            characterX = random.nextInt(99 - characterSizeX);
            characterY = random.nextInt(99 - characterSizeY);

            if (rectangleArray.get(characterX + characterY * 100).isObstacleReplaced){
                arthurMorgan = new Character("pictures/bee.png", characterX, characterY, characterSizeX, characterSizeY);
                arthurMorgan.currentRectangleIndex = characterX + characterY * 100;
                isCharacterCreated = true;
            }
        }

        arthurMorgan.specifyDirectionRandomly();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

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
        stage.setTitle("AUTONOMOUS GOLDEN HUNTER");
        stage.setScene(scene);
        stage.show();
    }

    public void tick(){
        arthurMorgan.move(windowWidth,windowHeight);
        arthurMorgan.shouldCheckAround(windowWidth, rectangleSize, gapSize, rectangleArray);
    }

    public static void main(String[] args) {

        launch();
    }
}