package com.java.fangzheng.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.java.fangzheng.R;
import com.java.fangzheng.Bean.NewsData;
import com.java.fangzheng.utils.MyDatabaseHelper;
import com.java.fangzheng.utils.StatusBarUtils;

public class NewsActivity extends AppCompatActivity {
    private NewsData newsData;
    private TextView newsTitle;
    private TextView newsContent;
    private TextView newsDate;
    private TextView newsPublisher;
    private ImageView newsImage;
    private ImageButton backBtn,collectBtn;
    private MyDatabaseHelper dbHelper;
    private VideoView videoPlayer;
    Boolean isCollected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.transparent);
        Bundle bundle = getIntent().getExtras();
        newsData = (NewsData) bundle.getSerializable("newsData");
        dbHelper = new MyDatabaseHelper(this);
        isCollected = getIsCollected(newsData.getTitle());
        if (newsData.getVideo()==null)
            newsData.setVideo("");
        if (newsData.getVideo().equals("")) {
            setContentView(R.layout.activity_news);
            initImageData();
            initListener();
        } else {
            setContentView(R.layout.activity_news_video);
            initVideoData();
            initListener();
        }
    }

    public void initImageData() {
        newsContent  = findViewById(R.id.newsContent);
        newsDate = findViewById(R.id.newsDate);
        newsPublisher = findViewById(R.id.newsPublisher);
        newsTitle = findViewById(R.id.newsTitle);
        newsImage = findViewById(R.id.newsImage);
        backBtn = findViewById(R.id.newsBackBtn);
        collectBtn = findViewById(R.id.collectButton);

        newsTitle.setText(newsData.getTitle());
        newsDate.setText(newsData.getPublishTime());
        newsPublisher.setText(newsData.getPublisher());
        if(isCollected) {
            collectBtn.setBackground(getResources().getDrawable(R.drawable.collected));
        } else {
            collectBtn.setBackground(getResources().getDrawable(R.drawable.collect));
        }
        insertHistoryNews(newsData);
    }

    public void initVideoData() {
        newsContent  = findViewById(R.id.newsContent_video);
        newsDate = findViewById(R.id.newsDate_video);
        newsPublisher = findViewById(R.id.newsPublisher_video);
        newsTitle = findViewById(R.id.newsTitle_video);
        backBtn = findViewById(R.id.newsBackBtn_video);
        collectBtn = findViewById(R.id.collectButton_video);
        videoPlayer = findViewById(R.id.video_item);

        newsTitle.setText(newsData.getTitle());
        newsDate.setText(newsData.getPublishTime());
        newsPublisher.setText(newsData.getPublisher());

        videoPlayer.setVideoURI(Uri.parse(newsData.getVideo()));
        MediaController mc =  new MediaController(this);
        videoPlayer.setMediaController(mc);
        mc.setMediaPlayer(videoPlayer);
        videoPlayer.start();
        if(isCollected) {
            collectBtn.setBackground(getResources().getDrawable(R.drawable.collected));
        } else {
            collectBtn.setBackground(getResources().getDrawable(R.drawable.collect));
        }
        insertHistoryNews(newsData);
    }
    public void initListener() {

        String tempstr = newsData.getContent();
        int index = tempstr.indexOf("Notice");
        if(index != -1) {
            tempstr = tempstr.substring(0,tempstr.indexOf("Notice"));
        }
        newsContent.setText(tempstr);

        if(newsData.getType() == 1) {
            String url = newsData.getImage().substring(1,newsData.getImage().length()-1);
            if(url.indexOf(',')!=-1) {
                url = url.substring(0,url.indexOf(','));
            }
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.imageconfig);
            Glide.with(this).load(url).apply(requestOptions).into(newsImage);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCollected) {
                    collectBtn.setBackground(getResources().getDrawable(R.drawable.collected));
                    isCollected = true;
                    insertCollectionNews(newsData);
                    Toast.makeText(NewsActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                } else {
                    collectBtn.setBackground(getResources().getDrawable(R.drawable.collect));
                    isCollected = false;
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("Collection", "news_title = ?", new String[]{newsData.getTitle()});
                    Toast.makeText(NewsActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public Boolean getIsCollected(String newsTitle) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Collection where news_title = ?", new String[]{newsTitle});
        if(cursor.getCount() > 0) {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
            return true;
        } else {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
            return false;
        }
    }
    public void insertCollectionNews(NewsData news) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("news_title", newsData.getTitle());
        contentValues.put("news_date", newsData.getPublishTime());
        contentValues.put("news_publisher", newsData.getPublisher());
        contentValues.put("news_imageUrl", newsData.getImage());
        contentValues.put("news_content", newsData.getContent());
        contentValues.put("news_videoUrl", newsData.getVideo());
        sqLiteDatabase.insert("Collection", null, contentValues);
    }

    public void insertHistoryNews(NewsData news) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from History where news_title = ?", new String[]{news.getTitle()});
        if (cursor.getCount() > 0) {
            sqLiteDatabase.delete("History", "news_title = ?", new String[]{news.getTitle()});
        }
        contentValues.put("news_title", newsData.getTitle());
        contentValues.put("news_date", newsData.getPublishTime());
        contentValues.put("news_publisher", newsData.getPublisher());
        contentValues.put("news_imageUrl", newsData.getImage());
        contentValues.put("news_content", newsData.getContent());
        contentValues.put("news_videoUrl", newsData.getVideo());
        sqLiteDatabase.insert("History", null, contentValues);
    }


}