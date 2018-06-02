package com.example.android.newsstories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greta Grigutė on 2018-05-10.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {
    private List<NewsStory> newsStories;
    private LayoutInflater inflater;
    private Context context;
    private final ListItemClickListener itemClickListener;

    public StoryAdapter(Context context, List<NewsStory> newsStories, ListItemClickListener listener) {
        this.newsStories = newsStories;
        inflater = LayoutInflater.from(context);
        this.context = context;
        itemClickListener = listener;

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
        if (story.getAuthor().equals(Constants.NO_AUTHOR)){
            holder.author.setText(R.string.author_unknown);
        } else {
            holder.author.setText(story.getAuthor());
        }
        if (story.getPicture().equals(Constants.NO_PICTURE)) {
            holder.picture.setImageResource(R.drawable.no_picture_available);
        } else {
            Picasso.get().load(story.getPicture()).into(holder.picture);
        }
        holder.date.setText(story.getDate());
        holder.category.setText(story.getCategory());
        int categoryColor = getCategoryColor(story.getCategory());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.category.setBackgroundTintList(context.getResources().getColorStateList(categoryColor));// Do something for lollipop and above versions

            if (story.getStarred()){holder.favorite.setImageResource(R.drawable.yellow_star_full);
            }else holder.favorite.setImageResource(R.drawable.yellow_star_empty);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            //TODO: need to use array because I need all titles that were selected as starred saved. Also, do not forget to remove from favorites when deselected
            String currentTitle = sharedPreferences.getString("Title_key","");
            Boolean starred = sharedPreferences.getBoolean("FavoriteSelected_key", false);
            if (currentTitle.equals(story.getTitle())&&starred){holder.favorite.setImageResource(R.drawable.yellow_star_full);
            }else holder.favorite.setImageResource(R.drawable.yellow_star_empty);}
    }

    @Override
    public int getItemCount() {
        if (newsStories !=null){
        return newsStories.size();}
        else return 0;
    }

    public NewsStory getItem(int position) {
        return newsStories.get(position);
    }

    public void setNews(List<NewsStory> data) {
        newsStories.addAll(data);
        notifyDataSetChanged();
    }

    private int getCategoryColor(String category) {
        int categoryColor;
        switch (category) {
            case Constants.TECHNOLOGY:
                categoryColor = R.color.colorTechnology;
                break;
            case Constants.LAW:
                categoryColor = R.color.colorLaw;
                break;
            case Constants.SCIENCE:
                categoryColor = R.color.colorScience;
                break;
            case Constants.EDUCATION:
                categoryColor = R.color.colorEducation;
                break;
            case Constants.TRAVEL:
                categoryColor = R.color.colorTravel;
                break;
            case Constants.MUSIC:
                categoryColor = R.color.colorMusic;
                break;
            case Constants.ART_AND_DESIGN:
                categoryColor = R.color.colorArt;
                break;
            case Constants.FILM:
                categoryColor = R.color.colorFilm;
                break;
            case Constants.FASHION:
                categoryColor = R.color.colorFashion;
                break;
            case Constants.CROSSWORDS:
                categoryColor = R.color.colorCrosswords;
                break;
            default:
                categoryColor = R.color.colorPrimary;
                break;
        }
        return categoryColor;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView author;
        public ImageView picture;
        public TextView date;
        public AppCompatButton category;
        public ImageView favorite;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.story_title);
            author = view.findViewById(R.id.story_author);
            picture = view.findViewById(R.id.story_image);
            date = view.findViewById(R.id.story_date);
            category = view.findViewById(R.id.category);
            category.setOnClickListener(this);
            favorite =  view.findViewById(R.id.favorite);
            favorite.setOnClickListener(this);
            cardView = view.findViewById(R.id.cardview);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onListItemClick(v, getLayoutPosition());
        }
    }
    public interface ListItemClickListener {
        void onListItemClick(View view, int position);
    }
}

