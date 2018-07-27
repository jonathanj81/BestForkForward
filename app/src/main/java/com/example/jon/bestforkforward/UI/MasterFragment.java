package com.example.jon.bestforkforward.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeViewModel;
import com.example.jon.bestforkforward.R;

import org.w3c.dom.Text;

import java.util.List;

public class MasterFragment extends Fragment {

    private Typeface mMerienda;

    public MasterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        final RecyclerView recycler = rootView.findViewById(R.id.recyclerview);
        final MasterAdapter adapter = new MasterAdapter();

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        viewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });

        final CollapsingToolbarLayout toolLayout = rootView.findViewById(R.id.toolbar_layout);
        toolLayout.setCollapsedTitleTypeface(mMerienda);
        toolLayout.setExpandedTitleTypeface(mMerienda);

        AppBarLayout appBarLayout = rootView.findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0){
                    toolLayout.setTitle("Best Fork Forward");
                } else {
                    toolLayout.setTitle("Best Fork Forward: desserts");
                }
            }
        });

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.fork_meal_table);
        Palette.from(bitmap).maximumColorCount(64).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                toolLayout.setStatusBarScrimColor(palette.getDarkMutedSwatch().getRgb());
                toolLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
                toolLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
                recycler.setBackgroundColor(palette.getDarkMutedSwatch().getRgb());
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMerienda = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Merienda-Regular.ttf");
    }
}
