package com.example.jon.bestforkforward.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeViewModel;
import com.example.jon.bestforkforward.NetworkUtils.RetrofitFetcher;
import com.example.jon.bestforkforward.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
