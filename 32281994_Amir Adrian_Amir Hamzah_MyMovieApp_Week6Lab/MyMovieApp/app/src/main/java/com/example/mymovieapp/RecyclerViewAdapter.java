package com.example.mymovieapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<MovieClass> data;

    public RecyclerViewAdapter(ArrayList<MovieClass> data_in){
        data = data_in;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moviedb_cardview,parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.movieTitleCard.setText(data.get(position).getMovieTitle());
        holder.movieKeywordsCard.setText(data.get(position).getMovieKeywords());
        holder.movieCostCard.setText(data.get(position).getMovieCost() + "");
        holder.movieGenreCard.setText(data.get(position).getMovieGenre());
        holder.movieCountryCard.setText(data.get(position).getMovieCountry());
        holder.movieYearCard.setText(data.get(position).getMovieYear());


        final int fPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Toast.makeText(v.getContext(), "Movie No."+ (fPosition+1) +" Title: "+data.get(fPosition).getMovieTitle()+ " selected.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView movieTitleCard,movieYearCard,movieGenreCard,movieCostCard,movieCountryCard,movieKeywordsCard;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            movieTitleCard = itemView.findViewById(R.id.movieTitleCard);
            movieCostCard = itemView.findViewById(R.id.movieCostCard);
            movieCountryCard = itemView.findViewById(R.id.movieCountryCard);
            movieGenreCard = itemView.findViewById(R.id.movieGenreCard);
            movieYearCard = itemView.findViewById(R.id.movieYearCard);
            movieKeywordsCard = itemView.findViewById(R.id.movieKeywordsCard);
        }
    }

}
