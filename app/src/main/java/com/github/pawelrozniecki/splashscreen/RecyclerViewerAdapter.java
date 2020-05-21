package com.github.pawelrozniecki.splashscreen;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewerAdapter  extends RecyclerView.Adapter<RecyclerViewerAdapter.ViewHolder> {

ArrayList<String> jsonData = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();



    RecyclerViewerAdapter(ArrayList<String> jsonData , ArrayList<String> images,ArrayList<String> desc){
        this.jsonData = jsonData;
        this.images = images;
        this.desc = desc;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_viewer_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView imageView =  holder.image;
        String currentUrl = images.get(position);


        holder.title.setText(jsonData.get(position));
        holder.description.setText(desc.get(position));



        Context context = holder.image.getContext();

        Glide.with(context)
                .load(currentUrl)
                        .into(imageView);
    }

    @Override
    public int getItemCount() {

        return  jsonData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        Button readMore;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           title =  itemView.findViewById(R.id.newsTitle);
           description = itemView.findViewById(R.id.description);
           image = itemView.findViewById(R.id.image);

           readMore = itemView.findViewById(R.id.readmore);




        }
    }
}