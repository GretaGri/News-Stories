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

    public NewsStory(String Picture, String Category, String Title, String Author, String Date, String UrlWithDetails) {
        picture = Picture;
        category = Category;
        title = Title;
        author = Author;
        date = Date;
        url = UrlWithDetails;
    }

    protected NewsStory(Parcel in) {
        picture = in.readString();
        category = in.readString();
        title = in.readString();
        author = in.readString();
        date = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picture);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(url);
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
}
