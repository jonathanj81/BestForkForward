package com.example.jon.bestforkforward.NetworkUtils;

import android.app.Application;
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
        Log.i("FFFFFFFFFFFFFF", "getData started");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Log.i("FFFFFFFFFFFFFF", httpClient.toString());

        Retrofit retrofit =
                new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                        .client(httpClient)
                        .build();

        Log.i("FFFFFFFFFFFFFF", retrofit.baseUrl().toString());

        RecipeClient client = retrofit.create(RecipeClient.class);

        Log.i("FFFFFFFFFFFFFF", client.toString());

        Call<Recipe[]> call = client.getRecipesFromJson();

        Log.i("FFFFFFFFFFFFFF", call.toString());
        try {
            Recipe[] results = call.execute().body();
            Log.i("FFFFFFFFFFFFFF", "Made it to results");
            Log.i("FFFFFFFFFFFFFF", results[0].getName());
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertToDB(Context context){
        Log.i("AH HASFGH ASHG", "Retrofit triggered");
        RecipeRepository mRepo = new RecipeRepository(context);
        Recipe[] results = getData();

        for (Recipe recipe : results){
            mRepo.insertRecipe(recipe);
        }
    }
}
