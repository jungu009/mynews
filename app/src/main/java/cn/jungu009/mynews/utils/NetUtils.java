package cn.jungu009.mynews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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

/**
 * Created by jungu009 on 2016/10/11.
 *
 */

public final class NetUtils {

    private NetUtils(){}

    public static void loadNewsBitmap(List<News> newses, List<Bitmap> bitmaps) {
        bitmaps.clear();
        for(int i = 0; i < newses.size(); i++) {
            News news = newses.get(i);
            String uri = news.getThumbnailPicutres().get(0);
            InputStream is;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(uri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(true);
                conn.setRequestMethod("GET");
                conn.setReadTimeout(6*1000);
                if(conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                    // TODO 图片的缩放比例 需要判断
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 7;  // TODO 设置的越大好像没用
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

    public static String readStream(InputStream is) {
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

    public static void readJson(InputStream is, List list) throws JSONException {
        list.clear();
        String str = readStream(is);
        JSONObject json  = new JSONObject(str);
        JSONObject result = json.getJSONObject("result");
        JSONArray array = result.getJSONArray("data");
        for(int i = 0; i < array.length(); i++) {
            list.add(new News(array.getJSONObject(i)));
        }
    }

//    public static List readJson(String str) throws JSONException {
//        List list = new ArrayList();
//        JSONObject json  = new JSONObject(str);
//        JSONObject result = json.getJSONObject("result");
//        JSONArray array = result.getJSONArray("data");
//        for(int i = 0; i < array.length(); i++) {
//            list.add(array.getJSONObject(i));
//        }
//        return list;
//    }


    public static void loadNews(String uri, List<News> newses) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(6*1000);
            if(conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                readJson(is, newses);
            }else {
                // TODO error_code 的处理
                Log.i("myNews", "conn error");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }
    }

}
