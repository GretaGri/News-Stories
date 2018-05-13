package com.example.android.newsstories;

/**
 * Created by Greta Grigutė on 2018-05-10.
 */
public class NewsStory {
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
