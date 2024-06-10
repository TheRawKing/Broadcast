package com.therawking.broadcast;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChannelAdapter extends FirebaseRecyclerAdapter <ChannelModel,ChannelAdapter.myViewHolder> {

    public ChannelAdapter(@NonNull FirebaseRecyclerOptions<ChannelModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull ChannelModel channelModel) {
        myViewHolder.name.setText(channelModel.getChannelName());
        myViewHolder.id.setText(channelModel.getChannelID());
        myViewHolder.ad.setChecked(channelModel.isAdmin());
        Glide.with(myViewHolder.img.getContext())
                .load(channelModel.getChannelDP())
                .circleCrop()
                .into(myViewHolder.img);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item,parent,false);
        return new myViewHolder(view);
    }

    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "met a IOOBE in RecyclerView");
            }
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name, id;
        Switch ad;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.itemDP);
            name = (TextView) itemView.findViewById(R.id.itemName);
            id = (TextView) itemView.findViewById(R.id.itemID);
            ad = (Switch) itemView.findViewById(R.id.admin);


        }
    }

}
