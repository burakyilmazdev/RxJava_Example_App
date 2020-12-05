package com.example.rxdeneme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.rxdeneme.Models.Album;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Album> albums = new ArrayList<>();
    private MainActivity mainActivity;
    private Context context;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        context = holder.itemView.getContext();
        holder.bind(albums.get(position), position);
    }

    @Override
    public int getItemCount() {
        System.out.println("!!!!!!!"+albums.size()+"!!!!!!!!!!!!");
        return albums.size();
    }

    public void setAlbums(List<Album> albums){
        this.albums = albums;
    }

    public void updatePost(Album album){
        albums.set(albums.indexOf(album), album);
        notifyItemChanged(albums.indexOf(album));
    }

    public List<Album> getAlbums(){
        return albums;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.circle_image);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }

        public void bind(Album album, final int position){
            title.setText(album.getTitle());

            if(album.getPhotos() == null){
                showProgressBar(true);

            }
            else{
                showProgressBar(false);
                String ua=new WebView(context).getSettings().getUserAgentString();
                GlideUrl url = new GlideUrl(album.getPhotos().get(position).getUrl(), new LazyHeaders.Builder()
                        .addHeader("User-Agent", ua)
                        .build());


                Glide.with(context)
                        .asBitmap()
                        .override(200,200)
                        .load(url)
                        .into(imageView);
            }
        }

        private void showProgressBar(boolean showProgressBar){
            if(showProgressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            else{
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}