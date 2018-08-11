package com.example.jon.bestforkforward.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.IngredientsViewModel;
import com.example.jon.bestforkforward.DataHandling.IngredientsViewModelFactory;
import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.R;

public class StepsFragment extends Fragment {

    private int mRecipeID;
    public static String dessertName;

    private static final String GENERIC_KEY_STRING = "recipe_id";
    private static final String PREFS_MAIN_KEY = "recipe_id_main_memory";

    public StepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        final RecyclerView recycler = rootView.findViewById(R.id.recyclerview);
        final StepsAdapter adapter = new StepsAdapter();

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setHasFixedSize(true);
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mRecipeID = bundle.getInt(GENERIC_KEY_STRING, 1);
        }

        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_MAIN_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GENERIC_KEY_STRING, mRecipeID);
        editor.apply();

        final CollapsingToolbarLayout toolLayout = getActivity().findViewById(R.id.toolbar_layout);
        final View view = getActivity().findViewById(R.id.scrim_gradient);
        final TextView titleView = getActivity().findViewById(R.id.toolbar_title_textview);

        IngredientsViewModel viewModel = ViewModelProviders.of(this,
                new IngredientsViewModelFactory(getContext(), mRecipeID)).get(IngredientsViewModel.class);
        viewModel.getSingleRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                adapter.setSteps(recipe.getSteps());
                dessertName = recipe.getName();
                toolLayout.setBackground(new BitmapDrawable(getResources(), getBitmap(mRecipeID)));

                AppBarLayout appBarLayout = getActivity().findViewById(R.id.app_bar);
                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (getActivity() != null && isAdded()) {
                            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                                titleView.setText(getString(R.string.collapsed_title_text));
                            } else {
                                titleView.setText(getString(R.string.expanded_title_text, dessertName));
                            }
                        }
                    }
                });
            }
        });

        Palette.from(getBitmap(mRecipeID)).maximumColorCount(64).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(@NonNull Palette palette) {
                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                if (swatch != null) {
                    toolLayout.setStatusBarScrimColor(swatch.getRgb());
                    recycler.setBackgroundColor(swatch.getRgb());
                }
                view.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    private Bitmap getBitmap(int id) {
        Bitmap bitmap;

        switch (id) {
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nutella);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chocolatebrownie);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yellowcake);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cheesecake);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fork_meal_table);
                break;
        }
        return bitmap;
    }
}
