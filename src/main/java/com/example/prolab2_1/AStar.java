package com.example.prolab2_1;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class AStar {
    boolean goalReached = false;

    Node startRectangleInfo, goalRectangleInfo, currentRectangleInfo;

    static ArrayList<Node> openList = new ArrayList<>();
    static ArrayList<Node> checkedList = new ArrayList<>();
    ArrayList<Node> path = new ArrayList<>();

    public void setStartNode(Node node) {
        node.setAsStart(false);
        startRectangleInfo = node;
        currentRectangleInfo = startRectangleInfo;
    }

    public void setGoalNode(Node node) {
        node.setAsGoal(true);
        goalRectangleInfo = node;
    }

    public void setCostOnNodes(ArrayList<Node> nodes, int rectangleAmountX, int rectangleAmountY) {
        for (int column = 0; column < rectangleAmountX; column++) {
            for (int row = 0; row < rectangleAmountY; row++) {
                getCost(nodes.get(column + row * rectangleAmountX));
            }
        }
    }

    private void getCost(Node node) {
        int xDistance = Math.abs(node.column - startRectangleInfo.column);
        int yDistance = Math.abs(node.row - startRectangleInfo.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.column - goalRectangleInfo.column);
        yDistance = Math.abs(node.row - goalRectangleInfo.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }

    public void search(ArrayList<Node> rectanglesInfo, int rectangleAmountX, int rectangleAmountY) {
        if (!goalReached) {
            Character.canMove = false;

            int row = currentRectangleInfo.row;
            int column = currentRectangleInfo.column;
            currentRectangleInfo.setAsChecked(true);
            checkedList.add(currentRectangleInfo);
            openList.remove(currentRectangleInfo);

            if (row - 1 >= 0) {
                openRectangleInfo(rectanglesInfo.get(column + (row - 1) * rectangleAmountX));
            }
            if (column - 1 >= 0) {
                openRectangleInfo(rectanglesInfo.get((column - 1) + row * rectangleAmountX));
            }
            if (row + 1 < rectangleAmountY) {
                openRectangleInfo(rectanglesInfo.get(column + (row + 1) * rectangleAmountX));
            }
            if (column + 1 < rectangleAmountX) {
                openRectangleInfo(rectanglesInfo.get((column + 1) + row * rectangleAmountX));
            }

            int bestRectangleInfoIndex = 0;
            int bestRectangleInfofCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestRectangleInfofCost) {
                    bestRectangleInfoIndex = i;
                    bestRectangleInfofCost = openList.get(i).fCost;
                }
                else if (openList.get(i).fCost == bestRectangleInfofCost) {
                    if (openList.get(i).gCost < openList.get(bestRectangleInfoIndex).gCost) {
                        bestRectangleInfoIndex = i;
                    }
                }
            }

            if (!openList.isEmpty())
            {
                currentRectangleInfo = openList.get(bestRectangleInfoIndex);
                if (currentRectangleInfo == goalRectangleInfo) {
                    goalReached = true;
                    trackThePath();
                }
            }
        }
    }

    public void trackThePath() {
        Node current = goalRectangleInfo;

        while (current != startRectangleInfo) {
            current = current.parent;
            if (current != startRectangleInfo) {
                current.setAsPath();
                path.add(current);
            }
        }

        Character.fillPathDirectionList(path);
        Character.canMove = true;
        path.clear();

        for (Node n : checkedList){
            n.setAsOpen(false);
            n.setAsChecked(false);
        }
        for (Node n : openList){
            n.setAsOpen(false);
            n.setAsChecked(false);
        }

        checkedList.clear();
        openList.clear();

        startRectangleInfo = null;
        goalRectangleInfo = null;
    }

    public void openRectangleInfo(Node RectangleInfo) {
        if (!RectangleInfo.isOpen && !RectangleInfo.isChecked && RectangleInfo.canBeUsedOnPathfinding) {
            RectangleInfo.setAsOpen(true);
            RectangleInfo.parent = currentRectangleInfo;
            openList.add(RectangleInfo);
        }
    }
}

/* TAMAM */