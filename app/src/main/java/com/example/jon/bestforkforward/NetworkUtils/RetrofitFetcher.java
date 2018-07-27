package com.example.jon.bestforkforward.NetworkUtils;

import android.content.Context;
import android.util.Log;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeRepository;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFetcher {



    public static Recipe[] getData() {
        OkHttpClient httpClient = new OkHttpClient();

        Retrofit retrofit =
                new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/")
                        .client(httpClient)
                        .build();

        RecipeClient client = retrofit.create(RecipeClient.class);

        Call<Recipe[]> call = client.getRecipesFromJson();

        try {
           return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
