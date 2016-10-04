package cn.jungu009.mynews.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

/**
 * Created by jungu009 on 2016/10/4.
 *
 */

public final class NewsOpenHelper extends SQLiteOpenHelper{

    private static NewsOpenHelper helper;
    public static final int VERSION = 1;
    private static final String NAME = "news.db";
    public static final String TABLENAME = "news";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String AUTHORNAME = "author_name";
    public static final String PICTUREURL = "picture_url";
    public static final String URL = "url";
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLENAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT, " +
                    DATE + " TEXT, " +
                    AUTHORNAME + " TEXT, " +
                    PICTUREURL + " TEXT, " +
                    URL + " TEXT" +
                    ")";


    private NewsOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    public static NewsOpenHelper getHelper(Context context) {
        if(helper == null) {
            helper = new NewsOpenHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
//            db.execSQL("");
        }
    }
}
