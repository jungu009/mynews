package cn.jungu009.mynews;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jungu009.mynews.dao.INewsDao;
import cn.jungu009.mynews.dao.INewsDaoImpl;
import cn.jungu009.mynews.model.News;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String ADDR = "http://v.juhe.cn/toutiao/index?type=";
    private static final String KEY = "&key=bb12536c113e279e1f70735e54019196";
    private static final String TOPURL = ADDR + KEY;
    private static final String SHEHUIURL = ADDR + "shehui" + KEY;
    private static final String GUONEIURL = ADDR + "guonei" + KEY;
    private static final String GUOJIURL = ADDR + "guoji" + KEY;
    private static final String YULEURL = ADDR + "yule" + KEY;
    private static final String TIYUURL = ADDR + "tiyu" + KEY;
    private static final String JUNSHIURL = ADDR + "junshi" + KEY;
    private static final String KEJIURL = ADDR + "keji" + KEY;
    private static final String CAIJINGURL = ADDR + "caijing" + KEY;
    private static final String SHISHANGURL = ADDR + "shishang" + KEY;
    private static final String FAVOURIT = "favourit";

    private DrawerLayout mDrawerLayout;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<News> newses = new ArrayList<>();
    private INewsDao newsDao;
    private Toolbar mToolbar;
    private LinearLayout newsList;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.drawer_main);
        initToolbar();
        initTabs();
        initNavigate();

        newsList = (LinearLayout)findViewById(R.id.news_list);

        newsDao = new INewsDaoImpl(this);
        getNews(TOPURL);

    }

    private void initTabs() {
        // TODO 优化Tabs
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("头条"));
        tabLayout.addTab(tabLayout.newTab().setText("社会"));
        tabLayout.addTab(tabLayout.newTab().setText("国内"));
        tabLayout.addTab(tabLayout.newTab().setText("国际"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        navigateEvent("头条", TOPURL);
                        break;
                    case 1:
                        navigateEvent("社会", SHEHUIURL);
                        break;
                    case 2:
                        navigateEvent("国内", GUONEIURL);
                        break;
                    case 3:
                        navigateEvent("国际", GUOJIURL);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "tab error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*
     * 导航栏选项的功能
     */
    private void initNavigate() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = (NavigationView)findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//////                    case R.id.item_toutiao:
//////                        navigateEvent("头条", TOPURL);
//////                        break;
//////                    case R.id.item_shehui:
//////                        navigateEvent("社会", SHEHUIURL);
//////                        break;
//////                    case R.id.item_guonei:
//////                        navigateEvent("国内", GUONEIURL);
//////                        break;
//////                    case R.id.item_guoji:
//////                        navigateEvent("国际", GUOJIURL);
//////                        break;
//////                    case R.id.item_yule:
//////                        navigateEvent("娱乐", YULEURL);
//////                        break;
//////                    case R.id.item_tiyu:
//////                        navigateEvent("体育", TIYUURL);
//////                        break;
//////                    case R.id.item_junshi:
//////                        navigateEvent("军事", JUNSHIURL);
//////                        break;
//////                    case R.id.item_keji:
//////                        navigateEvent("科技", KEJIURL);
//////                        break;
//////                    case R.id.item_caijing:
//////                        navigateEvent("财经", CAIJINGURL);
//////                        break;
//////                    case R.id.item_shishang:
//////                        navigateEvent("时尚", SHISHANGURL);
//////                        break;
                    case R.id.item_myfavorite:
                        navigateEvent("收藏", FAVOURIT);
                        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
                        // TODO 要让Tabs隐藏 出去tab的选择。。。
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private void getNews(String url) {
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(url);
    }

    private void navigateEvent(String msg, String url) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
        getNews(url);
        setTitle(msg);
//        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
    }

    private void loadNewsBitmap(List<News> newses) {
        bitmaps = new ArrayList<>();
//        bitmaps.removeAll(bitmaps);
        for(News news : newses) {
            String uri = news.getThumbnailPicutres().get(0);
            InputStream is;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(uri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(6*1000);
                if(conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        int position = v.getId();
        // TODO newses集合的判断
        Intent detail = new Intent(this, DetailActivity.class);
        detail.putExtra("news", newses.get(position));
        startActivity(detail);
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {

        private String readStream(InputStream is) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String body = null;
            StringBuilder builder = new StringBuilder("");
            try {
                while((body = br.readLine()) != null) {
                    builder.append(body);
                }
                body = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }

        private void readJson(String str) throws JSONException{
            newses.removeAll(newses);
            JSONObject json  = new JSONObject(str);
            // TODO error_code 的处理
            JSONObject result = json.getJSONObject("result");
            JSONArray array = result.getJSONArray("data");
            for(int i = 0; i < array.length(); i++) {
                newses.add(new News(array.getJSONObject(i)));
            }
        }

        private void loadNews(String uri) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(uri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(6*1000);
                if(conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    readJson(readStream(is));

                    loadNewsBitmap(newses);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if(conn != null)
                    conn.disconnect();
            }
        }

        private void loadFavourit() {
            Cursor cursor = newsDao.queryAllNews();
            newses.removeAll(newses);

            for(int i = 0; i < cursor.getCount(); i++) {
                if(cursor.moveToNext()){
                    newses.add(new News(cursor));
                    loadNewsBitmap(newses);
                }else {
                    break;
                }
            }

        }

        @Override
        protected String doInBackground(String ... params) {
            String param = params[0];
            if(!FAVOURIT.equals(param)){
                loadNews(param);
            } else {
                loadFavourit();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            newsList.removeAllViews();
            // TODO 不要使用循环 内存溢出OOM 要对加载的新闻做限制不能一次全加载
            for(int i = 0; i < newses.size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.news_list_item, null, false);
                view.setId(i);
                ImageView img = (ImageView)view.findViewById(R.id.img);
                TextView title = (TextView)view.findViewById(R.id.title);
                TextView date = (TextView)view.findViewById(R.id.date);
                News news = newses.get(i);
                if(bitmaps.size() > i)
                    img.setImageBitmap(bitmaps.get(i));
                title.setText(news.getTitle());
                date.setText(news.getDate());

                view.setOnClickListener(MainActivity.this);
                newsList.addView(view, i);

            }

        }
    }

    private void initToolbar() {
        // TODO 美化toolbar 添加menu管理用户的喜好 增删tab中的选项
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT, true);
                break;
            default:
                break;
        }
        return true;
    }

    private void initStatusBar() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

}
