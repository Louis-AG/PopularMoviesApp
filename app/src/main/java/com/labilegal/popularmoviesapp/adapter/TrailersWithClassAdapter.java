package com.labilegal.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.labilegal.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by louisag on 23/02/2017.
 */

public class TrailersWithClassAdapter extends RecyclerView.Adapter<TrailersWithClassAdapter.TrailersWithClassViewHolder> {

private static final String TAG = TrailersWithClassAdapter.class.getSimpleName();

private String[] mStringData;

private final TrailersWithClassAdapterOnclickHandler mClickHandler;


private Context mContext;

public TrailersWithClassAdapter(TrailersWithClassAdapterOnclickHandler clickHandler, Context context) {
        this.mClickHandler = clickHandler;
        this.mContext = context;
        }

/*
Interface for ClickHandler
 */
public interface TrailersWithClassAdapterOnclickHandler {
    void onClick(String stringClicked);
}

    @Override
    public TrailersWithClassAdapter.TrailersWithClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailers_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailersWithClassAdapter.TrailersWithClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersWithClassAdapter.TrailersWithClassViewHolder holder, int position) {
        String stringTrailer = mStringData[position];
        String img_url="http://img.youtube.com/vi/"+stringTrailer+"/0.jpg";
        Context context = holder.mImageViewTrailersItem.getContext();
        Picasso.with(context)
                .load(img_url)
                .placeholder(R.drawable.button_play)
                .error(R.drawable.button_play)
                .into(holder.mImageViewTrailersItem);
        holder.mTextViewTrailerItem.setText("Trailer " + String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        if (mStringData == null) {
            return 0;
        } else {
            return mStringData.length;
        }
    }

    public void setmStringData(String[] mStringData) {
        this.mStringData = mStringData;
        notifyDataSetChanged();
    }

public class TrailersWithClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImageViewTrailersItem;
    TextView mTextViewTrailerItem;

    public TrailersWithClassViewHolder(View itemView) {
        super(itemView);
        this.mImageViewTrailersItem = (ImageView) itemView.findViewById(R.id.iv_trailer_item);
        this.mTextViewTrailerItem = (TextView) itemView.findViewById(R.id.tv_trailer_item);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();
        String string = mStringData[adapterPosition];
        mClickHandler.onClick(string);
    }
}
}
