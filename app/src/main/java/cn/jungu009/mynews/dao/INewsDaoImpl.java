package cn.jungu009.mynews.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import cn.jungu009.mynews.model.News;

/**
 * Created by jungu009 on 2016/10/4.
 *
 */

public class INewsDaoImpl implements INewsDao {
    private NewsOpenHelper helper;

    public INewsDaoImpl(Context context) {
        this.helper = NewsOpenHelper.getHelper(context);
    }

    @Override
    public long addNews(News news) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsOpenHelper.TITLE, news.getTitle());
        contentValues.put(NewsOpenHelper.DATE, news.getDate());
        contentValues.put(NewsOpenHelper.AUTHORNAME, news.getAuthorName());
        contentValues.put(NewsOpenHelper.PICTUREURL, news.getThumbnailPicutres().get(0));
        contentValues.put(NewsOpenHelper.URL, news.getUrl());
        return helper.getWritableDatabase().insert(NewsOpenHelper.TABLENAME, null, contentValues);
    }

    @Override
    public void delNews(long id) {
        helper.getWritableDatabase().delete(NewsOpenHelper.TABLENAME,
                NewsOpenHelper.ID, new String[]{id + ""});
    }

    @Override
    public void updateNews(News news) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsOpenHelper.TITLE, news.getTitle());
        contentValues.put(NewsOpenHelper.DATE, news.getDate());
        contentValues.put(NewsOpenHelper.AUTHORNAME, news.getAuthorName());
        contentValues.put(NewsOpenHelper.PICTUREURL, news.getThumbnailPicutres().get(0));
        contentValues.put(NewsOpenHelper.URL, news.getUrl());
        helper.getWritableDatabase().update(NewsOpenHelper.TABLENAME, contentValues,
                NewsOpenHelper.ID, new String[]{news.getId()+""});
    }

    @Override
    public Cursor queryAllNews() {
        return helper.getReadableDatabase().query(NewsOpenHelper.TABLENAME, null, null, null, null, null, null);
    }
}
