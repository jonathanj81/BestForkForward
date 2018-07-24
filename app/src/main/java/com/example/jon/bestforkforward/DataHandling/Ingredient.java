package com.example.jon.bestforkforward.DataHandling;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    private int quantity;
    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredient;

    public Ingredient(int quantity, String measure, String ingredient){
        this.ingredient = ingredient;
        this.measure = measure;
        this.quantity = quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public String getIngredient() {
        return this.ingredient;
    }

    public String getMeasure() {
        return this.measure;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setMeasure(String measure){
        this.measure = measure;
    }

    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }
}
