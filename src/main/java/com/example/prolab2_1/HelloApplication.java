package com.example.prolab2_1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class RectangleInfo {
    Rectangle rectangle;
    boolean isObstaclePlaced = true;
    boolean isPlayerMoved = true;
    boolean isSeen = false;
    Enum obstacleType;
    Treasure treasure;
    InputStream imagePath;
    ImageView imageView;
    Image image;

    RectangleInfo() throws FileNotFoundException {
        imagePath = new FileInputStream("pictures/1.jpg");
        image = new Image(imagePath);
        imageView = new ImageView(image);
    }

    public void setImageViewPosition(int x, int y) {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(rectangle.getWidth());
        imageView.setFitHeight(rectangle.getHeight());
    }
}

public class HelloApplication extends Application {
    Random random = new Random();
    Character arthurMorgan = null;
    int characterSizeX = 1;
    int characterSizeY = 1;
    int windowHeight = 1000;
    int windowWidth = 1000;
    static double rectangleSize = 9.5;
    static double gapSize = 0.5;
    static double rectangleAndGapSize = rectangleSize + gapSize;
    ArrayList<RectangleInfo> rectangleArray = new ArrayList<>();
    public static ArrayList<Treasure> treasures = new ArrayList<>();
    public ArrayList<ArrayList<RectangleInfo>> separatedArea = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        /*System.out.println("Enter Window Height: ");
        Scanner scanner = new Scanner(System.in);
        windowHeight = scanner.nextInt();
        System.out.println("Enter Window Width: ");
        windowWidth = scanner.nextInt();*/

        // X and X Coordinates and Total Number of Rectangles
        int rectangleAmountX = windowWidth / (int)rectangleAndGapSize;
        int rectangleAmountY = windowHeight / (int)rectangleAndGapSize;
        int rectangleTotal = rectangleAmountX * rectangleAmountY;


        // Create Ractangles
        for (double y = 0; y < windowHeight; y += rectangleAndGapSize) {
            for (double x = 0; x < windowWidth; x += rectangleAndGapSize) {
                RectangleInfo rectangleInfo = new RectangleInfo();
                rectangleInfo.rectangle = new Rectangle(x, y, rectangleSize, rectangleSize);
                rectangleInfo.setImageViewPosition((int)rectangleInfo.rectangle.getX(), (int)rectangleInfo.rectangle.getY());
                if (x > windowHeight / 2 - rectangleAndGapSize)
                    rectangleInfo.rectangle.setFill(Color.WHITE);
                else
                    rectangleInfo.rectangle.setFill(Color.WHITE);
                rectangleArray.add(rectangleInfo);
            }
        }

        // Split the Screen Into Areas
        int minWindowSize = getMinimumRectangleAmount(rectangleAmountX, rectangleAmountY);
        int separatedAreaSize = 0;

        if (minWindowSize <= 100) {
            separatedAreaSize = minWindowSize / 5;
        }
        else {
            separatedAreaSize = minWindowSize / 10;
        }

        int speratedAreaX = rectangleAmountX / separatedAreaSize;
        int speratedAreaY = rectangleAmountY / separatedAreaSize;
        int currentY;
        int currentX;
        int initialY = 0;
        int initialX = 0;
        int rectangleAreaY = rectangleAmountY / speratedAreaY;
        int rectangleAreaX = rectangleAmountX / speratedAreaX;

        for (int i = 0; i < speratedAreaY * speratedAreaX; i++) {
            separatedArea.add(new ArrayList<>());
        }

        for (int i = 0; i < separatedArea.size(); i++) {
            for (currentY = initialY; currentY < rectangleAreaY; currentY++) {
                for (currentX = initialX; currentX < rectangleAreaX; currentX++) {
                    separatedArea.get(i).add(rectangleArray.get(currentX + currentY * rectangleAmountX));
                    //rectangleArray.get(currentX + currentY * rectangleAmountX).rectangle.setFill(Color.BLUE);
                }
            }

            if ((i + 1) % speratedAreaX == 0) {
                initialY += separatedAreaSize;
                rectangleAreaY += separatedAreaSize;
                initialX = 0;
                rectangleAreaX = separatedAreaSize;
            }
            else {
                initialX += separatedAreaSize;
                rectangleAreaX += separatedAreaSize;
            }

        }

