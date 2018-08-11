package com.example.jon.bestforkforward.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.jon.bestforkforward.R;

public class MainActivity extends AppCompatActivity {

    private static String name;
    private Typeface mMerienda;
    private FrameLayout container;
    private CollapsingToolbarLayout toolLayout;
    private DrawerLayout mDrawerLayout;
    private int mRecipeId;
    private boolean isLand;
    public static int stateID = 1;
    public static boolean isInstructions = true;
    private boolean recreate = false;

    private static final String GENERIC_KEY_STRING = "recipe_id";
    private static final String PREFS_MAIN_KEY = "recipe_id_main_memory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            stateID = savedInstanceState.getInt(GENERIC_KEY_STRING);
            isInstructions = savedInstanceState.getBoolean("which_one");
            recreate = true;
        }

        isLand = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isLand){
            openSmallLand();
        } else {
            openSmallPortrait();
        }
        setToolbarLayout();
        setNavigationDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNavigationDrawer(){

        final int detailContainerID = isLand ? R.id.container_right : R.id.container;
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        SharedPreferences prefs = MainActivity.this.getSharedPreferences(PREFS_MAIN_KEY,
                                Context.MODE_PRIVATE);
                        int activeRecipeID = prefs.getInt(GENERIC_KEY_STRING, 1);

                        switch (menuItem.getItemId()){
                            case R.id.nav_main:
                                Fragment master = new MasterFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.container, master)
                                        .addToBackStack(null).commit();
                                break;
                            case R.id.nav_ingredients:
                                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                                Bundle bundleA = new Bundle();
                                bundleA.putInt(GENERIC_KEY_STRING, activeRecipeID);
                                ingredientsFragment.setArguments(bundleA);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(detailContainerID, ingredientsFragment)
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case R.id.nav_instructions:
                                StepsFragment stepsFragment = new StepsFragment();
                                Bundle bundleB = new Bundle();
                                bundleB.putInt(GENERIC_KEY_STRING, activeRecipeID);
                                stepsFragment.setArguments(bundleB);
                                getSupportFragmentManager().beginTransaction().replace(detailContainerID, stepsFragment)
                                        .addToBackStack(null).commit();
                        }

                        return true;
                    }
                });
    }

    private void setToolbarLayout(){
        mMerienda = Typeface.createFromAsset(getAssets(), "fonts/Merienda-Regular.ttf");
        TextView titleView = findViewById(R.id.toolbar_title_textview);
        titleView.setTypeface(mMerienda);

        toolLayout = findViewById(R.id.toolbar_layout);
        toolLayout.setTitleEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void openSmallPortrait(){
        Fragment master = new MasterFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, master)
                .addToBackStack(null).commit();

        Intent fromWidget = getIntent();
        if (fromWidget != null){
            mRecipeId = fromWidget.getIntExtra(GENERIC_KEY_STRING, 0);
            if (mRecipeId != 0){
                stateID = mRecipeId;
                StepsFragment stepsFragment = new StepsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(GENERIC_KEY_STRING, mRecipeId);
                stepsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, stepsFragment)
                        .addToBackStack(null).commit();
                return;
            }
        }
        if (recreate){
            Fragment fragment;
            if (isInstructions){
                fragment = new StepsFragment();
            }else {
                fragment = new IngredientsFragment();
            }
            Bundle bundle = new Bundle();
            bundle.putInt(GENERIC_KEY_STRING, stateID);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                    .addToBackStack(null).commit();
        }
    }

    private void openSmallLand(){
        Fragment master = new MasterFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, master)
                .addToBackStack(null).commit();

        Fragment fragment = new StepsFragment();
        int useThisID = 1;

        if (recreate){
            useThisID = stateID;
            if (!isInstructions){
                fragment = new IngredientsFragment();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt(GENERIC_KEY_STRING, useThisID);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_right, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(GENERIC_KEY_STRING, stateID);
        outState.putBoolean("which_one", isInstructions);
        super.onSaveInstanceState(outState);
    }
}
