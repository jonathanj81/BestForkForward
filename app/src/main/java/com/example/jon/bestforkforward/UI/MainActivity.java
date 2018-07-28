package com.example.jon.bestforkforward.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeViewModel;
import com.example.jon.bestforkforward.NetworkUtils.RetrofitFetcher;
import com.example.jon.bestforkforward.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabsPagerAdapter mTabsPagerAdapter;
    private ViewPager mViewPager;
    private RecipesFragment mRecipesFragment;
    private IngredientsFragment mIngredientsFragment;
    private InstructionsFragment mInstructionsFragment;

    private int mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CollapsingToolbarLayout toolLayout = findViewById(R.id.toolbar_layout);
        Typeface mMerienda = Typeface.createFromAsset(getAssets(), "fonts/Merienda-Regular.ttf");
        toolLayout.setCollapsedTitleTypeface(mMerienda);
        toolLayout.setExpandedTitleTypeface(mMerienda);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    toolLayout.setTitle("Best Fork Forward");
                } else {
                    toolLayout.setTitle("Best Fork Forward: desserts");
                }
            }
        });

        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mTabsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tablayout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fork_meal_table);
        Palette.from(bitmap).maximumColorCount(128).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(@NonNull Palette palette) {
                toolLayout.setStatusBarScrimColor(palette.getDarkMutedSwatch().getRgb());
                toolLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
                toolLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
                mViewPager.setBackgroundColor(palette.getLightMutedSwatch().getRgb());
            }
        });

        mRecipesFragment = new RecipesFragment();
        mIngredientsFragment = new IngredientsFragment();
        mInstructionsFragment = new InstructionsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"Recipes", "Ingredients", "Steps"};

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mRecipesFragment;
                case 1:
                    return mIngredientsFragment;
                case 2:
                    return mInstructionsFragment;
                default:
                    return mRecipesFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