        // Add Enums to ArrayList
        ArrayList<TypeObstacles> typeObstacle = new ArrayList<>();
        typeObstacle.addAll(Arrays.asList(TypeObstacles.values()));

        ArrayList<DynamicObstacles> dynamicObstacle = new ArrayList<>();
        dynamicObstacle.addAll(Arrays.asList(DynamicObstacles.values()));

        // Add Generator and ArrayList for Obstacles and Treasures
        ArrayList<StaticObstacle> staticObstacles = new ArrayList<>();
        ArrayList<DynamicObstacle> dynamicObstacles = new ArrayList<>();


        TreasureGenerator treasureGenerator = new TreasureGenerator();
        ObstacleGenerator obstacleGenerator = new ObstacleGenerator();

        // Create Obstacles and Treasures
        int totalStaticObstacle = 20;
        int totalDynamicObstacle = 3;
        int totalTreasure = 5;
        int randomSeason;
        int randomObstacle;
        int staticObstaclesSize;

        // Create Default Static Obstacles
        obstacleGenerator.createDefaultObstacles(staticObstacles);
        staticObstaclesSize = staticObstacles.size();

        // Create Static Obstacles
        for (int i = 0; i < totalStaticObstacle - staticObstaclesSize; i++) {
            randomSeason = random.nextInt(2);
            switch (randomSeason){
                case 0:
                    randomObstacle = random.nextInt(typeObstacle.size());
                    staticObstacles.add(obstacleGenerator.generateWinterObstacle(typeObstacle.get(randomObstacle)));
                    break;
                case 1:
                    randomObstacle = random.nextInt(typeObstacle.size());
                    staticObstacles.add(obstacleGenerator.generateSummerObstacle(typeObstacle.get(randomObstacle)));
                    break;
            }
        }

        // Create Dynamic Obstacles
        for (int i = 0; i < totalDynamicObstacle; i++){
            randomObstacle = random.nextInt(dynamicObstacle.size());
            dynamicObstacles.add(obstacleGenerator.generateDynamicObstacle(dynamicObstacle.get(randomObstacle)));
        }

        // Create Treasures
        for (int i = 0; i < totalTreasure; i++){
            treasures.add(treasureGenerator.goldChest());
            treasures.add(treasureGenerator.silverChest());
            treasures.add(treasureGenerator.emeraldChest());
            treasures.add(treasureGenerator.copperChest());
        }


        // Set Coordinates of Treasures and Obstacles
        ArrayList<RectangleInfo> rectanglesInfo = new ArrayList<>();
        int imageRandomX;
        int imageRandomY;
        int imageRectangleIndex;
        int startImageIndex;
        int obstacleBorderSpace;
        int imageBorderSpace = 0;

