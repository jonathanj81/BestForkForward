package com.example.jon.bestforkforward.DataHandling;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "recipe_table")
public class Recipe {

    @PrimaryKey
    @SerializedName("id")
    private int recipe_id;

    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private List<Ingredient> ingredients;
    @SerializedName("steps")
    private List<Step> steps;
    @SerializedName("servings")
    private String servings;
    @SerializedName("image")
    private String image;

    public Recipe(int recipe_id, String name, List<Ingredient> ingredients, List<Step> steps,
                  String servings, String image){
        this.recipe_id = recipe_id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public int getRecipe_id() {
        return this.recipe_id;
    }

    public String getName() {
        return this.name;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    public String getServings() {
        return this.servings;
    }

    public String getImage() {
        return this.image;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
