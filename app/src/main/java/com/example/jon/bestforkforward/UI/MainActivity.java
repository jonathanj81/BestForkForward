package com.example.jon.bestforkforward.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeViewModel;
import com.example.jon.bestforkforward.NetworkUtils.RetrofitFetcher;
import com.example.jon.bestforkforward.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String name;
    private Typeface mMerienda;
    private FrameLayout container;
    private CollapsingToolbarLayout toolLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMerienda = Typeface.createFromAsset(getAssets(), "fonts/Merienda-Regular.ttf");

        toolLayout = findViewById(R.id.toolbar_layout);
        toolLayout.setCollapsedTitleTypeface(mMerienda);
        toolLayout.setExpandedTitleTypeface(mMerienda);

        container = findViewById(R.id.container);

        Fragment master = new MasterFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,master)
                .addToBackStack(null).commit();
    }

    public void setActionBarTitle(String title) {
        toolLayout.setTitle(title);
    }
}
