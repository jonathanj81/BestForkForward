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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeViewModel;
import com.example.jon.bestforkforward.R;

import org.w3c.dom.Text;

import java.util.List;

public class MasterFragment extends Fragment {

    private RecyclerView recycler;

    public MasterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        recycler = rootView.findViewById(R.id.recyclerview);
        final MasterAdapter adapter = new MasterAdapter();

        recycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        viewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });

        final CollapsingToolbarLayout toolLayout = getActivity().findViewById(R.id.toolbar_layout);
        final TextView titleView = getActivity().findViewById(R.id.toolbar_title_textview);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (getActivity() != null && isAdded()) {
                    if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                        titleView.setText(getString(R.string.collapsed_title_text));
                    } else {
                        titleView.setText(getString(R.string.expanded_title_text,
                                getString(R.string.default_expanded_text)));
                    }
                }
            }
        });

        final View view = getActivity().findViewById(R.id.scrim_gradient);
        view.setVisibility(View.INVISIBLE);

        ((ImageView)getActivity().findViewById(R.id.test_toolbar_imageview))
                .setImageResource(R.drawable.fork_meal_table);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fork_meal_table);
        Palette.from(bitmap).maximumColorCount(64).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(@NonNull Palette palette) {
                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                if (swatch != null) {
                    toolLayout.setStatusBarScrimColor(swatch.getRgb());
                    recycler.setBackgroundColor(swatch.getRgb());
                }
            }
        });

        return rootView;
    }
}
