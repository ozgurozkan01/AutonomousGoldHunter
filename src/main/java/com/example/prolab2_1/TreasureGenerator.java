package com.example.prolab2_1;

import java.io.FileNotFoundException;

public class TreasureGenerator{
    Treasure treasure;

    Treasure goldChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/gold_chest.png", TreasureType.GOLD);
    }
    Treasure silverChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/silver_chest.png", TreasureType.SILVER);
    }
    Treasure emeraldChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/emerald_chest.png", TreasureType.EMERALD);
    }
    Treasure copperChest() throws FileNotFoundException {
        return treasure = new Treasure("pictures/copper_chest.png", TreasureType.COPPER);
    }
}
