package cn.jungu009.mynews.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cn.jungu009.mynews.R;
import cn.jungu009.mynews.adapter.NewsListAdapter;
import cn.jungu009.mynews.dao.INewsDao;
import cn.jungu009.mynews.dao.INewsDaoImpl;
import cn.jungu009.mynews.model.News;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView favoriteList;
    private INewsDao newsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsDao = new INewsDaoImpl(this);
        setContentView(R.layout.activity_favorite);
        initToolbar();

        favoriteList = (RecyclerView)findViewById(R.id.favorite_list);

        new LoadFavoriteAsyncTask().execute();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(0xF00);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    class LoadFavoriteAsyncTask extends AsyncTask<Void, Void, Void> {
        List<News> allNews = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            Cursor newses = newsDao.queryAllNews();
            while(newses.moveToNext()) {
                News news = new News(newses);
                allNews.add(news);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            favoriteList.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this));
            RecyclerView.Adapter adapter = new NewsListAdapter(allNews, null, FavoriteActivity.this);
            favoriteList.setAdapter(adapter);
        }
    }

}
