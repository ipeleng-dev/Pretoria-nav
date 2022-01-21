package com.poe.preotrianav;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.poe.preotrianav.models.bookmarkLandmark;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class bookmarked_fragment extends Fragment {


    private RecyclerView recycle_bookmarked;
    private ArrayList<bookmarkLandmark> bl;
    private recycle_adapter adapter_bookmarked;
    private int icount =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bookmarked, container, false);


        recycle_bookmarked = (RecyclerView) v.findViewById(R.id.recycle_bookmarked);

        bl = new ArrayList<>();

        bookmarkLandmark bl1 = new bookmarkLandmark("Union Buildings","");

        bl.add(bl1);
        bl.add(new bookmarkLandmark("Acadia",""));

        adapter_bookmarked = new recycle_adapter(getContext(),bl);
        recycle_bookmarked.setLayoutManager(new LinearLayoutManager(getActivity()));

        recycle_bookmarked.setAdapter(adapter_bookmarked);

        return v;
    }

    public class recycle_adapter  extends RecyclerView.Adapter<recycle_adapter.MyViewHolder> {

        private Context context;
        private ArrayList<bookmarkLandmark> bl;


        public recycle_adapter(Context context, ArrayList<bookmarkLandmark> bl){

            this.context = context;
            this.bl = bl;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.grid_bookmarked,parent,false);
            return  new MyViewHolder(v);
        }

        @NonNull
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

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

            ImageView image_books;
            RatingBar ratingBar;
            LayerDrawable stars;
            TextView title;
            RelativeLayout relativeLayout;
            public MyViewHolder(@NonNull View itemView) {

                super(itemView);

                relativeLayout = itemView.findViewById(R.id.relative_viewBookmark);
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