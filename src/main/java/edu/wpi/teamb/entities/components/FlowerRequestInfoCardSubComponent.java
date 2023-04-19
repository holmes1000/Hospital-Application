package edu.wpi.teamb.entities.components;

import java.util.ArrayList;

public class FlowerRequestInfoCardSubComponent {
    public ArrayList<String> getFlowerTypes() {
        ArrayList<String> flowerTypes = new ArrayList<String>();
        flowerTypes.add("Rose");
        flowerTypes.add("Tulip");
        flowerTypes.add("Daisy");
        flowerTypes.add("Lily");
        flowerTypes.add("Sunflower");
        return flowerTypes;
    }

    public ArrayList<String> getColors() {
        ArrayList<String> colors = new ArrayList<String>();
        colors.add("Red");
        colors.add("Orange");
        colors.add("Yellow");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Purple");
        colors.add("Pink");
        colors.add("White");
        colors.add("Black");
        colors.add("Brown");
        colors.add("Other");
        return colors;
    }

    public ArrayList<String> getBuschellTypes() {
        ArrayList<String> buschellTypes = new ArrayList<String>();
        buschellTypes.add("Bouquet");
        buschellTypes.add("Single Flower");
        buschellTypes.add("Vase");
        buschellTypes.add("Other");
        return buschellTypes;
    }
}
