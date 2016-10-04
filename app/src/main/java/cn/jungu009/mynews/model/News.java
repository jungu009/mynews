package cn.jungu009.mynews.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jungu009 on 2016/10/1.
 *
 */

public class News implements Parcelable {
    private String title;
    private String date;
    private String authorName;
    private List<String> thumbnailPicutres = new ArrayList<>();
    private String url;
    private String uniquekey;
    private String type;
    private String realType;

    public News() {
    }

    public News(JSONObject news) throws JSONException {
        this.title = news.getString("title");
        this.date = news.getString("date");
        this.authorName = news.getString("author_name");
        this.thumbnailPicutres.add(news.getString("thumbnail_pic_s"));
//        this.thumbnailPicutres.add(news.getString("thumbnail_pic_s02"));
        this.thumbnailPicutres.add(news.getString("thumbnail_pic_s03"));
        this.url = news.getString("url");
        this.uniquekey = ""; //news.getString("uniquekey");
        this.type = ""; //news.getString("type");
        this.realType = ""; //news.getString("realtype");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<String> getThumbnailPicutres() {
        return thumbnailPicutres;
    }

    public void setThumbnailPicutres(List<String> thumbnailPicutres) {
        this.thumbnailPicutres = thumbnailPicutres;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealType() {
        return realType;
    }

    public void setRealType(String realType) {
        this.realType = realType;
    }

    public static final Parcelable.Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            News news = new News();
            news.setTitle(source.readString());
            news.setDate(source.readString());
            news.setAuthorName(source.readString());
            news.setThumbnailPicutres(source.readArrayList(ArrayList.class.getClassLoader()));
            news.setUrl(source.readString());
            news.setUniquekey(source.readString());
            news.setType(source.readString());
            news.setRealType(source.readString());
            return news;
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
        dest.writeString(getDate());
        dest.writeString(getAuthorName());
        dest.writeList(getThumbnailPicutres());
        dest.writeString(getUrl());
        dest.writeString(getUniquekey());
        dest.writeString(getType());
        dest.writeString(getRealType());
    }
}
