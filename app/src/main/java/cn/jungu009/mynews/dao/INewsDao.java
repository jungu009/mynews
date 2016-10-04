package cn.jungu009.mynews.dao;

import android.database.Cursor;

import cn.jungu009.mynews.model.News;

/**
 * Created by jungu009 on 2016/10/4.
 *
 */

public interface INewsDao {
    public long addNews(News news);
    public void delNews(long id);
    public void updateNews(News news);
    public Cursor queryAllNews();
}
