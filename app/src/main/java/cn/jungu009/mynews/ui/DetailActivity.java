package cn.jungu009.mynews.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.jungu009.mynews.R;
import cn.jungu009.mynews.dao.INewsDao;
import cn.jungu009.mynews.dao.INewsDaoImpl;
import cn.jungu009.mynews.model.News;
import cn.jungu009.mynews.utils.WXUtil;

/**
 * 网页详情
 */
public class DetailActivity extends AppCompatActivity {

    private INewsDao newsDao;
    private WebView mWebView;
    private News news;
    private IWXAPI api;
    private static final String APPID = "wx0a8c963910e5e8b6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APPID, false);
        api.registerApp(APPID);

        setContentView(R.layout.activity_detail);
        initToolbar();
        newsDao = new INewsDaoImpl(this);

        news = getIntent().getParcelableExtra("news");
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
                Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
                newsDao.addNews(news);
                break;
            case R.id.item_share:
                shareWebpage(news);
                break;
            default:
                break;
        }
        return true;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void shareWebpage(News news) {
        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = news.getUrl();
        webpage.webpageUrl = "http://www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = news.getTitle();
        msg.description = "新闻描述";
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon); // 微信对图片是有要求的 这个图片不过关
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_btn_speak_now);
        msg.thumbData = WXUtil.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
