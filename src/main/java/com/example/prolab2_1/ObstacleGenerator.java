package com.example.prolab2_1;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class ObstacleGenerator {
    int randomIndex;
    Random random = new Random();
    TreeObstacles[] treeObstacles = TreeObstacles.values();
    RockObstacles[] rockObstacles = RockObstacles.values();

    // Creates Two Times From Whole Obstacles
    public void createDefaultObstacles(ArrayList<StaticObstacle> staticObstacles) throws FileNotFoundException {
        for (int i = 0; i < 2; i++) {
            for (TypeObstacles typeObstacles : TypeObstacles.values()) {
                staticObstacles.add(generateSummerObstacle(typeObstacles));
                staticObstacles.add(generateWinterObstacle(typeObstacles));
            }
        }
    }

    public StaticObstacle generateSummerObstacle(TypeObstacles typeObstacles) throws FileNotFoundException {
        switch (typeObstacles) {
            case WALL:
                return new StaticObstacle("pictures/wall_summer.jpg", 10, 1, Season.SUMMER, TypeObstacles.WALL);
            case MOUNTAIN:
                return new StaticObstacle("pictures/mountain_summer.png", 15, 15, Season.SUMMER, TypeObstacles.MOUNTAIN);
            case TREE:
                randomIndex = random.nextInt(TreeObstacles.values().length);
                switch (treeObstacles[randomIndex]) {
                    case TREE2X2:
                        return new StaticObstacle("pictures/tree_summer_2x2.png", 2, 2, Season.SUMMER, TypeObstacles.TREE);
                    case TREE3X3:
                        return new StaticObstacle("pictures/tree_summer_3x3.png", 3, 3, Season.SUMMER, TypeObstacles.TREE);
                    case TREE4X4:
                        return new StaticObstacle("pictures/tree_summer_4x4.png", 4, 4, Season.SUMMER, TypeObstacles.TREE);
                    case TREE5X5:
                        return new StaticObstacle("pictures/tree_summer_5x5.png", 5, 5, Season.SUMMER, TypeObstacles.TREE);
                }
            case ROCK:
                randomIndex = random.nextInt(RockObstacles.values().length);
                switch (rockObstacles[randomIndex]) {
                    case ROCK2X2:
                        return new StaticObstacle("pictures/rock_summer_2x2.png", 2, 2, Season.SUMMER, TypeObstacles.ROCK);
                    case ROCK3X3:
                        return new StaticObstacle("pictures/rock_summer_3x3.png", 3, 3, Season.SUMMER, TypeObstacles.ROCK);
                }
        }
        return null;
    }

    public StaticObstacle generateWinterObstacle(TypeObstacles typeObstacles) throws FileNotFoundException {
        switch (typeObstacles) {
            case WALL:
                return new StaticObstacle("pictures/wall_winter.png", 10, 1, Season.WINTER, TypeObstacles.WALL);
            case MOUNTAIN:
                return new StaticObstacle("pictures/mountain_winter.png", 15, 15, Season.WINTER, TypeObstacles.MOUNTAIN);
            case TREE:
                randomIndex = random.nextInt(TreeObstacles.values().length);
                switch (treeObstacles[randomIndex]) {
                    case TREE2X2:
                        return new StaticObstacle("pictures/tree_winter_2x2.png", 2, 2, Season.WINTER, TypeObstacles.TREE);
                    case TREE3X3:
                        return new StaticObstacle("pictures/tree_winter_3x3.png", 3, 3, Season.WINTER, TypeObstacles.TREE);
                    case TREE4X4:
                        return new StaticObstacle("pictures/tree_winter_4x4.png", 4, 4, Season.WINTER, TypeObstacles.TREE);
                    case TREE5X5:
                        return new StaticObstacle("pictures/tree_winter_5x5.png", 5, 5, Season.WINTER, TypeObstacles.TREE);
                }
            case ROCK:
                randomIndex = random.nextInt(RockObstacles.values().length);
                switch (rockObstacles[randomIndex]) {
                    case ROCK2X2:
                        return new StaticObstacle("pictures/rock_winter_2x2.png", 2, 2, Season.WINTER, TypeObstacles.ROCK);
                    case ROCK3X3:
                        return new StaticObstacle("pictures/rock_winter_3x3.png", 3, 3, Season.WINTER, TypeObstacles.ROCK);
                }
        }
        return null;
    }

    public DynamicObstacle generateDynamicObstacle (DynamicObstacles dynamicObstacle) throws FileNotFoundException {
        switch (dynamicObstacle){
            case BEE:
                return new DynamicObstacle("pictures/bee.png", 5, 2,2, 2, DynamicObstacles.BEE);
            case BIRD:
                return new DynamicObstacle("pictures/bird.png", 2, 9, 2, 2, DynamicObstacles.BIRD);
        }
        return null;
    }
}
