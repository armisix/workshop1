package com.example.varut.workshop1.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.varut.workshop1.FragmentRecyclerView;
import com.example.varut.workshop1.R;
import com.example.varut.workshop1.SecondActivity;
import com.example.varut.workshop1.volley.MySingleton;

import java.util.List;

/**
 * Created by Varut on 04/23/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public static final String TAG = "RecyclerAdapter";
    public static final String PREFIX_IMAGE = "http://dev.ctrlyati.in.th/uploads/";

    private Context mContext;
    private List<ImageModel> mImages;
    private String checkViewType;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public NetworkImageView mNetworkImageView;
        public TextView mName;
        public TextView mSize;
        public TextView mDate;
        public TextView mType;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            mNetworkImageView = (NetworkImageView) view.findViewById(R.id.networkImageView);
            mName = (TextView) view.findViewById(R.id.name);
            mSize = (TextView) view.findViewById(R.id.size);
            mDate = (TextView) view.findViewById(R.id.date);
            mType = (TextView) view.findViewById(R.id.type);
        }

        @Override
        public void onClick(View v) {

            int pos = getPosition();
            Log.d(TAG, "onClick " + pos);

            Intent intent = new Intent(mContext, SecondActivity.class);
            intent.putExtra("name", mImages.get(pos).getName());
            intent.putExtra("size", mImages.get(pos).getSize());
            intent.putExtra("date", mImages.get(pos).getDate());
            intent.putExtra("type", mImages.get(pos).getType());
            intent.putExtra("imageUrl", PREFIX_IMAGE + mImages.get(pos).getName());
            mContext.startActivity(intent);
        }
    }

    public RecyclerAdapter(Context context, List<ImageModel> dataset, String viewType) {
        mContext = context;
        mImages = dataset;
        checkViewType = viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(checkViewType.equals(FragmentRecyclerView.MNG_GRID)){
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.recyclerview_grid, parent, false);
        } else if(checkViewType.equals(FragmentRecyclerView.MNG_LIST_VERTICAL)){
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.recyclerview_list_vertical, parent, false);
        } else if(checkViewType.equals(FragmentRecyclerView.MNG_LIST_HORIZONTAL)){
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.recyclerview_list_horizontal, parent, false);
        }
        else { // Default
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.recyclerview_grid, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ImageModel image = mImages.get(position);

        viewHolder.mName.setText(image.getName());
        viewHolder.mSize.setText(image.getSize());
        viewHolder.mDate.setText(image.getDate());
        viewHolder.mType.setText(image.getType());

        String imageUrl = PREFIX_IMAGE + image.getName();
        ImageLoader mImageLoader;
        mImageLoader = MySingleton.getInstance(mContext).getImageLoader();
        viewHolder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

}