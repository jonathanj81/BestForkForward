package com.example.jon.bestforkforward.NetworkUtils;

import com.example.jon.bestforkforward.DataHandling.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeClient {

    @GET()
    Call<Recipe[]> getRecipesFromJson();
}
