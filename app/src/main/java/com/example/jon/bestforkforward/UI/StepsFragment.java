package com.example.jon.bestforkforward.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jon.bestforkforward.DataHandling.IngredientsViewModel;
import com.example.jon.bestforkforward.DataHandling.IngredientsViewModelFactory;
import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.R;

public class StepsFragment extends Fragment {

    private int mRecipeID;
    public static String dessertName;

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
            mRecipeID = bundle.getInt("recipe_id", 0);
        }

        final CollapsingToolbarLayout toolLayout = getActivity().findViewById(R.id.toolbar_layout);
        final View view = getActivity().findViewById(R.id.scrim_gradient);

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
                        if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                            toolLayout.setTitle("Best Fork Forward");
                        } else {
                            toolLayout.setTitle("Best Fork Forward: " + dessertName);
                        }
                    }
                });
            }
        });

        Palette.from(getBitmap(mRecipeID)).maximumColorCount(64).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                toolLayout.setStatusBarScrimColor(palette.getDarkMutedSwatch().getRgb());
                recycler.setBackgroundColor(palette.getDarkMutedSwatch().getRgb());
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
