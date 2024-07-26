package com.example.prolab2_1;

import java.io.FileNotFoundException;

public class TreasureGenerator{
    Treasure treasure;

    Treasure goldChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/gold_chest_close.png", TreasureType.GOLD);
    }
    Treasure silverChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/silver_chest_close.png", TreasureType.SILVER);
    }
    Treasure emeraldChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/emerald_chest_close.png", TreasureType.EMERALD);
    }
    Treasure copperChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/copper_chest_close.png", TreasureType.COPPER);
    }
}