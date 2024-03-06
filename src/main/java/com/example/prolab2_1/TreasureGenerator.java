package com.example.prolab2_1;

import java.io.FileNotFoundException;

public class TreasureGenerator{
    Treasure treasure;
    Treasure goldChest() throws FileNotFoundException {
        return treasure = new Treasure("gold", "pictures/gold_chest.png");
    }
    Treasure silverChest() throws FileNotFoundException {
        return treasure = new Treasure("silver", "pictures/silver_chest.png");
    }
    Treasure emeraldChest() throws FileNotFoundException {
        return treasure = new Treasure("emerald", "pictures/emerald_chest.png");
    }
    Treasure copperChest() throws FileNotFoundException {
        return treasure = new Treasure("copper", "pictures/copper_chest.png");
    }
}
