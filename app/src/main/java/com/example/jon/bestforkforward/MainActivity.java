package com.example.jon.bestforkforward;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeDatabase;
import com.example.jon.bestforkforward.DataHandling.RecipeRepository;
import com.example.jon.bestforkforward.DataHandling.RecipeViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView testView = findViewById(R.id.test_textview);

        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        viewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null && recipes.size() > 0) {
                    testView.setText(recipes.get(0).getName());
                } else {
                    testView.setText("Good god");
                }
            }
        });
    }
}
