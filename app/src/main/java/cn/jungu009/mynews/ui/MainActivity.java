package cn.jungu009.mynews.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jungu009.mynews.R;
import cn.jungu009.mynews.adapter.NewsPagerAdapter;
import cn.jungu009.mynews.dao.INewsDao;
import cn.jungu009.mynews.dao.INewsDaoImpl;
import cn.jungu009.mynews.fragments.CategoryFragment;
import cn.jungu009.mynews.utils.ViewPagerUtils;

public class MainActivity extends AppCompatActivity {

    private static final String FIRST = "first";
    private static final String CATEGORY = "category";

    private static final int REQUESTCODE = 9111;

    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SharedPreferences settings;
    private List<String> categories;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initStatusBar();
        setContentView(R.layout.drawer_main);
        initToolbar();
        initTabs();
        initNavigate();
        initViewPager();
    }

    // 第一次启动程序时 初始化数据
    private void init() {
        settings = getPreferences(MODE_PRIVATE);
        boolean isFirst = settings.getBoolean(FIRST, true);
        if(isFirst) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(FIRST, false);
            Set<String> set = new HashSet(Arrays.asList(getResources().getStringArray(R.array.category)));
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
//                Intent category = new Intent(MainActivity.this, CategoryActivity.class);
//                category.putStringArrayListExtra("categories", (ArrayList<String>) categories);
//                startActivityForResult(category, REQUESTCODE);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == REQUESTCODE && resultCode == RESULT_OK) {
//            SharedPreferences.Editor editor = settings.edit();
//            Set set = new HashSet(data.getStringArrayListExtra("categroies"));
//            categories = new ArrayList<>(set);
//            editor.putStringSet(CATEGORY, set);
//            editor.apply();
//        }
    }

    private void setTabsText() {
        for(int i = 0; i < categories.size(); i++) {
            tabLayout.getTabAt(i).setText(categories.get(i));
        }
    }

    private void rewordCategory() {
        Set set = settings.getStringSet(CATEGORY, null);
        if(set != null){
            categories = new ArrayList<>(set);
            // 排序 将头条固定在第一个
            Collections.sort(categories, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if(o1.equals("头条")) {
                        return -1;
                    }
                    if(o2.equals("头条")) {
                        return 1;
                    }
                    return o2.compareTo(o1);
                }
            });
        }
    }

    private void initTabs() {
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        rewordCategory();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

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
                        Toast.makeText(MainActivity.this, "收藏", Toast.LENGTH_LONG).show();
                        Intent favorite = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(favorite);
                        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private void initViewPager() {
        for(int i = 0; i < categories.size(); i++) {
            CategoryFragment fragment = CategoryFragment.newInstance(categories.get(i), ViewPagerUtils.whichUrl(categories.get(i)));
            fragments.add(fragment);
        }
        viewPager = (ViewPager)findViewById(R.id.view_page);
//        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new NewsPagerAdapter(getSupportFragmentManager(), fragments));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this, "page selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0, false);
        tabLayout.setupWithViewPager(viewPager);  // 关联ViewPager 和 tablayout
        setTabsText();
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
