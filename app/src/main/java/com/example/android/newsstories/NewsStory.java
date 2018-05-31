package com.example.android.newsstories;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Greta Grigutė on 2018-05-10.
 */
public class NewsStory implements Parcelable {
    private String picture;
    private String category;
    private String title;
    private String author;
    private String date;
    private String url;
    private int starredValue;
    private boolean starred;

    public NewsStory(String Picture, String Category, String Title, String Author, String Date, String UrlWithDetails, boolean Starred) {
        picture = Picture;
        category = Category;
        title = Title;
        author = Author;
        date = Date;
        url = UrlWithDetails;
        if (Starred) {starredValue = 1;}
        else starredValue = 0;
    }

    protected NewsStory(Parcel in) {
        picture = in.readString();
        category = in.readString();
        title = in.readString();
        author = in.readString();
        date = in.readString();
        url = in.readString();
        starredValue = in.readInt();
        if (starredValue == 1) {starred = true;
        }else if(starredValue == 0) {starred = false;}
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picture);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(url);
        dest.writeInt(starredValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewsStory> CREATOR = new Creator<NewsStory>() {
        @Override
        public NewsStory createFromParcel(Parcel in) {
            return new NewsStory(in);
        }

        @Override
        public NewsStory[] newArray(int size) {
            return new NewsStory[size];
        }
    };

    public String getPicture() {
        return picture;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public boolean getStarred() {
        return starred;
    }
    public void setStarred (boolean Starred){
       starred = Starred;
    }
}
