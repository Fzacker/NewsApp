package com.java.fangzheng.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.java.fangzheng.Bean.NewsData;
import com.java.fangzheng.R;
import com.java.fangzheng.adapter.NewsAdapter;
import com.java.fangzheng.utils.MyDatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity {
    private List<NewsData> newsList = new ArrayList<>();

    private NewsAdapter newsAdapter;

    private MyDatabaseHelper dbHelper;
    private RecyclerView rv_history;
    private TextView tvHint;
    private ImageButton delete_btn,back_btn;

    @Override
    public int initView() {
        return R.layout.activity_history;
    }

    public HistoryActivity() {}

    @Override
    public void initData() {
        rv_history = findViewById(R.id.history_news);
        tvHint = findViewById(R.id.tvHint);
        delete_btn = findViewById(R.id.delete_btn);
        back_btn = findViewById(R.id.BackBtn);
        dbHelper = new MyDatabaseHelper(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_history.setLayoutManager(linearLayoutManager);

        newsAdapter = new NewsAdapter(this);
        rv_history.setAdapter(newsAdapter);
        initNews();
    }

    @Override
    public void initListener() {
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Serializable obj) {
                NewsData newsData = (NewsData) obj;
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsData", newsData);
                Intent intent = new Intent(HistoryActivity.this, NewsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("delete from History");
                initNews();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initNews() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from History", null);
        newsList.clear();
        if (cursor.getCount() != 0) {
            if (cursor.moveToLast()) {
                do {
                    //遍历Cursor对象，取出数据并打印
                    String news_title = cursor.getString(cursor.getColumnIndexOrThrow("news_title"));
                    String news_date = cursor.getString(cursor.getColumnIndexOrThrow("news_date"));
                    String news_author = cursor.getString(cursor.getColumnIndexOrThrow("news_publisher"));
                    String news_imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("news_imageUrl"));
                    String news_content = cursor.getString(cursor.getColumnIndexOrThrow("news_content"));
                    String news_video = cursor.getString(cursor.getColumnIndexOrThrow("news_videoUrl"));
                    NewsData news = new NewsData();
                    news.setTitle(news_title);
                    news.setPublishTime(news_date);
                    news.setPublisher(news_author);
                    news.setImage(news_imageUrl);
                    news.setContent(news_content);
                    news.setVideo(news_video);
                    newsList.add(news);
                } while (cursor.moveToPrevious());
            }
        } else {
            tvHint.setText("历史记录为空");
        }
        cursor.close();
        db.close();
        newsAdapter.setData(newsList);
        newsAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        initNews();
    }

}