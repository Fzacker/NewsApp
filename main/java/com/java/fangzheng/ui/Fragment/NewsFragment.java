package com.java.fangzheng.ui.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.java.fangzheng.R;
import com.java.fangzheng.adapter.NewsAdapter;
import com.java.fangzheng.api.Apiclient;
import com.java.fangzheng.Bean.NewsData;
import com.java.fangzheng.Bean.NewsItem;
import com.java.fangzheng.ui.NewsActivity;
import com.java.fangzheng.utils.MyDatabaseHelper;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragment {
    private String category;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private NewsAdapter newsAdapter;
    List<NewsData> newsData = new ArrayList<>();

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(String category) {
        NewsFragment fragment = new NewsFragment();
        if(category == "推荐") {
            fragment.category = "";
        } else {
            fragment.category = category;
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = v.findViewById(R.id.RecyclerView);
        refreshLayout = v.findViewById(R.id.refreshLayout);

        return v;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getActivity());
        recyclerView.setAdapter(newsAdapter);

        refreshLayout.autoRefresh(0,250,1.0f,true);

        getNewsList(true, String.valueOf(15), "", getDate(System.currentTimeMillis()), "", category);

        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Serializable obj) {
                NewsData newsData = (NewsData) obj;
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsData", newsData);
                navigateToWithBundle(NewsActivity.class, bundle);
                //Toast.makeText(getActivity(), newsData.getImage(), Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(9000);
                getNewsList(true, String.valueOf(50), "", getDate(System.currentTimeMillis()), "", category);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(5000);
                if(newsData.size() > 0) {
                    String endTime = newsData.get(newsData.size()-1).getPublishTime();
                    getNewsList(false, String.valueOf(50), "", endTime, "", category);
                } else {
                    refreshlayout.finishLoadMore(false);
                }
            }
        });

    }

    private void getNewsList(boolean isRefresh, String size, String startDate, String endDate, String words, String categories) {
        Call<NewsItem> call = Apiclient.getInstance().getApi().getNewsItem(size, startDate, endDate, words, categories);
        call.enqueue(new Callback<NewsItem>() {
            @Override
            public void onResponse(Call<NewsItem> call, retrofit2.Response<NewsItem> response) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                if (response.isSuccessful()) {
                    List<NewsData> list = response.body().getData();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            newsData = list;
                        } else {
                            int standard = newsData.size()-7>=0 ? newsData.size()-7 : 0;
                            int standard2 = list.size()<=6 ? list.size() : 6;
                            for (int i = newsData.size()-1; i >= standard ; i--) {
                                for(int j = 0; j<=standard2-1; j++) {
                                    if(newsData.get(i).getTitle().equals(list.get(j).getTitle())) {
                                        list.get(j).setTitle("");
                                    }
                                }
                            }
                            list.removeIf(x->x.getTitle().equals(""));
                            newsData.addAll(list);
                        }
                        newsAdapter.setData(newsData);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "获取新闻列表失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NewsItem> call, Throwable t) {
                //TODO: handle failure
                Toast.makeText(getActivity(), "获取新闻列表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDate(long timeMills) {
        //获取时间
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss"); //设置时间格式
        Date curDate = new Date(timeMills); //获取当前时间
        String createDate = formatter.format(curDate);
        return createDate;
    }
}