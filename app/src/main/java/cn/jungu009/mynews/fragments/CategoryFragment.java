package cn.jungu009.mynews.fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jungu009.mynews.R;
import cn.jungu009.mynews.adapter.NewsListAdapter;
import cn.jungu009.mynews.model.News;
import cn.jungu009.mynews.utils.NetUtils;


/**
 * Created by jungu009 on 2016/10/8.
 * viewpager 的内容
 */

public final class CategoryFragment extends Fragment {

    private static final String CATEGORY = "category";
    private static final String URL = "url";

    private List<Bitmap> bitmaps = Collections.synchronizedList(new ArrayList<Bitmap>());
    private List<News> newses = Collections.synchronizedList(new ArrayList<News>());
    private String category;
    private String url;
    private RecyclerView recyclerView;
    private long updateTime;

    public static CategoryFragment newInstance(String category, String url) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, category);
        bundle.putString(URL, url);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.url = getArguments().getString(URL);
        this.category = getArguments().getString(CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("myNews", "createView");
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_layout, container, false);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("myNews", "ActivityCreated");
        loadNews(url);
    }

    public void loadNews(String url) {
        Log.i("myNews", "loadNews");
        new LoadNewsAsyncTask().execute(url);
    }

    public void clearNews() {
        newses.clear();
        bitmaps.clear();
        for(int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            bitmap.recycle();  // 回收C中的图片内存
        }
        recyclerView.removeAllViews();
    }

    class LoadNewsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String ... params) {
            String param = params[0];
            // 控制更新频率
            long time = System.currentTimeMillis() - updateTime;
            if(time > 30000){ // 第一次肯定大于30秒
                updateTime = System.currentTimeMillis();
                NetUtils.loadNews(param, newses);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            RecyclerView.Adapter adapter = new NewsListAdapter(newses, bitmaps, getContext());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("myNews", "fragment destroy " + url);
//        clearNews();
    }
}
