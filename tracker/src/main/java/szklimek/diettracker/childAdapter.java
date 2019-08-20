/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package szklimek.diettracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/***
 * The adapter class for the RecyclerView, contains the sports data
 */
class childAdapter extends RecyclerView.Adapter<childAdapter.ChildViewHolder>  {

    //Member variables
    private GradientDrawable mGradientDrawable;
    private ArrayList<Child> mSportsData;
    private Context mContext;

    /**
     * Constructor that passes in the sports data and the context
     * @param sportsData ArrayList containing the sports data
     * @param context Context of the application
     */
    childAdapter(Context context, ArrayList<Child> sportsData) {
        this.mSportsData = sportsData;
        this.mContext = context;

        //Prepare gray placeholder
        mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setColor(Color.GRAY);

        //Make the placeholder same size as the images
        Drawable drawable = ContextCompat.getDrawable
                (mContext,R.drawable.user);
        if(drawable != null) {
            mGradientDrawable.setSize(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }


    /**
     * Required method for creating the viewholder objects.
     * @param parent The ViewGroup into which the new View is added after it is
     *               bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly create ChildViewHolder.
     */
    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(mContext, LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false), mGradientDrawable);
    }

    /**
     * Required method that binds the data to the viewholder.
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {

        //Get the current sport
        Child currentChild = mSportsData.get(position);

        //Bind the data to the views
        holder.bindTo(currentChild);

    }


    /**
     * Required method for determining the size of the data set.
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return mSportsData.size();
    }


    /**
     * ChildViewHolder class that represents each row of data in the RecyclerView
     */
    static class ChildViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        //Member Variables for the holder data
        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mSportsImage;
        private Context mContext;
        private Child mCurrentChild;
        private GradientDrawable mGradientDrawable;

        /**
         * Constructor for the ChildViewHolder, used in onCreateViewHolder().
         * @param itemView The rootview of the list_item.xml layout file
         */
        ChildViewHolder(Context context, View itemView, GradientDrawable gradientDrawable) {
            super(itemView);

            //Initialize the views
            mTitleText = itemView.findViewById(R.id.title);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mSportsImage = itemView.findViewById(R.id.sportsImage);

            mContext = context;
            mGradientDrawable = gradientDrawable;

            //Set the OnClickListener to the whole view
            itemView.setOnClickListener(this);
        }

        void bindTo(Child currentChild){
            //Populate the textviews with data
            mTitleText.setText(currentChild.getTitle());
            mInfoText.setText(Html.fromHtml(currentChild.getInfo()));

            //Get the current sport
            mCurrentChild = currentChild;



            //Load the images into the ImageView using the Glide library
            Glide.with(mContext).load(currentChild.
                    getImageResource()).placeholder(mGradientDrawable).into(mSportsImage);
        }

        @Override
        public void onClick(View view) {

            //Set up the detail intent
            Intent detailIntent = Child.starter(mContext,
                    mCurrentChild.getTitle(),
                    mCurrentChild.getDetail(),
                    mCurrentChild.getNews(),
                    mCurrentChild.getImageResource());


            //Start the detail activity
            mContext.startActivity(detailIntent);
        }
    }
}
