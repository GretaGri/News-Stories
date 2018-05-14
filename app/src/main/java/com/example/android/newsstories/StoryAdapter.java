package com.example.android.newsstories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Greta Grigutė on 2018-05-10.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder>{
private List<NewsStory> newsStories;
private LayoutInflater inflater;
private Context context;

    public StoryAdapter(Context context,List<NewsStory> newsStories) {
        this.newsStories = newsStories;
        inflater = LayoutInflater.from(context);
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewsStory story = newsStories.get(position);
        holder.title.setText(story.getTitle());
        holder.author.setText(story.getAuthor());
        if(story.getPicture().equals(context.getString(R.string.no_picture))){
            holder.picture.setImageResource(R.drawable.no_picture_available);
        }
        else{
        Picasso.get().load(story.getPicture()).into(holder.picture);
        }
        holder.date.setText(story.getDate());
        holder.category.setText(story.getCategory());
        Log.d("StoryAdapter","Data set");
    }

    @Override
    public int getItemCount() {
        return newsStories.size();
    }

    public NewsStory getItem(int position) {
        return newsStories.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public ImageView picture;
        public TextView date;
        public Button category;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.story_title);
            author = view.findViewById(R.id.story_author);
            picture = view.findViewById(R.id.story_image);
            date = view.findViewById(R.id.story_date);
            category = view.findViewById(R.id.category);
        }
    }
    public void setNews(List<NewsStory> data) {
        newsStories.addAll(data);
        notifyDataSetChanged();
    }
}

