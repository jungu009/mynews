package cn.jungu009.mynews;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final String FIRST = "first";
    private static final String CATEGORY = "category";

    private DrawerLayout mDrawerLayout;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<News> newses = new ArrayList<>();
    private INewsDao newsDao;
    private LinearLayout newsList;
    private TabLayout tabLayout;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initStatusBar();
        setContentView(R.layout.drawer_main);
        initToolbar();
        initTabs();
        initNavigate();

        newsList = (LinearLayout)findViewById(R.id.news_list);

        newsDao = new INewsDaoImpl(this);
        getNews(TOPURL);

    }

    // 第一次启动程序时 初始化数据
    private void init() {
        settings = getPreferences(MODE_PRIVATE);
        boolean isFirst = settings.getBoolean(FIRST, true);
        if(isFirst) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(FIRST, false);
            Set set = new HashSet<String>();
            set.addAll(Arrays.asList(getResources().getStringArray(R.array.category)));
            editor.putStringSet(CATEGORY, set);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT, true);
                break;
            case R.id.item_catogray_manage:
                Toast.makeText(MainActivity.this, "分类管理", Toast.LENGTH_LONG).show();

                break;
            default:
                break;
        }
        return true;
    }

    private void setTabsText() {
        Set set = settings.getStringSet(CATEGORY, null);
        if(set != null){
            List<String> categories = new ArrayList<>(set);
            Collections.sort(categories, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    // TODO tabs排序
                    return 0;
                }
            });
            for(int i = 0; i < categories.size(); i++) {
                tabLayout.addTab(tabLayout.newTab().setText(categories.get(i)));
            }
        }
    }

    private void initTabs() {
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        setTabsText();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String text = tab.getText().toString();
                if("头条".equals(text)) {
                    navigateEvent("头条", TOPURL);
                } else if("社会".equals(text)) {
                    navigateEvent("社会", SHEHUIURL);
                } else if("国际".equals(text)) {
                    navigateEvent("国际", GUOJIURL);
                } else if("国内".equals(text)) {
                    navigateEvent("国内", GUONEIURL);
                } else if("娱乐".equals(text)) {
                    navigateEvent("娱乐", YULEURL);
                } else if("体育".equals(text)) {
                    navigateEvent("体育", TIYUURL);
                } else if("军事".equals(text)) {
                    navigateEvent("军事", JUNSHIURL);
                } else if("科技".equals(text)) {
                    navigateEvent("科技", KEJIURL);
                } else if("财经".equals(text)) {
                    navigateEvent("财经", CAIJINGURL);
                } else if("时尚".equals(text)) {
                    navigateEvent("时尚", SHISHANGURL);
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

    private void initNavigate() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = (NavigationView)findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_myfavorite:
                        navigateEvent("收藏", FAVOURIT);
                        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
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
                view.setElevation(4);
                view.setOnClickListener(MainActivity.this);
                newsList.addView(view, i);

            }

        }
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_menu_black_24dp);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
