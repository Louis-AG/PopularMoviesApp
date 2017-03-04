package com.labilegal.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labilegal.popularmoviesapp.R;
import com.labilegal.popularmoviesapp.data.Review;

/**
 * Created by louisag on 24/02/2017.
 */

public class ReviewsWithClassAdapter  extends RecyclerView.Adapter<ReviewsWithClassAdapter.ReviewsWithClassViewHolder> {

    private static final String TAG = ReviewsWithClassAdapter.class.getSimpleName();

    private Review[] mReviewData;

    private Context mContext;

    public ReviewsWithClassAdapter( Context context) {
        this.mContext = context;
    }


    @Override
    public ReviewsWithClassAdapter.ReviewsWithClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.reviews_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewsWithClassAdapter.ReviewsWithClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsWithClassAdapter.ReviewsWithClassViewHolder holder, int position) {
        Review review = mReviewData[position];
        holder.mTextViewReviewItemAuthor.setText(review.getmAuthor());
        holder.mTextViewReviewItemContent.setText(review.getmContent());
    }

    @Override
    public int getItemCount() {
        if (mReviewData == null) {
            return 0;
        } else {
            return mReviewData.length;
        }
    }

    public void setmReviewsData(Review[] ReviewData) {
        this.mReviewData = ReviewData;
        notifyDataSetChanged();
    }

    public class ReviewsWithClassViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewReviewItemAuthor;
        TextView mTextViewReviewItemContent;

        public ReviewsWithClassViewHolder(View itemView) {
            super(itemView);
            this.mTextViewReviewItemAuthor = (TextView) itemView.findViewById(R.id.tv_reviews_item_author);
            this.mTextViewReviewItemContent = (TextView) itemView.findViewById(R.id.tv_reviews_item_content);
        }
    }
}
