package cn.jungu009.mynews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
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

import cn.jungu009.mynews.model.News;

public class MainActivity extends AppCompatActivity {

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

    private NewsAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView newsList;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<News> newses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeAlphaOfStatusBar();
        setContentView(R.layout.activity_main);
        initActionBar();
        moveDrawerToTop();
        initNavigate();

        newsList = (ListView)findViewById(R.id.list_item);

        getNews(TOPURL);

    }

    /*
     * ToDo
     * 导航栏选项的功能
     */
    private void initNavigate() {
        NavigationView mNavigationView = (NavigationView) mDrawerLayout.findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_toutiao:
                        navigateEvent("头条", TOPURL);
                        break;
                    case R.id.item_shehui:
                        navigateEvent("社会", SHEHUIURL);
                        break;
                    case R.id.item_guonei:
                        navigateEvent("国内", GUONEIURL);
                        break;
                    case R.id.item_guoji:
                        navigateEvent("国际", GUOJIURL);
                        break;
                    case R.id.item_yule:
                        navigateEvent("娱乐", YULEURL);
                        break;
                    case R.id.item_tiyu:
                        navigateEvent("体育", TIYUURL);
                        break;
                    case R.id.item_junshi:
                        navigateEvent("军事", JUNSHIURL);
                        break;
                    case R.id.item_keji:
                        navigateEvent("科技", KEJIURL);
                        break;
                    case R.id.item_caijing:
                        navigateEvent("财经", CAIJINGURL);
                        break;
                    case R.id.item_shishang:
                        navigateEvent("时尚", SHISHANGURL);
                        break;
                    case R.id.item_myfavorite:
                        Toast.makeText(MainActivity.this, "收藏", Toast.LENGTH_LONG).show();
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
        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
    }

    class NewsAdapter extends BaseAdapter {
        List<News> newses;

        NewsAdapter(List<News> newses) {
            this.newses = newses;
        }

        @Override
        public int getCount() {
            return newses.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.news_list_item, null, false);
            }
            final ImageView img = (ImageView)view.findViewById(R.id.img);
            TextView title = (TextView)view.findViewById(R.id.title);
            TextView date = (TextView)view.findViewById(R.id.date);
            News news = newses.get(position);
            if(bitmaps.size() > position)
                img.setImageBitmap(bitmaps.get(position));
            title.setText(news.getTitle());
            date.setText(news.getDate());
            return view;
        }
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
            /*
                ToDo
                error_code 的处理
             */
            JSONObject result = json.getJSONObject("result");
            JSONArray array = result.getJSONArray("data");
            for(int i = 0; i < array.length(); i++) {
                newses.add(new News(array.getJSONObject(i)));
            }
        }

        @Override
        protected String doInBackground(String ... params) {
            HttpURLConnection conn = null;

            try {
                URL url = new URL(params[0]);
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
            return null;
        }

        @Override
        protected void onPostExecute(String str) {

            newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detail = new Intent(MainActivity.this, DetailActivity.class);
                    detail.putExtra("news", newses.get(position));
                    startActivity(detail);
                }
            });

            if(mAdapter == null) {
                mAdapter = new NewsAdapter(newses);
                newsList.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
                newsList.deferNotifyDataSetChanged();
            }
        }
    }

    private void initActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if(mActionBar != null) {
            mActionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_add);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    private void changeAlphaOfStatusBar() {
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

    private void moveDrawerToTop() {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer_main, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        FrameLayout container = (FrameLayout) mDrawerLayout.findViewById(R.id.drawer_content); // This is the container we defined just now.
        container.addView(child, 0);
        mDrawerLayout.findViewById(R.id.drawer_layout).setPadding(0, 0, 0, 0);

        // Make the drawer replace the first child
        decor.addView(mDrawerLayout);
    }

}