        // Set Coordinates of Static Obstacles
        for (int k = 0; k < staticObstacles.size(); k++) {
            search:
            for (int m = 0; m < 1; m++) {
                imageRandomY = random.nextInt(rectangleAmountY - 1 - staticObstacles.get(k).sizeY);

                if (staticObstacles.get(k).season == Season.SUMMER)
                    imageRandomX = random.nextInt((rectangleAmountX - 1) / 2 - staticObstacles.get(k).sizeX) + rectangleAmountX / 2;
                else
                    imageRandomX = random.nextInt((rectangleAmountX - 1) / 2 - staticObstacles.get(k).sizeX);

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
                        imageRectangleIndex = (imageRandomX + x) + ((imageRandomY + y) * rectangleAmountX);

                        if (rectangleArray.get(imageRectangleIndex).isObstaclePlaced) {
                            rectanglesInfo.add(rectangleArray.get(imageRectangleIndex));
                        }
                        else {
                            m--;
                            rectanglesInfo.clear();
                            continue search;
                        }
                    }
                }
            }

            for (int i = 0; i < rectanglesInfo.size(); i++) {
                rectanglesInfo.get(i).isObstaclePlaced = false;
                if (staticObstacles.get(k).getObstacleType() == TypeObstacles.TREE ||  staticObstacles.get(k).getObstacleType() == TypeObstacles.MOUNTAIN) {
                    rectanglesInfo.get(i).isPlayerMoved = false;
                    rectanglesInfo.get(i).obstacleType = staticObstacles.get(k).getObstacleType();
                }
            }

            startImageIndex = staticObstacles.get(k).sizeX + 3;
            if (staticObstacles.get(k).getObstacleType() == TypeObstacles.ROCK ||  staticObstacles.get(k).getObstacleType() == TypeObstacles.WALL) {
                for (int j = 1; j <= staticObstacles.get(k).sizeY; j++) {
                    for (int l = 0; l < staticObstacles.get(k).sizeX; l++) {
                        rectanglesInfo.get(l + startImageIndex).isPlayerMoved = false;
                        rectanglesInfo.get(l + startImageIndex).obstacleType = staticObstacles.get(k).getObstacleType();
                    }
                    startImageIndex += staticObstacles.get(k).sizeX + 2;
                }
            }


            staticObstacles.get(k).imageView.setX(rectanglesInfo.get(staticObstacles.get(k).sizeX + imageBorderSpace).rectangle.getX());
            staticObstacles.get(k).imageView.setY(rectanglesInfo.get(staticObstacles.get(k).sizeX + imageBorderSpace - 1).rectangle.getY());
            rectanglesInfo.clear();
        }

        // Set Coordinates of Dynamic Obstacles
        for (int k = 0; k < dynamicObstacles.size(); k++) {
            search:
            for (int m = 0; m < 1; m++) {
                imageRandomY = random.nextInt(rectangleAmountY - 1 - dynamicObstacles.get(k).visitFieldY);
                imageRandomX = random.nextInt(rectangleAmountX - 1 - dynamicObstacles.get(k).visitFieldX);

                for (int y = 0; y < dynamicObstacles.get(k).visitFieldY; y++) {
                    for (int x = 0; x < dynamicObstacles.get(k).visitFieldX; x++) {
                        imageRectangleIndex = (imageRandomX + x) + ((imageRandomY + y) * rectangleAmountX);
                        if (rectangleArray.get(imageRectangleIndex).isObstaclePlaced) {
                            rectanglesInfo.add(rectangleArray.get(imageRectangleIndex));
                        }
                        else {
                            m--;
                            rectanglesInfo.clear();
                            continue search;
                        }
                    }
                }
            }

            for (int i = 0; i < rectanglesInfo.size(); i++) {
                rectanglesInfo.get(i).rectangle.setFill(Color.LIGHTPINK);
                rectanglesInfo.get(i).isObstaclePlaced = false;
                rectanglesInfo.get(i).isPlayerMoved = false;
                rectanglesInfo.get(i).obstacleType = dynamicObstacles.get(k).species;
            }

            dynamicObstacles.get(k).imageView.setX(rectanglesInfo.get(0).rectangle.getX());
            dynamicObstacles.get(k).imageView.setY(rectanglesInfo.get(0).rectangle.getY());
            rectanglesInfo.clear();
        }

        // Set Coordinates of Treasures
        for (int k = 0; k < treasures.size(); k++) {
            search:
            for (int m = 0; m < 1; m++) {
                imageRandomY = random.nextInt(rectangleAmountY - treasures.get(k).sizeY);
                imageRandomX = random.nextInt(rectangleAmountX - treasures.get(k).sizeX);

                for (int y = 0; y < treasures.get(k).sizeY; y++) {
                    for (int x = 0; x < treasures.get(k).sizeX; x++) {
                        imageRectangleIndex = (imageRandomX + x) + ((imageRandomY + y) * rectangleAmountX);
                        if (rectangleArray.get(imageRectangleIndex).isObstaclePlaced) {
                            rectanglesInfo.add(rectangleArray.get(imageRectangleIndex));
                        }
                        else {
                            m--;
                            rectanglesInfo.clear();
                            continue search;
                        }
                    }
                }
            }

            for (int i = 0; i < rectanglesInfo.size(); i++)
            {
                rectanglesInfo.get(i).isObstaclePlaced = false;
                rectanglesInfo.get(i).isPlayerMoved = false;
                rectanglesInfo.get(i).obstacleType = treasures.get(k).getTreasureType();
                rectanglesInfo.get(i).treasure = treasures.get(k);
            }

            treasures.get(k).imageView.setX(rectanglesInfo.get(0).rectangle.getX());
            treasures.get(k).imageView.setY(rectanglesInfo.get(0).rectangle.getY());
            rectanglesInfo.clear();
        }

        // Create Character
        boolean isCharacterCreated = false;
        int locationX;
        int locationY;

        int characterMaxStraightWay = getMinimumRectangleAmount(rectangleAmountX, rectangleAmountY) / 4;
        int characterMinStraightWay = getMinimumRectangleAmount(rectangleAmountX, rectangleAmountY) / 6;
        while (!isCharacterCreated) {
            locationX = random.nextInt(rectangleAmountX - characterSizeX);
            locationY = random.nextInt(rectangleAmountY - characterSizeY);

            if (rectangleArray.get(locationX + locationY  * rectangleAmountX).isObstaclePlaced){
                arthurMorgan = new Character("pictures/bee.png", locationX, locationY, characterSizeX, characterSizeY, (int)rectangleAndGapSize, characterMaxStraightWay, characterMinStraightWay);
                arthurMorgan.currentRectangleIndex = locationX + locationY * rectangleAmountX;
                arthurMorgan.setFrontDirection(arthurMorgan.getDirection(windowWidth,windowHeight, (int)rectangleAndGapSize, rectangleArray));
                arthurMorgan.setBackDirection(arthurMorgan.getBackDirection());
                isCharacterCreated = true;
            }
        }

        // With the tick method, the character is made to move continuously on the screen
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            try {
                tick();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        // Add Obstacles, Treasures, Rectangles and Character to Screen
        Group myGroup = new Group();

        for (int i = 0; i < rectangleTotal; i++)
        {
            myGroup.getChildren().add(rectangleArray.get(i).rectangle);
        }

        for (int i = 0; i < staticObstacles.size(); i++)
            myGroup.getChildren().add(staticObstacles.get(i).imageView);

        for (int i = 0; i < dynamicObstacles.size(); i++)
            myGroup.getChildren().add(dynamicObstacles.get(i).imageView);

        for (int i = 0; i < treasures.size(); i++)
            myGroup.getChildren().add(treasures.get(i).imageView);

        for (int i = 0; i < rectangleArray.size(); i++)
            myGroup.getChildren().add(rectangleArray.get(i).imageView);

        myGroup.getChildren().add(arthurMorgan.imageView);


        Scene scene = new Scene(myGroup,windowWidth,windowHeight);
        scene.setFill(Color.BLACK);
        stage.setTitle("AUTONOMOUS GOLD HUNTER");
        stage.setScene(scene);
        stage.show();
    }

    public void tick() throws FileNotFoundException {
        arthurMorgan.move(windowWidth,windowHeight, (int)rectangleAndGapSize, rectangleArray);
    }

    int getMinimumRectangleAmount(int rectangleAmountX, int rectangleAmountY)
    {
        if (rectangleAmountX < rectangleAmountY) {
            return rectangleAmountX;
        }
        return rectangleAmountY;
    }
    public static void main(String[] args) {
        launch();
    }
}