package com.example.jon.bestforkforward.DataHandling;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListConverter {

    @TypeConverter
    public static List<Ingredient> ingredientStringToList(String delimited) {
        List<String> tempList = Arrays.asList(delimited.split("---"));
        List<Ingredient> ingredients = new ArrayList<>();

        for (int i = 0; i < tempList.size(); i += 3){
            Ingredient tempIngredient = new Ingredient(Float.valueOf(tempList.get(i)),
                    tempList.get(i+1),tempList.get(i+2));
            ingredients.add(tempIngredient);
        }
        return ingredients;
    }

    @TypeConverter
    public static String ingredientListToString(List<Ingredient> list) {
        StringBuilder temp = new StringBuilder();
        for (Ingredient item : list){
            temp.append(String.valueOf(item.getQuantity())).append("---")
                    .append(item.getMeasure()).append("---")
                    .append(item.getIngredient()).append("---");
        }
        return temp.toString();
    }

    @TypeConverter
    public static List<Step> stepStringToList(String delimited) {
        List<String> tempList = Arrays.asList(delimited.split("---"));
        List<Step> steps = new ArrayList<>();

        for (int i = 0; i < tempList.size()-5; i += 5){
            Step tempStep = new Step(Integer.valueOf(tempList.get(i)),
                    tempList.get(i+1),tempList.get(i+2), tempList.get(i+3),
                    tempList.get(i+4));
            steps.add(tempStep);
        }
        return steps;
    }

    // gave me retrofit library lessons. thank you.

    @TypeConverter
    public static String stepListToString(List<Step> list) {
        StringBuilder temp = new StringBuilder();
        for (Step item : list){
            temp.append(String.valueOf(item.getId())).append("---")
                    .append(item.getShortDescription()).append("---")
                    .append(item.getDescription()).append("---")
                    .append(item.getVideoURL()).append("---")
                    .append(item.getThumbnailURL()).append("---");
        }
        return temp.toString();
    }
}
