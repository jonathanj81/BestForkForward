package com.example.jon.bestforkforward.UI;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Ingredient;
import com.example.jon.bestforkforward.DataHandling.Step;
import com.example.jon.bestforkforward.R;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    private List<Step> mSteps;

    @NonNull
    @Override
    public StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_card_view,parent,false);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapterViewHolder holder, int position) {

        holder.mNumber.setText(String.valueOf(mSteps.get(position).getId()+1));
        holder.mShortDescription.setText(mSteps.get(position).getShortDescription());
        String url = mSteps.get(position).getVideoURL();
        if (url == null || url.isEmpty()){
            holder.mVideoIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        else return mSteps.size();
    }

    public void setSteps(List<Step> steps){
        mSteps = steps;
        notifyDataSetChanged();
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNumber;
        private TextView mShortDescription;
        private ImageView mVideoIcon;

        public StepsAdapterViewHolder(View itemView) {
            super(itemView);
            mNumber = itemView.findViewById(R.id.step_number);
            mShortDescription = itemView.findViewById(R.id.step_short_description);
            mVideoIcon = itemView.findViewById(R.id.video_present_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentManager fm = ((MainActivity)v.getContext()).getSupportFragmentManager();
            VideoDialogFragment frag = VideoDialogFragment.
                    newInstance(mSteps, getAdapterPosition());
            frag.show(fm, "video_frag");
        }
    }
}
