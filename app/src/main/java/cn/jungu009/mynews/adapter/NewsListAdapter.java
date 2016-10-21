package cn.jungu009.mynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.jungu009.mynews.ui.DetailActivity;
import cn.jungu009.mynews.R;
import cn.jungu009.mynews.model.News;

/**
 * Created by jungu009 on 2016/10/8.
 *
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {
    private List<News> newsList;
    private List<Bitmap> bitmaps;
    private Context context;

    public NewsListAdapter(List<News> newsList, List<Bitmap> bitmaps, Context context) {
        this.newsList = newsList;
        this.bitmaps = bitmaps;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            News news = newsList.get(position);
//            holder.img.setImageBitmap(bitmaps.get(position));
            holder.title.setText(news.getTitle());
            holder.date.setText(news.getDate());
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        TextView title, date;

        MyViewHolder(View view) {
            super(view);
            img = (ImageView)view.findViewById(R.id.img);
            title = (TextView)view.findViewById(R.id.title);
            date = (TextView)view.findViewById(R.id.date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent detail = new Intent(context, DetailActivity.class);
            detail.putExtra("news", newsList.get(getAdapterPosition()));
            context.startActivity(detail);
        }
    }

}
