package cn.jungu009.mynews;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import cn.jungu009.mynews.model.News;

public class DetailActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initActionBar();
        News news = getIntent().getParcelableExtra("news");
        setTitle(news.getTitle());

        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.loadUrl(news.getUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.item_favourite:
                Toast.makeText(this, "收藏", Toast.LENGTH_LONG).show();
                break;
            case R.id.item_share:
                Toast.makeText(this, "分享", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void initActionBar() {
        mActionBar = getSupportActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
