package com.example.prolab2_1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class HelloApplication extends Application {
    TextField textFieldHeight = new TextField();
    TextField textFieldWidth = new TextField();
    Group rootGame = new Group();
    Group rootMain = new Group();
    Random random = new Random();
    Character arthurMorgan = null;
    int characterSizeX = 1;
    int characterSizeY = 1;
    int windowHeight;
    int windowWidth;
    int rectangleAmountX;
    int rectangleAmountY;
    int rectangleTotal;
    boolean screen2Opened = false;
    boolean fogAdd = false;
    static double rectangleSize = 9.5;
    static double gapSize = .5;
    static double rectangleAndGapSize = rectangleSize + gapSize;
    public ArrayList<Node> rectangleArray = new ArrayList<>();
    public static ArrayList<Treasure> treasures = new ArrayList<>();
    public ArrayList<ArrayList<Node>> separatedArea = new ArrayList<>();


    @Override
    public void start(Stage stage) throws IOException {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
            try {
                try {
                    windowHeight = Integer.parseInt(textFieldHeight.getText());
                    windowWidth = Integer.parseInt(textFieldWidth.getText());
                }
                catch (NumberFormatException ety){
                }
                catch (Exception ety){
                }

                // X and Y Coordinates and Total Number of Rectangles
                rectangleAmountX = windowWidth / (int)rectangleAndGapSize;
                rectangleAmountY = windowHeight / (int)rectangleAndGapSize;
                rectangleTotal = rectangleAmountX * rectangleAmountY;


                // Create Ractangles
                for (double y = 0; y < rectangleAmountY; y++) {
                    for (double x = 0; x < rectangleAmountX; x++) {
                        Node rectangleInfo = new Node();
                        rectangleInfo.rectangle = new Rectangle(x * rectangleAndGapSize, y * rectangleAndGapSize, rectangleSize, rectangleSize);
                        rectangleInfo.setImageViewPosition((int)rectangleInfo.rectangle.getX(), (int)rectangleInfo.rectangle.getY());
                        rectangleInfo.column = (int)x;
                        rectangleInfo.row = (int)y;
                        if (x * rectangleAndGapSize > windowHeight / 2 - rectangleAndGapSize)
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
                int totalStaticObstacle = 8;
                int totalDynamicObstacle = 3;
                int totalTreasure = 4;
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
                ArrayList<Node> rectanglesInfo = new ArrayList<>();
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
                            rectanglesInfo.get(i).canBeUsedOnPathfinding = false;
                            rectanglesInfo.get(i).obstacleType = staticObstacles.get(k).getObstacleType();
                        }
                    }

                    startImageIndex = staticObstacles.get(k).sizeX + 3;
                    if (staticObstacles.get(k).getObstacleType() == TypeObstacles.ROCK ||  staticObstacles.get(k).getObstacleType() == TypeObstacles.WALL) {
                        for (int j = 1; j <= staticObstacles.get(k).sizeY; j++) {
                            for (int l = 0; l < staticObstacles.get(k).sizeX; l++) {
                                rectanglesInfo.get(l + startImageIndex).isPlayerMoved = false;
                                rectanglesInfo.get(l + startImageIndex).canBeUsedOnPathfinding = false;
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
                        rectanglesInfo.get(i).canBeUsedOnPathfinding = false;
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

                    for (int i = 0; i < rectanglesInfo.size(); i++) {
                        rectanglesInfo.get(i).isObstaclePlaced = false;
                        rectanglesInfo.get(i).isPlayerMoved = false;
                        rectanglesInfo.get(i).obstacleType = treasures.get(k).getTreasureType();
                        rectanglesInfo.get(i).obstacleType = treasures.get(k).getTreasureType();
                        rectanglesInfo.get(i).treasure = treasures.get(k);
                        treasures.get(k).nodes.add(rectanglesInfo.get(i));
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
                        arthurMorgan = new Character("pictures/mainCharacter.jpg", locationX, locationY, characterSizeX, characterSizeY, (int)rectangleAndGapSize, characterMaxStraightWay, characterMinStraightWay);
                        arthurMorgan.currentRectangleIndex = locationX + locationY * rectangleAmountX;
                        //arthurMorgan.setFrontDirection(arthurMorgan.getDirectionAutonomously(windowWidth,windowHeight, (int)rectangleAndGapSize, rectangleArray));
                        //arthurMorgan.setBackDirection(arthurMorgan.getBackDirection());
                        isCharacterCreated = true;
                    }
                }

                // With the tick method, the character is made to move continuously on the screen
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), ec -> {
                    try {
                        tick();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();

                Timeline pathFindingTimeLine = new Timeline(new KeyFrame(Duration.millis(4), eu -> {
                    pathFindingUpdate();
                }));

                pathFindingTimeLine.setCycleCount(Animation.INDEFINITE);
                pathFindingTimeLine.play();


                // Add Obstacles, Treasures, Rectangles and Character to Screen
                for (int i = 0; i < rectangleTotal; i++)
                    rootGame.getChildren().add(rectangleArray.get(i).rectangle);

                for (int i = 0; i < staticObstacles.size(); i++)
                    rootGame.getChildren().add(staticObstacles.get(i).imageView);

                for (int i = 0; i < dynamicObstacles.size(); i++)
                    rootGame.getChildren().add(dynamicObstacles.get(i).imageView);

                for (int i = 0; i < treasures.size(); i++)
                    rootGame.getChildren().add(treasures.get(i).imageView);

                rootGame.getChildren().add(arthurMorgan.imageView);


                // Game screen appears when the button on the main screen is pressed
                Stage stageGame = (Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow();
                Scene sceneGame = new Scene(rootGame,windowWidth,windowHeight);
                sceneGame.setFill(Color.BLACK);
                stageGame.setTitle("AUTONOMOUS GOLD HUNTER");
                stageGame.setScene(sceneGame);
                stageGame.show();

                // Will run when enter is pressed
                sceneGame.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.ENTER && !fogAdd) {
                            fogAdd = true;
                            try {
                                tick();
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                            screen2Opened = true;
                        }
                    }
                });
            }
            catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            }
        };


        textFieldHeight.setLayoutX(450);
        textFieldHeight.setLayoutY(750);
        textFieldWidth.setLayoutX(450);
        textFieldWidth.setLayoutY(800);
        textFieldHeight.setPromptText("HEIGHT");
        textFieldWidth.setPromptText("WIDTH");


        Image image = new Image("file:pictures/mainScreen.jpg");
        ImageView imageView = new ImageView(image);
        rootMain.getChildren().add(imageView);

        image = new Image("file:pictures/start.jpg");
        imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(200);

        Button button = new Button();
        button.setGraphic(imageView);
        button.setStyle("  -fx-border-style: none; -fx-border-width: 0; -fx-border-insets:0; -fx-font-size:5px");
        button.setLayoutX(420);
        button.setLayoutY(850);
        button.setOnAction(event);

        rootMain.getChildren().add(textFieldHeight);
        rootMain.getChildren().add(textFieldWidth);
        rootMain.getChildren().add(button);


        Scene scene = new Scene(rootMain, 1024, 1024);

        stage.setTitle("AUTONOMOUS GOLD HUNTER");
        stage.setScene(scene);
        stage.show();
    }

    public void tick() throws FileNotFoundException {
        if (screen2Opened && Character.canMove) {
            arthurMorgan.move(windowWidth, windowHeight, (int) rectangleAndGapSize, rectangleArray);
        }

        if (!screen2Opened && fogAdd) {
            for (int i = 0; i < rectangleArray.size(); i++) {
                rootGame.getChildren().add(rectangleArray.get(i).imageView);
            }
        }
    }

    public void pathFindingUpdate() {
        if (screen2Opened && !arthurMorgan.getIsMovingAutonomously()) {
            arthurMorgan.aStarPathFinding.search(rectangleArray, rectangleAmountX, rectangleAmountY);
        }
    }

    // Returns min screen size
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

/* TAMAM */