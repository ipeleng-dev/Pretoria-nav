package com.poe.preotrianav;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.poe.preotrianav.models.bookmarkLandmark;

import java.util.ArrayList;

public class bottom_sheet extends BottomSheetDialogFragment{

    private RecyclerView recycle_bookmarks;
    private ArrayList<bookmarkLandmark> bl;
    private bottom_sheet.recycle_adapter adapter;
    private int icount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View  v = inflater.inflate(R.layout.bottom_sheet_display,container,false);

          recycle_bookmarks = v.findViewById(R.id.recycle_bookmarks);
          bl = new ArrayList<>();

          bookmarkLandmark bl1 = new bookmarkLandmark("Union Buildings","");

          bl.add(bl1);
          bl.add(new bookmarkLandmark("Acadia",""));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new recycle_adapter(getContext(),bl);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recycle_bookmarks.setLayoutManager(manager);
        recycle_bookmarks.setHasFixedSize(true);

        recycle_bookmarks.setAdapter(adapter);
    }

    public class recycle_adapter  extends RecyclerView.Adapter<bottom_sheet.recycle_adapter.MyViewHolder> {

        private Context context;
        private ArrayList<bookmarkLandmark> bl;


        public recycle_adapter(Context context, ArrayList<bookmarkLandmark> bl){

            this.context = context;
            this.bl = bl;
        }

        @NonNull
        @Override
        public bottom_sheet.recycle_adapter.MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.recycle_bottom_sheet_item,parent,false);
            return  new bottom_sheet.recycle_adapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull bottom_sheet.recycle_adapter.MyViewHolder holder, int position) {
            //Add  the firebase storage reference here


            holder.title.setText(bl.get(position).getTitle());

            //Deal with the pictures here the commented code below is where u should add the pictures
//            holder.relativeLayout.setBackgroundResource(array_b_image.get(position));

            //Add some db logic that involves current user to save their preference
            holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(icount == 0){
                        holder.stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                        holder.stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        holder.stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        icount = 1;
                    }else{
                        holder.stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        holder.stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        holder.stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        icount = 0;
                    }

                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return bl.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder{

            RatingBar ratingBar;
            LayerDrawable stars;
            TextView title;
            RelativeLayout relativeLayout;
            public MyViewHolder(@NonNull View itemView) {

                super(itemView);
                relativeLayout = itemView.findViewById(R.id.relative_grid);
                title = itemView.findViewById(R.id.text_title_bookmark);
                ratingBar = itemView.findViewById(R.id.ratingBar);
                stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);


            }
        }


    }
}
