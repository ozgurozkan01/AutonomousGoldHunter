package com.example.prolab2_1;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
    int maxStraigthWay;
    int minStraigthWay;
    int randomStraigthWay;
    int currentStraightWay = 0;
    final int viewField = 7;
    final int viewDirection = 3;
    InputStream imagePath;
    static ImageView imageView;
    Image image;
    private MotionDirection frontDirection;
    private MotionDirection backDirection;
    private TreasureType targetTreasure = TreasureType.GOLD;
    private ArrayList<MotionDirection> emptyDirections = new ArrayList<>();
    private ArrayList<MotionDirection> lastFourDirections = new ArrayList<>();
    private ArrayList<TreasureType> treasuresType = new ArrayList<>();
    private ArrayList<Treasure> treasures = new ArrayList<>();


    public Character(String imagePath, int locationX, int locationY, int characterSizeX, int characterSizeY, int rectangleAndGapSize, int maxStraigthWay, int minStraigthWay) throws FileNotFoundException {
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
        this.maxStraigthWay = maxStraigthWay;
        this.minStraigthWay = minStraigthWay;
        treasuresType.addAll(Arrays.asList(TreasureType.values()));
    }


    MotionDirection getDirectionAutonomously(int windowWidth, int windowHeight, int rectangleAndGapSize, ArrayList<Node> rectanglesInfo) {
        emptyDirections.clear();
        boolean isIteratedPath = false;
        if (lastFourDirections.size() == 4){
            isIteratedPath = true;
            search :
            for (int i = 0; i < lastFourDirections.size() -1; i++)
            {
                for (int j = i+1; j < lastFourDirections.size(); j++)
                {
                    if (lastFourDirections.get(i) == lastFourDirections.get(j)){
                        isIteratedPath = false;
                        lastFourDirections.removeFirst();
                        break search;
                    }
                }
            }
        }

        if ((currentRectangleIndex - windowWidth / rectangleAndGapSize) >= 0 &&
                rectanglesInfo.get(currentRectangleIndex - windowWidth / rectangleAndGapSize).isPlayerMoved && backDirection != MotionDirection.UP) {
            emptyDirections.add(MotionDirection.UP);
        }
        if ((currentRectangleIndex + windowWidth / rectangleAndGapSize) < (windowWidth / rectangleAndGapSize * windowHeight / rectangleAndGapSize) &&
                rectanglesInfo.get(currentRectangleIndex + windowWidth / rectangleAndGapSize).isPlayerMoved && backDirection != MotionDirection.DOWN) {
            emptyDirections.add(MotionDirection.DOWN);
        }
        if ((currentRectangleIndex - 1 > currentRectangleIndex - (currentRectangleIndex % (windowWidth / rectangleAndGapSize)) - 1) &&
                rectanglesInfo.get(currentRectangleIndex - 1).isPlayerMoved && backDirection != MotionDirection.LEFT) {
            emptyDirections.add(MotionDirection.LEFT);
        }
        if ((currentRectangleIndex + 1 < currentRectangleIndex + ((windowWidth / rectangleAndGapSize) - currentRectangleIndex % (windowWidth / rectangleAndGapSize))) &&
                rectanglesInfo.get(currentRectangleIndex + 1).isPlayerMoved && backDirection != MotionDirection.RIGHT) {
            emptyDirections.add(MotionDirection.RIGHT);
        }

        if (emptyDirections.size() == 1 && emptyDirections.getFirst() == frontDirection) {
            currentStraightWay = 0;
            return frontDirection;
        }
        else {
            if (isIteratedPath) {
                emptyDirections.remove(lastFourDirections.getFirst());
                lastFourDirections.removeFirst();
            }
            emptyDirections.remove(frontDirection);
        }

        if (emptyDirections.isEmpty()) {
            currentStraightWay = 0;
            return backDirection;
        }

        currentStraightWay = 0;
        Random random = new Random();
        randomStraigthWay = random.nextInt(minStraigthWay) + (maxStraigthWay - minStraigthWay);
        int randomDirectionIndex = random.nextInt(emptyDirections.size());
        MotionDirection newDirection = emptyDirections.get(randomDirectionIndex);
        lastFourDirections.add(newDirection);

        return newDirection;
    }


    public void move(int windowWidth, int windowHeight, int rectangleAndGapSize, ArrayList<Node> rectanglesInfo) throws FileNotFoundException {
        if (imageView.getY() > 0 && frontDirection == MotionDirection.UP)
            imageView.setY(imageView.getY() - 0.5);

        else if (imageView.getY() < windowHeight - characterSizeY && frontDirection == MotionDirection.DOWN)
            imageView.setY(imageView.getY() + 0.5);

        else if (imageView.getX() > 0 && frontDirection == MotionDirection.LEFT)
            imageView.setX(imageView.getX() - 0.5);

        else if (imageView.getX() < windowWidth - characterSizeX && frontDirection == MotionDirection.RIGHT)
            imageView.setX(imageView.getX() + 0.5);

        else {
            frontDirection = getDirectionAutonomously(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
            backDirection = getBackDirection();
        }

        if (Math.abs(imageView.getY() - lastLocationY) >= rectangleAndGapSize) {
            lastLocationY = (int)imageView.getY();
            switch (frontDirection) {
                case UP:
                    currentRectangleIndex -= (windowWidth / rectangleAndGapSize);
                    break;
                case DOWN:
                    currentRectangleIndex += (windowWidth / rectangleAndGapSize);
                    break;
            }

            rectanglesInfo.get(currentRectangleIndex).rectangle.setFill(Color.RED);
            checkAround(windowWidth, windowHeight,rectangleAndGapSize,rectanglesInfo);
            checkMotionDirection(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
            currentStraightWay++;
            if (currentStraightWay >= randomStraigthWay) {
                frontDirection = getDirectionAutonomously(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
                backDirection = getBackDirection();
            }
        }

        else if (Math.abs(imageView.getX() - lastLocationX) >= rectangleAndGapSize){
            lastLocationX = (int)imageView.getX();
            switch (frontDirection) {
                case RIGHT:
                    currentRectangleIndex++;
                    break;
                case LEFT:
                    currentRectangleIndex--;
                    break;
            }

            rectanglesInfo.get(currentRectangleIndex).rectangle.setFill(Color.RED);
            checkAround(windowWidth, windowHeight, rectangleAndGapSize,rectanglesInfo);
            checkMotionDirection(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
            currentStraightWay++;
            if (currentStraightWay >= randomStraigthWay) {
                frontDirection = getDirectionAutonomously(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
                backDirection = getBackDirection();
            }
        }
    }


    public void checkMotionDirection(int windowWidth, int windowHeight, int rectangleAndGapSize, ArrayList<Node> rectanglesInfo) {
        switch (frontDirection) {
            case UP:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex - windowWidth / rectangleAndGapSize * i) >= 0 &&
                            !rectanglesInfo.get(currentRectangleIndex - windowWidth / rectangleAndGapSize * i).isPlayerMoved) {
                        frontDirection = getDirectionAutonomously(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
                        backDirection = getBackDirection();
                    }
                }
                break;

            case DOWN:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex + windowWidth / rectangleAndGapSize * i) < (windowWidth / rectangleAndGapSize * windowHeight / rectangleAndGapSize) &&
                            !rectanglesInfo.get(currentRectangleIndex + windowWidth / rectangleAndGapSize * i).isPlayerMoved) {
                        frontDirection = getDirectionAutonomously(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
                        backDirection = getBackDirection();
                    }
                }
                break;

            case LEFT:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex - i > currentRectangleIndex - (currentRectangleIndex % (windowWidth / rectangleAndGapSize)) - 1)
                            && !rectanglesInfo.get(currentRectangleIndex - i).isPlayerMoved) {
                        frontDirection = getDirectionAutonomously(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
                        backDirection = getBackDirection();
                    }
                }
                break;

            case RIGHT:
                for (int i = 1; i <= 3; i++) {
                    if ((currentRectangleIndex + i < currentRectangleIndex + ((windowWidth / rectangleAndGapSize) - currentRectangleIndex % (windowWidth / rectangleAndGapSize))) &&
                            !rectanglesInfo.get(currentRectangleIndex + i).isPlayerMoved) {
                        frontDirection = getDirectionAutonomously(windowWidth, windowHeight, rectangleAndGapSize, rectanglesInfo);
                        backDirection = getBackDirection();
                    }
                }
                break;
        }
    }


    public void checkAround(int windowWidth, int windowHeight, int rectangleAndGapSize, ArrayList<Node> rectanglesInfo) throws FileNotFoundException {
        int aroundInitialIndex = currentRectangleIndex - viewDirection - viewDirection * windowWidth / rectangleAndGapSize;
        for (int y = 0; y < viewField; y++) {
            for (int x = 0; x < viewField; x++) {
                final int index = aroundInitialIndex + x + y * windowWidth / rectangleAndGapSize;
                boolean leftAndRightControl =   index % (windowWidth / rectangleAndGapSize) <= currentRectangleIndex % (windowWidth / rectangleAndGapSize) + viewDirection &&
                        index % (windowWidth / rectangleAndGapSize) >= currentRectangleIndex % (windowWidth / rectangleAndGapSize) - viewDirection;
                boolean upAndDownControl =  index >= 0 &&
                        index < (windowWidth / rectangleAndGapSize) * (windowHeight / rectangleAndGapSize);

                if (upAndDownControl && leftAndRightControl) {
                    if (treasuresType.contains(rectanglesInfo.get(index).obstacleType)) {

                        // if treasure is close and has a type we look for
                        if (rectanglesInfo.get(index).treasure.getTreasureType() == targetTreasure &&  rectanglesInfo.get(index).treasure.getTreasureState() == TreasureState.CLOSE) {
                            rectanglesInfo.get(index).treasure.setTreasureState(TreasureState.OPEN);
                            HelloApplication.treasures.remove(rectanglesInfo.get(index).treasure);

                            boolean shouldChangeTarget = true;
                            for (Treasure treasure : HelloApplication.treasures) {
                                if (treasure.getTreasureType() == targetTreasure) {
                                    shouldChangeTarget = false;
                                }
                            }

                            if (shouldChangeTarget && !HelloApplication.treasures.isEmpty()) {
                                int treasureIndex = targetTreasure.ordinal() + 1;
                                targetTreasure = TreasureType.values()[treasureIndex];
                                System.out.println(targetTreasure);
                            }

                            rectanglesInfo.get(index).treasure.updateImage("pictures/" + rectanglesInfo.get(index).treasure.getTreasureType().name().toLowerCase() +"_chest_open.jpg");
                            System.out.println("This treasure convenient! I am reach :)");
                        }
                        else if (rectanglesInfo.get(index).treasure.getTreasureType() != targetTreasure &&
                                rectanglesInfo.get(index).treasure.getTreasureState() == TreasureState.CLOSE &&
                                !treasures.contains(rectanglesInfo.get(index).treasure)){
                            treasures.add(rectanglesInfo.get(index).treasure);
                            System.out.println("This treasure not convenient! But added");
                        }
                    }

                    if (!rectanglesInfo.get(index).isSeen)
                    {
                        rectanglesInfo.get(index).isSeen = true;
                        rectanglesInfo.get(index).imageView.setImage(null);
                    }
                }

            }
        }
    }


    public MotionDirection getBackDirection() {
        switch (frontDirection) {
            case UP:
                return MotionDirection.DOWN;
            case DOWN:
                return MotionDirection.UP;
            case LEFT:
                return MotionDirection.RIGHT;
            case RIGHT:
                return MotionDirection.LEFT;
        }
        return null;
    }

    public void setFrontDirection(MotionDirection direction) { frontDirection = direction; }
    public void setBackDirection(MotionDirection direction) { backDirection = direction; }
    public TreasureType getTargetTreasure() {return targetTreasure;}
}