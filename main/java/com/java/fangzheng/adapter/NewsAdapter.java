package com.java.fangzheng.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.java.fangzheng.R;
import com.java.fangzheng.Bean.NewsData;
import com.java.fangzheng.utils.MyDatabaseHelper;

import java.io.Serializable;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MyDatabaseHelper dbHelper;
    private Context mContext;
    private List<NewsData> data;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<NewsData> data) {
        this.data = data;
    }
    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    public int getItemViewType(int position) {
        int type = data.get(position).getType();
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbHelper = new MyDatabaseHelper(mContext);
        if(viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_news,parent,false);
            ViewHolderOne viewHolderOne = new ViewHolderOne(view);
            return viewHolderOne;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_text_news,parent,false);
            ViewHolderTwo viewHolderTwo = new ViewHolderTwo(view);
            return viewHolderTwo;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        NewsData newsData = data.get(position);
        if(type == 1) {
            ViewHolderOne vh = (ViewHolderOne) holder;
            vh.tvTitle.setText(newsData.getTitle());
            vh.tvPublisher.setText(newsData.getPublisher());
            vh.tvDate.setText(newsData.getPublishTime());
            vh.newsData = newsData;
            if(getIsViewed(newsData.getTitle())) {
                vh.tvTitle.setTextColor(Color.parseColor("#BEBEBE"));
            }

            String url = newsData.getImage().substring(1,newsData.getImage().length()-1);
            if(url.indexOf(',')!=-1) {
                url = url.substring(0,url.indexOf(','));
            }
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.imageconfig);
            Glide.with(mContext).load(url).apply(requestOptions).into(vh.tvImage);
        } else {
            ViewHolderTwo vh = (ViewHolderTwo) holder;
            vh.txTitle.setText(newsData.getTitle());
            vh.txPublisher.setText(newsData.getPublisher());
            vh.txDate.setText(newsData.getPublishTime());
            vh.newsData = newsData;
            if(getIsViewed(newsData.getTitle())) {
                vh.txTitle.setTextColor(Color.GRAY);
            }
        }
    }


    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        }else {
            return data.size();
        }
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvPublisher;
        private ImageView tvImage;
        private NewsData newsData;
        public ViewHolderOne(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDate = view.findViewById(R.id.tvDate);
            tvPublisher = view.findViewById(R.id.tvPublisher);
            tvImage = view.findViewById(R.id.imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击事件
                    mOnItemClickListener.onItemClick(newsData);
                    tvTitle.setTextColor(Color.parseColor("#BEBEBE"));
                }
            });
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        private TextView txTitle;
        private TextView txDate;
        private TextView txPublisher;
        private NewsData newsData;
        public ViewHolderTwo(@NonNull View view) {
            super(view);
            txTitle = view.findViewById(R.id.txTitle);
            txDate = view.findViewById(R.id.txDate);
            txPublisher = view.findViewById(R.id.txPublisher);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击事件
                    mOnItemClickListener.onItemClick(newsData);
                    txTitle.setTextColor(Color.GRAY);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }

    public Boolean getIsViewed(String newsTitle) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from History where news_title = ?", new String[]{newsTitle});
        if(cursor.getCount() > 0) {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        } else {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return false;
        }
    }
}
